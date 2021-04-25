package com.dyb.demo.Service.ServiceImpl;

import com.dyb.demo.Entity.Paper;
import com.dyb.demo.Entity.Question;
import com.dyb.demo.Mapper.PaperMapper;
import com.dyb.demo.Mapper.Paper_QuestionMapper;
import com.dyb.demo.Mapper.QuestionMapper;
import com.dyb.demo.Mapper.Question_PointsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

@Service
public class Paper_QuestionServiceImpl {

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    Question_PointsMapper question_pointsMapper;

    @Autowired
    PaperMapper paperMapper;

    @Autowired
    Paper_QuestionMapper paper_questionMapper;

    public static final double f1 = 0.5;
    public static final double f2 = 0.5;

    //个体种群数目，期望试卷，题库
    public static List<Paper> cszq(int count, Paper paper, List<Question> questionList) {
        //测试计数器
        int testCount = 1;
        List<Paper> unitList = new ArrayList<>();
        Map<Integer, Integer> eachTypeCount = paper.getEachTypeCount();
        Paper unit;
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            unit = new Paper();
            unit.setPaperid(i + 1);
            while (paper.getTotalscore() != unit.getTotalscore()) {
                unit.getQuestionList().clear();
                int j = 1;
                for (Map.Entry<Integer, Integer> entry : eachTypeCount.entrySet()) {
                    List<Question> temp = new ArrayList<>();
                    temp.addAll(questionList);
                    System.out.println("第" + testCount + "次过滤开始" + j++ + "序号");
                    temp = filter(temp, paper, entry.getKey());
                    System.out.println("第" + testCount + "次过滤结束！！！");

                    for (int k = 0; k < entry.getValue(); k++) {
                        int length = temp.size();
                        int pos = random.nextInt(length - k);
                        unit.getQuestionList().add(temp.get(pos));
                        Question ex = temp.get(pos);
                        temp.set(pos, temp.get(length - 1 - k));
                        temp.set(length - 1 - k, ex);
                    }
                }
                unit.setSubject(paper.getSubject());
                unit.setProblemCount(paper.getProblemCount());
                unit.setTotalscore(getTotalScore(unit.getQuestionList()));
                System.out.println("第" + testCount + "次卷子生成，" + "总分是" + unit.getTotalscore());
                testCount++;
            }
            unit.setKPCoverage(getKPCoverage(unit.getQuestionList(), paper));
            unit.setDifficulty(getDifficulty(unit.getQuestionList()));
            unit.setAdaptationDegree(getAdaptationDegree(unit.getKPCoverage(), unit.getDifficulty(), f1, f2, paper));
            unit.setPoints(getPoints(unit.getQuestionList()));
            unit.setEachTypeCount(paper.getEachTypeCount());
            unitList.add(unit);
            System.out.println("第" + (i + 1) + "份初始卷子已经添加");
        }
        return unitList;
    }

    //选择算子，轮盘赌(试卷集合，选择后数量)
    public static List<Paper> select(List<Paper> unitList, int count) {
        List<Paper> selectedList = new ArrayList<>();
        Random random = new Random();
        double allAdaptationDegree = 0.0;
        for (Paper unit : unitList) {
            allAdaptationDegree += unit.getAdaptationDegree();
        }

        while (selectedList.size() != count) {
            double degree = 0.0;
            double randDegree = (random.nextInt(99) + 1) * 0.01 * allAdaptationDegree;
            for (Paper unit : unitList) {
                degree += unit.getAdaptationDegree();
                if (randDegree <= degree) {
                    if (!selectedList.contains(unit)) {
                        selectedList.add(unit);
                        break;
                    }
                }
            }
        }
        return selectedList;
    }

    //交叉算子(样本种群，交叉后个体数，期望试卷)
    public static List<Paper> cross(List<Paper> unitList, int count, Paper paper) {
        List<Paper> crossedList = new ArrayList<>();
        Random random = new Random();
        while (crossedList.size() != count) {
            //交叉个体编号
            int indexone = random.nextInt(unitList.size());
            int indextwo = random.nextInt(unitList.size());
            Paper unitone;
            Paper unittwo;
            //确保交叉个体不同
            if (indexone != indextwo) {
                //测试
                System.out.println("当前交叉个体" + "(" + indexone + "," + indextwo + ")");
                System.out.println("当前已经交叉个数：" + crossedList.size());
                //交叉个体
                unitone = unitList.get(indexone);
                unittwo = unitList.get(indextwo);
                //交叉位置
                int crossPosition = random.nextInt(unitone.getProblemCount() - 2);
                System.out.println("交叉位置" + crossPosition);

                double scoreOne = unitone.getQuestionList().get(crossPosition).getScore() + unitone.getQuestionList().get(crossPosition).getScore();
                double scoreTwo = unittwo.getQuestionList().get(crossPosition).getScore() + unittwo.getQuestionList().get(crossPosition).getScore();

                if (scoreOne == scoreTwo) {
                    Paper unitNewOne = new Paper();
                    Paper unitNewTwo = new Paper();
                    unitNewOne.setQuestionList(unitone.getQuestionList());
                    unitNewTwo.setQuestionList(unittwo.getQuestionList());

                    //交换
                    for (int k = crossPosition; k < crossPosition + 2; k++) {
                        unitNewOne.getQuestionList().set(k, unittwo.getQuestionList().get(k));
                        unitNewTwo.getQuestionList().set(k, unitone.getQuestionList().get(k));
                    }

                    if (crossedList.size() < count) {
                        unitNewOne.setPaperid(crossedList.size());
                        unitNewOne.setSubject(paper.getSubject());
                        //
                        unitNewOne.setTotalscore(getTotalScore(unitNewOne.getQuestionList()));
                        unitNewOne.setEachTypeCount(paper.getEachTypeCount());
                        unitNewOne.setProblemCount(paper.getProblemCount(paper.getEachTypeCount()));
                        unitNewOne.setPoints(getPoints(unitNewOne.getQuestionList()));
                        unitNewOne.setKPCoverage(getKPCoverage(unitNewOne.getQuestionList(), paper));
                        unitNewOne.setDifficulty(getDifficulty(unitNewOne.getQuestionList()));
                        unitNewOne.setAdaptationDegree(getAdaptationDegree(unitNewOne.getKPCoverage(), unitNewOne.getDifficulty(), f1, f2, paper));
                        crossedList.add(unitNewOne);
                    }

                    if (crossedList.size() < count) {
                        unitNewTwo.setPaperid(crossedList.size());
                        unitNewTwo.setSubject(paper.getSubject());
                        unitNewTwo.setTotalscore(getTotalScore(unitNewTwo.getQuestionList()));
                        unitNewTwo.setEachTypeCount(paper.getEachTypeCount());
                        unitNewTwo.setProblemCount(paper.getProblemCount(paper.getEachTypeCount()));
                        unitNewTwo.setPoints(getPoints(unitNewTwo.getQuestionList()));
                        unitNewTwo.setKPCoverage(getKPCoverage(unitNewTwo.getQuestionList(), paper));
                        unitNewTwo.setDifficulty(getDifficulty(unitNewTwo.getQuestionList()));
                        unitNewTwo.setAdaptationDegree(getAdaptationDegree(unitNewTwo.getKPCoverage(), unitNewTwo.getDifficulty(), f1, f2, paper));
                        crossedList.add(unitNewTwo);
                    }
                }
                //去重
            }
        }
        //
        return crossedList;
    }

    //变异算子
    public static List<Paper> change(List<Paper> unitList, Paper paper, List<Question> dbList) {
        Random random = new Random();
        int count = 1;
        for (Paper unit : unitList) {
            System.out.println("这是第" + count++ + "次变异");
            //变异位置
            int index = random.nextInt(unit.getQuestionList().size());
            //取得变异位置题目的有效知识点
            Question problem = unit.getQuestionList().get(index);
            List<Integer> list = new ArrayList<>();
            for (Integer point : problem.getPoints()) {
                if (paper.getPoints().contains(point)) {
                    list.add(point);
                }
            }
            //变异位置题目题型
            int type = problem.getType();
            //找到题库中相同题型，相同有效知识点的替换题集
            List<Question> one = new ArrayList<>();
            List<Question> two = new ArrayList<>();
            List<Question> dbList1 = new ArrayList<>();
            dbList1.addAll(dbList);
            for (Question problem1 : dbList1) {
                if (problem1.getType() != type) {
                    one.add(problem1);
                }
            }
            dbList1.removeAll(one);
            for (Question problem2 : dbList1) {
                for (Integer point : list) {
                    if (problem2.getPoints().contains(point)) {
                        two.add(problem2);
                    }
                }
            }
            //从符合条件的题集two中随机选取位置
            int index2 = random.nextInt(two.size());
            //变异
            unit.getQuestionList().set(index,two.get(index2));
            //重新计算适应度函数和知识点集合
            unit.setPoints(getPoints(unit.getQuestionList()));
            unit.setKPCoverage(getKPCoverage(unit.getQuestionList(), paper));
            unit.setDifficulty(getDifficulty(unit.getQuestionList()));
            unit.setAdaptationDegree(getAdaptationDegree(unit.getKPCoverage(), unit.getDifficulty(), f1, f2, paper));
        }
        return unitList;
    }

    //得到候选试卷集合里的最优解
    public static Paper getPaper(List<Paper> paperList) {
        Paper paperBest = paperList.get(0);
        double max = paperBest.getAdaptationDegree();
        for (Paper paper : paperList) {
            if (paper.getAdaptationDegree() > max) {
                max = paper.getAdaptationDegree();
                paperBest = paper;
            }
        }
        return paperBest;
    }

    //判断是否结束
    public static boolean isEnd(List<Paper> paperList, double expect) {
        for (Paper paper1 : paperList) {
            if (paper1.getAdaptationDegree() >= expect) {
                return true;
            }
        }
        return  false;
    }

    //获取当前试卷的知识点集合
    public static List<Integer> getPoints(List<Question> questionList) {
        List<Integer> Points = new ArrayList<>();
        for (Question question : questionList) {
            for (Integer point : question.getPoints()) {
                Points.add(point);
            }
        }
        HashSet<Integer> set = new HashSet<>(Points);
        Points.clear();
        Points.addAll(set);
        return Points;
    }

    //获取当前试卷的适应度函数
    public static double getAdaptationDegree(double KPCoverage, double diff, double f1, double f2, Paper paper) {
        double adaptationDegree = 1 - (1 - KPCoverage) * f1 - Math.abs(paper.getDifficulty() - diff) * f2;
        DecimalFormat df = new DecimalFormat("0.000");
        adaptationDegree = Double.valueOf(df.format(adaptationDegree));
        return adaptationDegree;
    }

    //获取当前试卷的难度系数
    public static double getDifficulty(List<Question> questionList) {
        double diff = 0.0;
        for (Question question : questionList) {
            diff += question.getScore() * question.getDifficulty();
        }
        DecimalFormat df = new DecimalFormat("0.000");
        double result = diff / getTotalScore(questionList);
        result = Double.valueOf(df.format(result));
        return result;
    }

    //获取当前试卷的知识点分布系数
    public static double getKPCoverage(List<Question> questionList, Paper paper) {
        List<Integer> points = new ArrayList<>();
        for (Question question : questionList) {
            for (Integer point : question.getPoints()) {
                points.add(point);
            }
        }
        HashSet<Integer> set = new HashSet<>(points);
        set.retainAll(paper.getPoints());
        DecimalFormat df = new DecimalFormat("0.000");
        double result = Double.valueOf(df.format((double) set.size() / paper.getPoints().size()));
        return result;
    }

    //获取当前试卷总分
    public static Integer getTotalScore(List<Question> questionList) {
        int sum = 0;
        for (Question question : questionList) {
            sum += question.getScore();
        }
        return sum;
    }

    //过滤题型和知识点不匹配的题目
    public static List<Question> filter(List<Question> temp, Paper paper, int type) {
        List<Question> one = new ArrayList<>();
        List<Question> two = new ArrayList<>();
        for (Question question : temp) {
            if (question.getType() != type) {
                one.add(question);
            }
        }
        temp.removeAll(one);
        for (Question question2 : temp) {
            if (!isContain(paper, question2)) {
                two.add(question2);
            }
        }
        temp.removeAll(two);
        return temp;
    }

    //判断题目知识点和期望试卷知识点是否有交集
    public static boolean isContain(Paper paper, Question question) {
        for (int i = 0; i < question.getPoints().size(); i++) {
            if (paper.getPoints().contains(question.getPoints().get(i))) {
                return true;
            }
        }
        return false;
    }

    //调用生成卷子(总分，难度系数，期望知识点，期望适应度函数，各种题型数量)
    public Paper generate(int totalScore, double difficulty,List<Integer> points, double expect, TreeMap<Integer, Integer> map) {
        List<Question> questionList = questionMapper.findAll();
        for (Question question : questionList) {
            List<Integer> pointList = question_pointsMapper.findByQuestionId(question.getId());
            for (Integer point : pointList) {
                question.getPoints().add(point);
            }
        }
        int count = 1;
        int runcount = 500;
        Paper paper = new Paper();
        paper.setSubject(questionList.get(0).getSubject());
        paper.setProblemCount(paper.getProblemCount(map));
        paper.setTotalscore(totalScore);
        paper.setDifficulty(difficulty);
        paper.setPoints(points);
        paper.setEachTypeCount(map);
        List<Paper> result = cszq(20, paper, questionList);
        System.out.println(result.size() + "份卷子已经生成！");
        System.out.println("迭代开始！！！");
        while (!isEnd(result, expect)) {
            System.out.println("-------------当前为第" + (count++) + "代-----------------------");
            if (count > runcount) {
                System.out.println("计算" + runcount + "代仍然没有结果，请重设迭代结束条件！");
                break;
            }
            System.out.println("选择开始！！！");
            result = select(result, 10);
            System.out.println("选择结束！！！当前选择种群个体总数：" + result.size());
            System.out.println("交叉开始！！！");
            result = cross(result, 20, paper);
            System.out.println("交叉结束！！！");

            if (isEnd(result, expect)) {
                break;
            }
            System.out.println("变异开始！！！");
            result = change(result, paper, questionList);
            System.out.println("当前种群中最好的适应度为：" + getPaper(result).getAdaptationDegree());
            System.out.println("变异结束！！！");

        }
        if (count <= runcount) {
            System.out.println("在第" + count + "代得到结果！");
            System.out.println("当前种群中最好的适应度为：" + getPaper(result).getAdaptationDegree());
        }
        Paper paper1 = getPaper(result);
        //设置索引
        Integer index = paperMapper.findMaxIndex();
        paper1.setPaperid(index + 1);
        return paper1;
    }
}

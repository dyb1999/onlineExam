package com.dyb.demo.Mapper;

import com.dyb.demo.Entity.Score;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface ScoreMapper {

    @Select("select * from t_score")
    List<Score> findAll();

    @Select("select distinct paperid from t_score where examcode=#{examcode}")
    Integer findPaperidByExamcode(int examcode);//设定一场考试对一张卷子不重复

    @Select("select * from t_score where examcode=#{examcode} and paperid=#{paperid} and loginNumber=#{loginNumber}")
    Score findByEPL(int examcode, int paperid, String loginNumber);

    @Select("select * from t_score where examcode=#{examcode}")
    List<Score> findByExamcode(int examcode);

    @Select("select * from t_score where loginNumber=#{loginNumber}")
    List<Score> findByNumber(String loginNumber);

    @Insert("insert into t_score(examcode,loginNumber,paperid,subject,score,level) values (#{examcode},#{loginNumber},#{paperid},#{subject},#{score},#{level})")
    Integer addScoreRecord(Score score);

    @Delete("delete from t_score where examcode=#{examcode} and loginNumber=#{loginNumber}")
    Integer deleteByEL(int examcode, String loginNumber);

    @Delete("delete from t_score where examcode=#{examcode} and paperid=#{paperid} and loginNumber=#{loginNumber}")
    Integer deleteScoreRecord(int examcode, int paperid, String loginNumber);

    @Update("update t_score set examcode=#{examcode}, loginNumber=#{loginNumber}, paperid=#{paperid}, subject=#{subject}, score=#{score}, level=#{level} where examcode=#{examcode} and paperid=#{paperid} and loginNumber=#{loginNumber}")
    Integer updateAllByEPL(Score score);

    @Update("update t_score set score=#{score} where examcode=#{examcode} and paperid=#{paperid} and loginNumber=#{loginNumber}")
    Integer updateScoreByEPL(Score score);

    @Update("update t_score set level=#{level} where examcode=#{examcode} and paperid=#{paperid} and loginNumber=#{loginNumber}")
    Integer updateLevelByEPL(Score score);
}

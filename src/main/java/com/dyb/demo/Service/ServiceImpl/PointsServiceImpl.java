package com.dyb.demo.Service.ServiceImpl;

import com.dyb.demo.Entity.Points;
import com.dyb.demo.Mapper.PointsMapper;
import com.dyb.demo.Service.PointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointsServiceImpl implements PointsService {
    @Autowired
    PointsMapper pointsMapper;

    @Override
    public List<Points> findAll() {
        return pointsMapper.findAll();
    }

    @Override
    public Integer addPoints(Points points) {
        return pointsMapper.addPoints(points);
    }

    //插入到问题_知识点表中
    @Override
    public Integer addQuestionPoint(int question_id, int point_id) {
        return pointsMapper.addQuestionPoint(question_id,point_id);
    }
}

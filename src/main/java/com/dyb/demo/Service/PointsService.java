package com.dyb.demo.Service;

import com.dyb.demo.Entity.Points;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PointsService {

    List<Points> findAll();

    Integer addPoints(Points points);

    Integer addQuestionPoint(int question_id,int point_id);

}

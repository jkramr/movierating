package com.jkramr.demo.repository;

import org.springframework.data.util.Pair;

import java.util.Map;
import java.util.function.BinaryOperator;

public interface RankRepository {

    //duplicates should not happen unless bad state in DB
    BinaryOperator<Integer> degradeDuplicates = (score1, score2) -> -1;

    Long put(Long id, Integer rating);

    Map<Long, Integer> findAll();

    Map<Long, Integer> findRange(long limit);

    void remove(Long id, Integer rating);

}

package com.jkramr.demo.repository;

import java.util.Map;
import java.util.function.BinaryOperator;

public interface RankRepository {

  //duplicates should not happen unless bad state in DB
  BinaryOperator<Integer> degradeDuplicates = (score1, score2) -> -1;

  Long put(Long id, Integer rating);

  Map<Long, Integer> findRange(long start, long end);

  void remove(Long id, Integer rating);

  boolean isEmpty();
}

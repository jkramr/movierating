package com.jkramr.demo.repository;

import lombok.Getter;
import org.springframework.data.redis.core.BoundZSetOperations;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.data.redis.core.ZSetOperations.TypedTuple;

@Getter
public class RedisRankRepository
        implements RankRepository {

  /**
   * Z-set stores key-values along with the score.
   * Specifically useful for leaderboards, rankings etc.
   */
  private final BoundZSetOperations<String, Long> ratingOperations;

  public RedisRankRepository(BoundZSetOperations<String, Long> zSetOperations) {
    ratingOperations = zSetOperations;
  }

  @Override
  public Long put(Long id, Integer rating) {
    ratingOperations.add(id, rating);

    return id;
  }

  @Override
  public Map<Long, Integer> findRange(long start, long end) {
    return ratingOperations.reverseRangeWithScores(start, end)
                           .stream()
                           .collect(Collectors.toMap(
                                   TypedTuple::getValue,
                                   typedTuple -> typedTuple.getScore().intValue(),
                                   degradeDuplicates,
                                   LinkedHashMap::new
                           ));
  }

  @Override
  public void remove(Long id, Integer rating) {
    ratingOperations.remove(id);
  }

  @Override
  public boolean isEmpty() {
    return ratingOperations.size() == 0;
  }
}

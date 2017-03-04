package com.jkramr.demo.repository;

import lombok.Getter;
import org.springframework.data.redis.core.BoundZSetOperations;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.data.redis.core.ZSetOperations.*;

@Getter
public class RedisRankRepository implements RankRepository {

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
    public Map<Long, Integer> findAll() {
        return findRange(ratingOperations.size());
    }

    @Override
    public Map<Long, Integer> findRange(long limit) {
        return ratingOperations.reverseRangeWithScores(0, limit - 1)
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
}

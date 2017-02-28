package com.jkramr.demo.repository;

import lombok.Getter;
import org.springframework.data.redis.core.BoundZSetOperations;

import java.util.Collection;

@Getter
public class RedisRankRepository implements RankRepository {

    /**
     * Z-set stores key-values along with the score.
     * Specifically useful for leaderboards, rankings etc.
     * Easy access to records
     */
    private final BoundZSetOperations<String, Long> ratingOperations;

    public RedisRankRepository(BoundZSetOperations<String, Long> zSetOperations) {
        ratingOperations = zSetOperations;
    }

    @Override
    public Long put(Long id, Integer rating) {
        ratingOperations.add(id, rating);

        return rank(id, rating);
    }

    @Override
    public Long rank(Long id, Integer rating) {
        return ratingOperations.rank(id);
    }

    @Override
    public Collection<Long> findAll() {
        return findFirst(ratingOperations.size());
    }

    @Override
    public Collection<Long> findFirst(long limit) {
        return ratingOperations.reverseRange(0, limit);
    }

    @Override
    public void remove(Long id, Integer rating) {
        ratingOperations.remove(id);
    }
}

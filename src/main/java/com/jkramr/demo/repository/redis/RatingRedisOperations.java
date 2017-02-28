package com.jkramr.demo.repository.redis;

import lombok.Getter;
import org.springframework.data.redis.core.BoundZSetOperations;

import java.util.Collection;
import java.util.Set;

@Getter
public class RatingRedisOperations {

    private final BoundZSetOperations<String, Long> ratingOperations;

    public RatingRedisOperations(BoundZSetOperations<String, Long> zSetOperations) {
        ratingOperations = zSetOperations;
    }

    public void put(Long id, Integer rating) {
        ratingOperations.add(id, rating);
    }

    public Long getRank(Long id) {
        return ratingOperations.rank(id);
    }

    public Collection<Long> findAll() {
        return findFirst(ratingOperations.size());
    }

    public Collection<Long> findFirst(long limit) {
        return ratingOperations.range(0, limit);
    }
}

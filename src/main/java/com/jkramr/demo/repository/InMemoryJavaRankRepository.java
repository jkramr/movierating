package com.jkramr.demo.repository;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryJavaRankRepository implements RankRepository {

    /**
     * Map ratings -> corresponding set of movie ids
     *
     * Stub implementation using in-memory ranking storage (red-black tree, sorted by score)
     * Will die when memory runs out, not persistent, use Redis!
     *
     * Complexity
     *
     * Time:
     * Insertion: O(log(n))
     * Search: O(log(n)) normally, but more -> O(n) with growing rating collisions
     *
     * Space: O(n)
     */
    private TreeMap<Integer, Set<Long>> ratings;

    public InMemoryJavaRankRepository() {
        this.ratings = new TreeMap<>(Comparator.reverseOrder());
    }

    @Override
    public Long put(Long id, Integer rating) {
        ratings.putIfAbsent(rating, new TreeSet<>());
        ratings.get(rating).add(id);

        return rank(id, rating);
    }

    @Override
    public Long rank(Long id, Integer rating) {
        long counter = 0;

        for (Integer score : ratings.keySet()) {
            if (Objects.equals(score, rating)) {
                return counter;
            }
            counter++;
        }

        return counter;
    }

    @Override
    public Collection<Long> findAll() {
        return ratings.keySet()
                .stream()
                .flatMap(key -> ratings.get(key).stream())
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Long> findFirst(long limit) {
        return ratings.keySet()
                .stream()
                .flatMap(key -> ratings.get(key).stream())
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public void remove(Long id, Integer rating) {
        ratings.get(rating).remove(id);
    }
}

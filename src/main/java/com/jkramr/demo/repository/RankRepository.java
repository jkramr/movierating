package com.jkramr.demo.repository;

import java.util.Collection;

public interface RankRepository {

    Long put(Long id, Integer rating);

    Long rank(Long id, Integer rating);

    Collection<Long> findAll();

    Collection<Long> findFirst(long limit);

    void remove(Long id, Integer rating);
}

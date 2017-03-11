package com.jkramr.demo.repository;

import org.springframework.data.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryJavaRankRepository
        implements RankRepository {

  /**
   * Map ratings -> corresponding set of movie ids
   * <p>
   * Stub implementation using in-memory ranking storage (red-black tree, sorted by score)
   * Will die when memory runs out, not persistent, use Redis!
   * <p>
   * Complexity
   * <p>
   * Time:
   * Insertion: O(log(n))
   * Search: O(log(n)) normally, but more -> O(n) with growing rating collisions
   * <p>
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

    return id;
  }

  @Override
  public Map<Long, Integer> findRange(long start, long end) {
    long maxSize = end - start + 1;

    return ratings.keySet()
                  .stream()
                  .flatMap(key -> ratings.get(key).stream().map(id -> Pair.of(id, key)))
                  .skip(start)
                  .limit(maxSize)
                  .collect(Collectors.toMap(
                          Pair::getFirst,
                          Pair::getSecond,
                          degradeDuplicates,
                          LinkedHashMap::new
                  ));
  }

  @Override
  public void remove(Long id, Integer rating) {
    ratings.get(rating).remove(id);
  }

  @Override
  public boolean isEmpty() {
    return ratings.isEmpty();
  }

}

package com.jkramr.demo.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MovieRankRecord {

  private final Movie movie;
  private final Long  rank;
}

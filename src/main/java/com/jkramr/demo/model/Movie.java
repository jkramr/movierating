package com.jkramr.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Movie {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private Integer year;

    @NonNull
    private Integer rating;

    @NonNull
    private String movieTitle;
}

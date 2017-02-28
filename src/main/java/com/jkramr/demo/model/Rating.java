package com.jkramr.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Calendar;

/**
 *
 */
@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Rating {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private Long rank;

    @NonNull
    private Long timestamp;

    @NonNull
    private Long movieId;

    @NonNull
    private Integer rating;

    @NonNull
    private String movieTitle;

    @Override
    public String toString() {
        return rank + " " + movieTitle;
    }

}

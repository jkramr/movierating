package com.jkramr.demo;

import com.jkramr.demo.repository.RatingRecordRepository;
import com.jkramr.demo.repository.redis.RatingRedisOperations;
import com.jkramr.demo.repository.MovieRepository;
import com.jkramr.demo.service.MovieRatingService;
import com.jkramr.demo.service.provider.RedisMovieRatingService;
import com.jkramr.demo.service.provider.PlainJavaMovieRatingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@SpringBootApplication
public class MovieRatingApplication {

    /**
     * Enables Redis-driven rating calculation
     * 12-step reconfigurable property (code default -> application.properties -> environment variable etc.)
     */
    @Value("${redis:false}")
    Boolean redis;

    @Bean
    RedisTemplate<String, Long> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate<String, Long> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(Long.class));
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    @Bean
    MovieRatingService movieRatingService(
            MovieRepository movieRepository,
            RatingRedisOperations ratingRedisOperations,
            RatingRecordRepository ratingRecordRepository
    ) {
        return redis
                ? new RedisMovieRatingService(movieRepository, ratingRecordRepository, ratingRedisOperations)
                : new PlainJavaMovieRatingService(movieRepository, ratingRecordRepository);
    }

    @Bean
    RatingRedisOperations movieRatingRedisRepository(RedisTemplate<String, Long> stringLongredisTemplate) {
        return new RatingRedisOperations(stringLongredisTemplate.boundZSetOps("movie_rating"));
    }

    public static void main(String[] args) {
        SpringApplication.run(MovieRatingApplication.class, args);
    }
}


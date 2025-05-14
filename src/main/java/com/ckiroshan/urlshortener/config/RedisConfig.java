package com.ckiroshan.urlshortener.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching // Enables Spring's annotation-driven cache management
public class RedisConfig {
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        // Creates a Redis connection factory using Lettuce client
        return new LettuceConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        // Creates a RedisTemplate with String keys and JSON-serialized values
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        // Set key serializer to String
        template.setKeySerializer(new StringRedisSerializer());
        // Set value serializer to JSON serializer
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}
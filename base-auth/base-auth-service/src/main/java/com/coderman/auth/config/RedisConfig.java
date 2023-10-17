package com.coderman.auth.config;

import com.coderman.redis.config.BaseRedisConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPoolConfig;

@Component
@Configuration("authRedisConfig")
public class RedisConfig extends BaseRedisConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.redis")
    @Primary
    public RedisProperties authRedisProperties(){
        return new RedisProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.redis.jedis.pool")
    public JedisPoolConfig authJedisPoolConfig(){
        return new JedisPoolConfig();
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(@Qualifier("authRedisProperties") RedisProperties properties, @Qualifier("authJedisPoolConfig") JedisPoolConfig jedisPoolConfig){
        return createJedisConnectionFactory(properties, jedisPoolConfig);
    }

    @Bean
    public StringRedisTemplate redisTemplate(@Qualifier("jedisConnectionFactory") JedisConnectionFactory jedisConnectionFactory){
        return createStringRedisTemplate(jedisConnectionFactory);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(@Qualifier("jedisConnectionFactory") JedisConnectionFactory jedisConnectionFactory){
        return createMessageListenerContainer(jedisConnectionFactory);
    }



}

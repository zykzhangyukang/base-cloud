package com.coderman.sync.config;

import com.coderman.redis.config.BaseRedisConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration("syncRedisConfig")
@Slf4j
public class RedisConfig extends BaseRedisConfig {
    
    @Bean
    @ConfigurationProperties(prefix = "spring.redis")
    @Primary
    public RedisProperties syncRedisProperties(){
        return new RedisProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.redis.jedis.pool")
    public JedisPoolConfig syncJedisPoolConfig(){
        return new JedisPoolConfig();
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(@Qualifier("syncRedisProperties") RedisProperties properties, @Qualifier("syncJedisPoolConfig") JedisPoolConfig jedisPoolConfig){
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

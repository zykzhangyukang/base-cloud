package com.coderman.sync.config;

import com.coderman.service.config.BaseRedisConfig;
import com.coderman.sync.constant.PlanConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import javax.annotation.Resource;

@Configuration
@Slf4j
@SuppressWarnings("all")
public class RedisConfig extends BaseRedisConfig {

    @Resource
    private RedisConnectionFactory jedisConnectionFactory;

    @Resource
    private MessageListenerAdapter messageListenerAdapter;

    @Bean
    public RedisMessageListenerContainer container() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);
        container.addMessageListener(messageListenerAdapter, new PatternTopic(PlanConstant.PLAN_REFRESH_KEY));
        return container;
    }
}

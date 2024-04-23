package ru.sixbeans.meetingengine.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 259200) // 3 days
public class RedisSessionConfiguration {

}

package com.serkan.GraphQL_Project.config;

import com.serkan.GraphQL_Project.model.CourseEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Configuration
public class SubscriptionConfig {

    // Publisher -- multicast -- thread safe buffer
    @Bean
    public Sinks.Many<CourseEvent> courseEventSink() {
        return Sinks.many().multicast().onBackpressureBuffer();
    }

    // subscribers will listen here and will see the updates thanks to FLux
    @Bean
    public Flux<CourseEvent> courseEventFlux(Sinks.Many<CourseEvent> sink) {
        return sink.asFlux().share();
    }
}
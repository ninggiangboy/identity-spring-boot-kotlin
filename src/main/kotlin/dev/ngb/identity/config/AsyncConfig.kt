package dev.ngb.identity.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.annotation.EnableAsync


@Configuration
@EnableAsync
class AsyncConfig {
    @Bean
    fun taskExecutor(): TaskExecutor {
        return SimpleAsyncTaskExecutor()
    }
}
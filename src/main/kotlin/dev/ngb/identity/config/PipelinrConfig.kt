package dev.ngb.identity.config

import an.awesome.pipelinr.Command
import an.awesome.pipelinr.CommandHandlers
import an.awesome.pipelinr.Pipeline
import an.awesome.pipelinr.Pipelinr
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.ObjectProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PipelinrConfig {

    val logger: Logger = LoggerFactory.getLogger(PipelinrConfig::class.java)

    @Bean
    fun pipeline(
        commandHandlers: ObjectProvider<Command.Handler<*, *>?>,
        middlewares: ObjectProvider<Command.Middleware?>
    ): Pipeline {
        logger.info("Found ${commandHandlers.stream().count()} pipelinr command handlers")
        logger.info("Found ${middlewares.orderedStream().count()} pipelinr middlewares")
        return Pipelinr()
            .with(CommandHandlers { commandHandlers.stream() })
            .with(Command.Middlewares { middlewares.orderedStream() })
    }
}
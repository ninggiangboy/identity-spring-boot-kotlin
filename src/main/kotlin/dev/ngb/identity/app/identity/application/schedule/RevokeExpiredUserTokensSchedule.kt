package dev.ngb.identity.app.identity.application.schedule

import dev.ngb.identity.service.TokenService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RevokeExpiredUserTokensSchedule(private val tokenService: TokenService) {
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.HOURS)
    fun scheduleTask() {
        tokenService.revokeExpiredUserTokens()
    }
}
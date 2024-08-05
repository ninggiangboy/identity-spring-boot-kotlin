package dev.ngb.identity.app.identity.application.schedule

import dev.ngb.identity.app.identity.domain.repository.AppUserRepository
import dev.ngb.identity.config.property.TokenProperties
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.concurrent.TimeUnit

@Component
class RevokeUnconfirmedUserSchedule(
    private val appUserRepository: AppUserRepository,
    private val tokenProperties: TokenProperties
) {
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.HOURS)
    fun scheduleTask() {
        val invalidTime = Instant.now().minusMillis(tokenProperties.verificationTokenDuration)
        val ids = appUserRepository.findIdByIsEnabledIsFalseAndCreatedAtBefore(invalidTime)
        if (ids.isNotEmpty()) appUserRepository.deleteByIdIn(ids)
    }
}
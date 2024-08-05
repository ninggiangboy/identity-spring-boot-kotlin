package dev.ngb.identity.app.identity.domain.repository

import dev.ngb.chatapp.app.identity.domain.entity.AppUser
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.RepositoryDefinition
import java.time.Instant
import java.util.*

@RepositoryDefinition(domainClass = AppUser::class, idClass = UUID::class)
interface AppUserRepository {
    @EntityGraph(attributePaths = ["roles", "roles.permissions"])
    fun findById(id: UUID): AppUser?
    @EntityGraph(attributePaths = ["roles", "roles.permissions"])
    fun findByUsernameOrEmail(username: String, email: String): AppUser?
    fun existsByUsername(username: String): Boolean
    fun findByEmail(email: String): AppUser?
    fun save(user: AppUser): AppUser
    @Query("SELECT u.id FROM AppUser u WHERE u.isEnabled = false AND u.createdAt < :time")
    fun findIdByIsEnabledIsFalseAndCreatedAtBefore(time: Instant): List<UUID>
    fun deleteByIdIn(id: List<UUID>)
    fun deleteById(id: UUID)
}
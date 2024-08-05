package dev.ngb.chatapp.app.identity.domain.entity

import dev.ngb.identity.app.identity.domain.entity.AppRole
import jakarta.persistence.*
import java.time.Instant
import java.util.*

@Entity
@Table(name = "app_users", schema = "public")
open class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    open var id: UUID? = null

    @Column(name = "username")
    open var username: String? = null

    @Column(name = "hash_password")
    open var hashPassword: String? = null

    @Column(name = "email")
    open var email: String? = null

    @Column(name = "is_enabled")
    open var isEnabled: Boolean? = null

    @Column(name = "is_looked")
    open val isLooked: Boolean? = null

    @Column(name = "created_at")
    open var createdAt: Instant? = null

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "app_user_role",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    open var roles: MutableSet<AppRole> = mutableSetOf()
}
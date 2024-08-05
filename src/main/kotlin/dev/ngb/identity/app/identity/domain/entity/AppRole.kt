package dev.ngb.identity.app.identity.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "app_roles", schema = "public")
open class AppRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    open var id: Int? = null

    @Column(name = "role_name")
    open var name: String? = null

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "app_role_permission",
        joinColumns = [JoinColumn(name = "role_id")],
        inverseJoinColumns = [JoinColumn(name = "permission_id")]
    )
    open var permissions: MutableSet<AppPermission> = mutableSetOf()
}
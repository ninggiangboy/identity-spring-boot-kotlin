package dev.ngb.identity.app.identity.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "app_permissions", schema = "public")
open class AppPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    open var id: Int? = null

    @Column(name = "permission_name")
    open var name: String? = null
}
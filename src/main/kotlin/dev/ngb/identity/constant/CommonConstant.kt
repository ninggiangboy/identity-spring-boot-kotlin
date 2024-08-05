package dev.ngb.identity.constant

interface CommonConstant {
    companion object {
        const val ROLE_PREFIX = "ROLE_"
        const val ROLES_CLAIM = "roles"
        const val PERMISSIONS_CLAIM = "permissions"
        const val USER_TOKEN_PREFIX = "USER-"
    }
}
package dev.ngb.identity.app.identity.domain.constant

interface RegexPattern {
    companion object {
        const val USERNAME_PATTERN: String = "^[a-zA-Z0-9]*\$"
        const val PASSWORD_PATTERN: String = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@\$!%*?&]{8,}\$"
    }
}
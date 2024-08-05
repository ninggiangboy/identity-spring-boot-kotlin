package dev.ngb.identity.app.identity.api

import dev.ngb.identity.app.identity.application.command.login.LoginRequest
import dev.ngb.identity.app.identity.application.command.register.RegisterRequest
import dev.ngb.identity.common.api.DataResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RequestMapping("/api/identity")
interface IdentityApi {
    @GetMapping("/whoami")
    @PreAuthorize("authenticated")
    @ResponseStatus(HttpStatus.OK)
    fun whoAmI(@AuthenticationPrincipal user: Principal): DataResponse

    @PostMapping("/login")
    @PreAuthorize("permitAll")
    @ResponseStatus(HttpStatus.OK)
    fun login(@Valid @RequestBody request: LoginRequest): DataResponse

    @PostMapping("/refresh-token")
    @PreAuthorize("permitAll")
    @ResponseStatus(HttpStatus.OK)
    fun refreshToken(@RequestParam(name = "token") token: String): DataResponse

    @DeleteMapping("/logout/{token}")
    @PreAuthorize("authenticated")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(
        @PathVariable(name = "token") token: String,
        @AuthenticationPrincipal user: Principal
    ): DataResponse

    @DeleteMapping("/logout")
    @PreAuthorize("authenticated")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logoutAll(@AuthenticationPrincipal user: Principal): DataResponse

    @PostMapping("/register")
    @PreAuthorize("permitAll")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@Valid @RequestBody request: RegisterRequest): DataResponse

    @PostMapping("/email/confirm")
    @PreAuthorize("permitAll")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun verifyEmail(@RequestParam(name = "token") token: String): DataResponse

    @PostMapping("/email/confirm/resend")
    @PreAuthorize("permitAll")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun resendVerificationEmail(@RequestParam(name = "email") email: String): DataResponse

    @PostMapping("/password/reset")
    @PreAuthorize("permitAll")
    @ResponseStatus(HttpStatus.OK)
    fun resetPassword(@RequestParam(name = "email") email: String): DataResponse

    @GetMapping("/password/reset/confirm")
    @PreAuthorize("permitAll")
    @ResponseStatus(HttpStatus.OK)
    fun confirmResetPassword(@RequestParam(name = "token") token: String): DataResponse

    @PostMapping("/password/reset/confirm")
    @PreAuthorize("permitAll")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun confirmResetPassword(
        @RequestParam(name = "token") token: String,
        @RequestParam(name = "password") password: String
    ): DataResponse

    @PostMapping("/password/change")
    @PreAuthorize("authenticated")
    @ResponseStatus(HttpStatus.OK)
    fun changePassword(
        @RequestParam(name = "old_password") oldPassword: String,
        @RequestParam(name = "new_password") newPassword: String,
        @AuthenticationPrincipal user: Principal
    ): DataResponse

    @PostMapping("/email/change")
    @PreAuthorize("authenticated")
    @ResponseStatus(HttpStatus.OK)
    fun changeEmail(
        @RequestParam(name = "new_email") newEmail: String,
        @AuthenticationPrincipal user: Principal
    ): DataResponse
}
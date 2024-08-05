package dev.ngb.identity.common.annotation.validation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import org.springframework.web.multipart.MultipartFile
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [File.FileValidator::class])
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class File(
    val message: String = "Invalid image file",
    val invalidExtensionMessage: String = "Invalid image file extension",
    val maxSizeExceededMessage: String = "File size exceeds the maximum allowed",
    val emptyFileMessage: String = "File is empty",
    val maxFileSizeInMB: Long = 1, // Default 1MB
    val allowedExtensions: Array<String> = [],
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
) {
    class FileValidator : ConstraintValidator<File, MultipartFile> {

        private var maxFileSize: Long = 0
        private lateinit var allowedExtensions: Set<String>
        private lateinit var invalidExtensionMessage: String
        private lateinit var maxSizeExceededMessage: String
        private lateinit var emptyFileMessage: String

        override fun initialize(annotation: File) {
            this.maxFileSize = annotation.maxFileSizeInMB * 1024 * 1024
            this.invalidExtensionMessage = annotation.invalidExtensionMessage
            this.maxSizeExceededMessage = annotation.maxSizeExceededMessage
            this.emptyFileMessage = annotation.emptyFileMessage
            this.allowedExtensions = annotation.allowedExtensions.map { it.lowercase() }.toSet()
        }

        override fun isValid(value: MultipartFile?, context: ConstraintValidatorContext): Boolean {
            // If the file is not provided, it is considered valid, can be made required by adding @NotNull
            if (value == null) {
                return true
            }

            if (value.isEmpty) {
                context.setErrorMessage(emptyFileMessage)
                return false
            }

            val extension = getExtension(value.originalFilename ?: "")

            if (allowedExtensions.isNotEmpty() && !allowedExtensions.contains(extension)) {
                context.setErrorMessage(invalidExtensionMessage)
                return false
            }

            if (value.size > maxFileSize) {
                context.setErrorMessage(maxSizeExceededMessage)
                return false
            }

            return true
        }

        private fun ConstraintValidatorContext.setErrorMessage(message: String) {
            disableDefaultConstraintViolation()
            buildConstraintViolationWithTemplate(message).addConstraintViolation()
        }

        private fun getExtension(filename: String): String {
            val parts = filename.split(".")
            return if (parts.size > 1) parts.last().lowercase() else ""
        }
    }
}
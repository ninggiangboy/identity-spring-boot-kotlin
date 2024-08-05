package dev.ngb.identity.service

import jakarta.mail.MessagingException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

interface EmailService {
    fun sendEmail(email: String, subject: String, message: String)
    fun sendEmail(subject: String, emailTo: String, template: String, data: Map<String?, Any?>)
}

@Service
class EmailServiceImpl(
    private val emailSender: JavaMailSender,
    private val templateEngine: TemplateEngine
) : EmailService {

    override fun sendEmail(email: String, subject: String, message: String) {
        send(subject, email, message, false)
    }

    override fun sendEmail(subject: String, emailTo: String, template: String, data: Map<String?, Any?>) {
        val templateContext = Context()
        templateContext.setVariables(data)
        val content = templateEngine.process(template, templateContext)
        send(subject, emailTo, content, true)
    }

    private fun send(subject: String, emailTo: String, content: String, isHtmlFormat: Boolean) {
        try {
            val mimeMessage = emailSender.createMimeMessage()
            val message = MimeMessageHelper(mimeMessage, "utf-8")

            message.setSubject(subject)
            message.setText(content, isHtmlFormat)
            message.setTo(emailTo)

            emailSender.send(mimeMessage)
        } catch (e: MessagingException) {
            throw IllegalStateException("Failed to send email to $emailTo")
        }
    }
}

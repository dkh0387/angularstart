package de.dkh.cafemanagementbackend.utils

import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.mail.MailAuthenticationException
import org.springframework.mail.MailException
import org.springframework.mail.MailParseException
import org.springframework.mail.MailSendException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service

/**
 * @TODO: testing!
 */
@Service
@PropertySource("classpath:application.properties")
class EmailUtilsService(private val emailSender: JavaMailSender, private val environment: Environment) : EmailUtils {
    @Throws(
        MailException::class,
        MailAuthenticationException::class,
        MailParseException::class,
        MailSendException::class
    )
    override fun sendSimpleMessage(to: String, subject: String, text: String, emails: List<String>) {
        val simpleMailMessage = SimpleMailMessage()
        simpleMailMessage.from = environment["spring.mail.username"]
        simpleMailMessage.setTo(to)
        simpleMailMessage.subject = subject
        simpleMailMessage.text = text
        simpleMailMessage.setCc(emails.first { it != to }) // sending an email to one another admin
        emailSender.send(simpleMailMessage)
    }

    private fun List<String>.concat() = this.joinToString(",")

}
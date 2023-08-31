package de.dkh.cafemanagementbackend.utils

import de.dkh.cafemanagementbackend.constants.CafeConstants
import jakarta.mail.MessagingException
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.mail.MailAuthenticationException
import org.springframework.mail.MailException
import org.springframework.mail.MailParseException
import org.springframework.mail.MailSendException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

/**
Sending emails.
NOTE: we need to turn third party app support for a Google account in order to make it work!
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
        simpleMailMessage.from = environment.getProperty("spring.mail.username")
        simpleMailMessage.setTo(to)
        simpleMailMessage.subject = subject
        simpleMailMessage.text = text
        simpleMailMessage.setCc(emails.first { it != to }) // sending an email to one another admin
        emailSender.send(simpleMailMessage)
    }

    /**
     * @TODO: testing!
     */
    @Throws(MessagingException::class)
    override fun forgotEmail(to: String, subject: String, password: String) {
        val mimeMessage = emailSender.createMimeMessage()
        val helper = MimeMessageHelper(mimeMessage, true)
        helper.setFrom(environment.getProperty("spring.mail.username")!!)
        helper.setTo(to)
        helper.setSubject(subject)
        val htmlMessage =
            "String htmlMsg = \"<p><b>Your Login details for Cafe Management System</b><br><b>Email: </b> \" $to \" <br><b>temporary Password (change it after the login): </b> \" $password \"<br><a href=\\\"http://localhost:4200/\\\">Click here to login</a></p>\";"
        mimeMessage.setContent(htmlMessage, CafeConstants.MESSAGE_TYPE_TEXT_HTML)
        emailSender.send(mimeMessage)
    }

    private fun List<String>.concat() = this.joinToString(",")

}
package de.dkh.cafemanagementbackend.utils

import io.mockk.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.core.env.Environment
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import java.util.NoSuchElementException

class EmailUtilsServiceTest {

    private val emailSender: JavaMailSender = mockk<JavaMailSender>()
    private val environment = mockk<Environment>()
    private val objectUnderTest = EmailUtilsService(emailSender, environment)

    @Nested
    @DisplayName("Send email Testing")
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    inner class SendEmailTesting {

        @Test
        fun `should throw an Exception if emailSender does`() {
            // given
            every { environment.getProperty("spring.mail.username") } returns "deniskh87@gmail.com"
            every { emailSender.send(any<SimpleMailMessage>()) } throws RuntimeException("Authentication failed!")

            // when/then
            Assertions.assertThatThrownBy {
                objectUnderTest.sendSimpleMessage(
                    "deniskh87@gmail.com",
                    "Status update",
                    "Any text",
                    listOf("deniskh87@gmail.com")
                )
            }
                .isInstanceOf(RuntimeException::class.java)
        }

        @Test
        fun `should throw an Exception if admin email list is empty`() {
            // given
            every { environment.getProperty("spring.mail.username") } returns "deniskh87@gmail.com"
            every { emailSender.send(any<SimpleMailMessage>()) } throws RuntimeException("Authentication failed!")

            // when/then
            Assertions.assertThatThrownBy {
                objectUnderTest.sendSimpleMessage(
                    "deniskh87@gmail.com",
                    "Status update",
                    "Any text",
                    emptyList()
                )
            }
                .isInstanceOf(NoSuchElementException::class.java)
        }

        @Test
        fun `should send an email if everything is ok`() {
            // given
            every { environment.getProperty("spring.mail.username") } returns "deniskh87@gmail.com"
            every { emailSender.send(any<SimpleMailMessage>()) } just runs

            // when
            objectUnderTest.sendSimpleMessage(
                "deniskh87@gmail.com",
                "Status update",
                "Any text",
                listOf("test87@gmail.com")
            )

            // then
            verify(exactly = 1)
            {
                emailSender.send(any<SimpleMailMessage>())
            }
        }
    }
}
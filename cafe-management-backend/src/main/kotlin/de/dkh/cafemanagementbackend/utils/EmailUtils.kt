package de.dkh.cafemanagementbackend.utils

interface EmailUtils {

    fun sendSimpleMessage(to: String, subject: String, text: String, emails: List<String>)

    fun forgotEmail(to: String, subject: String, password: String)
}
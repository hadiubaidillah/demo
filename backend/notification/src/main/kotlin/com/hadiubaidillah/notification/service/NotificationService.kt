package com.hadiubaidillah.notification.service

import com.hadiubaidillah.notification.entity.Notification
import com.hadiubaidillah.notification.entity.toNotification
import com.hadiubaidillah.notification.repository.NotificationRepository
import com.hadiubaidillah.notification.repository.TemplateRepository
import com.hadiubaidillah.shared.model.EmailRequest
import com.hadiubaidillah.shared.model.Recipient
import jakarta.mail.*
import jakarta.mail.internet.InternetAddress
import jakarta.mail.internet.MimeMessage
import org.jetbrains.exposed.sql.transactions.transaction
import java.nio.charset.StandardCharsets
import java.util.*

val properties = Properties().apply {
    put("mail.smtp.host", "smtp.gmail.com")
    put("mail.smtp.port", "587")
    put("mail.smtp.auth", "true")
    put("mail.smtp.starttls.enable", "true")
}

val email: String? = System.getenv("EMAIL_ADDRESS")
val password: String? = System.getenv("EMAIL_PASSWORD")
val personal: String? = System.getenv("EMAIL_PERSONAL")

val session: Session? = Session.getInstance(properties, object : Authenticator() {
    override fun getPasswordAuthentication() = PasswordAuthentication(email, password)
})

fun List<Recipient>.toInternetAddresses(): Array<InternetAddress> =
    map { InternetAddress(it.email, it.name ?: it.email) }.toTypedArray()


class NotificationService(
    val notificationRepository: NotificationRepository,
    val templateRepository: TemplateRepository
) {

    fun getAll(authorId: UUID) = notificationRepository.findAllByAuthorId(authorId)

    fun getAllUnread(authorId: UUID) = notificationRepository.findAllUnreadByAuthorId(authorId)

    fun getByIdAndAuthorId(id: UUID, authorId: UUID): Notification? =
        notificationRepository.findByIdAndAuthorId(id, authorId)

    fun delete(id: UUID, authorId: UUID): Boolean = transaction {
        val notification: Notification = getByIdAndAuthorId(id, authorId) ?: throw Exception("Notification not found")
        notificationRepository.delete(UUID.fromString(notification.id))
    }

    fun markAllAsRead(authorId: UUID) {
        notificationRepository.markAllAsRead(authorId)
    }

    fun markAsRead(id: UUID, authorId: UUID) {
        val notification = this.getByIdAndAuthorId(id, authorId) ?: throw Exception("Notification not found")
        notification.read = true
        notificationRepository.update(id, notification)
    }

    fun sendNoReplyEmail(emailRequest: EmailRequest) {

        val template = templateRepository.getByCode(emailRequest.code) ?: throw Exception("Template not found")

        emailRequest.subject = template.subject
        var html = getEmailTemplate(template.file)

        emailRequest.parameters?.forEach { (key, value) ->
            emailRequest.subject = emailRequest.subject?.replace("{{$key}}", value)
            html = html.replace("{{$key}}", value)
        }
        println("html:\n$html")

        val toAddresses = emailRequest.to.toInternetAddresses()
        val ccAddresses = emailRequest.cc?.toInternetAddresses()
        val bccAddresses = emailRequest.bcc?.toInternetAddresses()

        val message = MimeMessage(session)

        message.addRecipients(Message.RecipientType.TO, toAddresses)
        ccAddresses?.let { message.addRecipients(Message.RecipientType.CC, it) }
        bccAddresses?.let { message.addRecipients(Message.RecipientType.BCC, it) }

        message.setFrom(InternetAddress(email, personal))
        message.subject = emailRequest.subject
        message.setContent(html, "text/html")

        notificationRepository.add(emailRequest.toNotification())

        Transport.send(message)
    }

}

fun getEmailTemplate(fileName: String): String {
    // Use the class loader to load the HTML file from resources/email/
    val classLoader = Thread.currentThread().contextClassLoader
    val inputStream = classLoader.getResourceAsStream("templates/email/$fileName")
        ?: throw IllegalArgumentException("Email template $fileName not found")

    // Read the input stream and convert it to a String
    return inputStream.bufferedReader(StandardCharsets.UTF_8).use { it.readText() }
}
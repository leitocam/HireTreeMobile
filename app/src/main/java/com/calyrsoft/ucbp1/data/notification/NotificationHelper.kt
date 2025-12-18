package com.hiretree.mobile.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.calyrsoft.ucbp1.MainActivity
import com.calyrsoft.ucbp1.R

/**
 * Helper para gestionar notificaciones locales de la app
 */
class NotificationHelper(private val context: Context) {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannels()
    }

    /**
     * Crea los canales de notificación (requerido en Android O+)
     */
    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Canal para notificaciones de entrevistas
            val interviewChannel = NotificationChannel(
                CHANNEL_INTERVIEW,
                "Entrevistas",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notificaciones sobre entrevistas y evaluaciones"
            }

            // Canal para notificaciones de certificados
            val certificateChannel = NotificationChannel(
                CHANNEL_CERTIFICATES,
                "Certificados",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificaciones sobre certificados generados"
            }

            // Canal para recordatorios
            val reminderChannel = NotificationChannel(
                CHANNEL_REMINDERS,
                "Recordatorios",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Recordatorios para practicar entrevistas"
            }

            notificationManager.createNotificationChannels(
                listOf(interviewChannel, certificateChannel, reminderChannel)
            )
        }
    }

    /**
     * Muestra notificación cuando se completa una entrevista
     */
    fun showInterviewCompletedNotification() {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "results")
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID_INTERVIEW_COMPLETED,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_INTERVIEW)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("¡Entrevista Completada! 🎉")
            .setContentText("Tu evaluación está lista. Toca para ver los resultados.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID_INTERVIEW_COMPLETED, notification)
    }

    /**
     * Muestra notificación cuando se genera un certificado
     */
    fun showCertificateReadyNotification(certificateName: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "profile")
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID_CERTIFICATE_READY,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_CERTIFICATES)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Certificado Generado 📜")
            .setContentText("Tu certificado '$certificateName' está listo para descargar.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID_CERTIFICATE_READY, notification)
    }

    /**
     * Muestra recordatorio para practicar entrevistas
     */
    fun showInterviewReminderNotification() {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("navigate_to", "home")
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID_REMINDER,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_REMINDERS)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("¿Listo para practicar? 💼")
            .setContentText("Realiza una entrevista hoy y mejora tus habilidades.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID_REMINDER, notification)
    }

    /**
     * Muestra notificación de progreso (para operaciones largas)
     */
    fun showProgressNotification(title: String, message: String): Int {
        val notification = NotificationCompat.Builder(context, CHANNEL_INTERVIEW)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setProgress(0, 0, true) // Indeterminate progress
            .setOngoing(true) // No se puede descartar
            .build()

        val notificationId = System.currentTimeMillis().toInt()
        notificationManager.notify(notificationId, notification)
        return notificationId
    }

    /**
     * Cancela una notificación específica
     */
    fun cancelNotification(notificationId: Int) {
        notificationManager.cancel(notificationId)
    }

    companion object {
        // Canales de notificación
        private const val CHANNEL_INTERVIEW = "interview_channel"
        private const val CHANNEL_CERTIFICATES = "certificate_channel"
        private const val CHANNEL_REMINDERS = "reminder_channel"

        // IDs de notificaciones
        private const val NOTIFICATION_ID_INTERVIEW_COMPLETED = 1001
        private const val NOTIFICATION_ID_CERTIFICATE_READY = 1002
        private const val NOTIFICATION_ID_REMINDER = 1003
    }
}


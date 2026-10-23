package com.luc.alertateste

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import androidx.core.app.NotificationCompat

object Notificacoes {
    private const val CHANNEL_ID = "alerta_extremo_v3"

    fun mostrar(context: Context, titulo: String, texto: String) {
        val nm = context.getSystemService(NotificationManager::class.java)

        val soundUri = Uri.parse("android.resource://${context.packageName}/${R.raw.amber_alert}")

        val canal = NotificationChannel(
            CHANNEL_ID,
            "Alertas extremos",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            enableVibration(true)
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            setSound(
                soundUri,
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
        }
        nm.createNotificationChannel(canal)

        val abrir = PendingIntent.getActivity(
            context, 0,
            Intent(context, AlertaActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra("titulo", titulo)
                .putExtra("texto", texto),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notif = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setContentTitle(titulo)
            .setContentText(texto)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setFullScreenIntent(abrir, true)
            .setAutoCancel(true)
            .build()

        nm.notify(999, notif)
    }
}

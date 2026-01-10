package com.example.splendolite

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title") ?: "Peringatan Tugas"
        val desc = intent.getStringExtra("desc") ?: "Jangan lupa kerjakan tugasmu!"

        // --- TAMBAHKAN KOD BUNYI DI SINI ---
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        if (alarmSound == null) {
            // Jika bunyi alarm tak ada, pakai bunyi notifikasi biasa
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }
        // ------------------------------------

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "tugas_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Notifikasi Tugas", NotificationManager.IMPORTANCE_HIGH)
            // Penting: Setel channel agar ada bunyi
            channel.enableVibration(true)
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(title)
            .setContentText(desc)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setSound(alarmSound) // Memasukkan bunyi ke notifikasi
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000)) // Bergetar
            .setAutoCancel(true)

        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }
}
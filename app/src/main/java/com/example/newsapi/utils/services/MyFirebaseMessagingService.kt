package com.example.newsapi.utils.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.newsapi.MainActivity
import com.example.newsapi.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService:FirebaseMessagingService() {
    // generate the notification
    // attach the notification created with the custom layout
    // show the notification
    override fun onMessageReceived(message: RemoteMessage) {
        if(message.notification != null){
            generateNotification(message.notification!!.title!!, message.notification!!.body!!)
        }

    }

    fun generateNotification(title:String, message:String){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this,0,intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        //channel id , channel name
        var builder :NotificationCompat.Builder = NotificationCompat.Builder(applicationContext,
            channelId
        )
            .setSmallIcon(R.drawable.icon_news_app)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(VIBRATION_TIME, RELAX_TIME, VIBRATION_TIME, RELAX_TIME))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)


        builder = builder.setContent(getRemoteView(title,message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channelId, channelName ,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)

            notificationManager.notify(0,builder.build())
        }


    }

    private fun getRemoteView(title: String, message: String): RemoteViews {
        val remoteViews = RemoteViews("com.example.newsapi", R.layout.notification)
        remoteViews.setTextViewText(R.id.title,title)
        remoteViews.setTextViewText(R.id.description,message)
        remoteViews.setImageViewResource(R.id.app_logo, R.drawable.icon_news_app)

        return remoteViews
    }

    companion object{
        private const val channelId = "notification_channel"
        private const val channelName = "com.example.newsapi"
        private const val VIBRATION_TIME = 1000L
        private const val RELAX_TIME = 1000L
    }

}
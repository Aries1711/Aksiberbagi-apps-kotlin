package com.aksiberbagi.donatur.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.aksiberbagi.donatur.R
import com.aksiberbagi.donatur.view.DashboardActivity
import com.aksiberbagi.donatur.view.DonasiDetailActivity
import com.aksiberbagi.donatur.view.DonasiRutinActivity
import com.aksiberbagi.donatur.view.SplashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


const val channelId = "notification_channel"
const val channelName = "com.aksiberbagi.donatur.services"

class MyFirebaseMessagingServices : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if(remoteMessage.notification != null){
            generateNotification(
                remoteMessage.notification!!.title!!,
                remoteMessage.notification!!.body!!,
                remoteMessage!!.data["keyFirebase"].toString(),
                remoteMessage!!.data["data"].toString(),
            )
        }
    }

    fun getRemoteView(title: String, subtitle: String) : RemoteViews{
        val remoteView = RemoteViews("com.aksiberbagi.donatur", R.layout.notification_panel)

        remoteView.setTextViewText(R.id.titleNotification, title)
        remoteView.setTextViewText(R.id.subtitleNotification, subtitle)
        remoteView.setImageViewResource(R.id.app_notif_logo, R.drawable.aksiberbagi_blue)

        return remoteView
    }

    fun generateNotification(title: String, subtitle: String , key: String, data: String){

        var intent : Intent

        if(key == "donasi rutin true"){
            intent = Intent(this,DonasiRutinActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("keyFirebase", "donasi rutin true" )
            intent.putExtras(mBundle)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }else if(key == "tagihan"){
            intent = Intent(this, DonasiDetailActivity::class.java)
            val sharedPreference: Preferences = Preferences(this)
            val mBundle = Bundle()
            mBundle.putString("keyFirebase", "tagihan" )
            sharedPreference.save("dataTagihan", data)
            intent.putExtras(mBundle)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }else{
            intent = Intent(this,DashboardActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        val pendingIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_ONE_SHOT)

        //channel_id, channel_name
        var builder : NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.aksiberbagi_blue)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(0))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title,subtitle))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0, builder.build())

    }
}
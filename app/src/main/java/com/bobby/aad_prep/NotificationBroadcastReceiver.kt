package com.bobby.aad_prep

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat

class NotificationBroadcastReceiver : BroadcastReceiver(){

    private lateinit var mNotificationManager: NotificationManager
    override fun onReceive(context: Context?, intent: Intent?) {
        setUpNotificationChannel(context)
        updateNotification(context!!)
        Toast.makeText(context, "Notification action worked", Toast.LENGTH_LONG).show()
    }

    private fun setUpNotificationChannel(context: Context?){
        mNotificationManager = context!!.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){

            val notificationChannel = NotificationChannel(NotificationActivity.PRIMARY_CHANNEL_ID,"AAD Notifications", NotificationManager.IMPORTANCE_HIGH).also {
                it.enableLights(true)
                it.lightColor = Color.MAGENTA
                it.enableVibration(true)
                it.description = "Cheeeeiiiii!!! Notification from AAD Prep"
            }

            mNotificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun getNotificationBuilder(context: Context): NotificationCompat.Builder{
        /*
        * Create intent and pending intent that is triggered when the notification is clicked
        * Pending Intent requires the NOTIFICATION_ID
        * */
        val intent = Intent(context, NotificationActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context,
            NotificationActivity.NOTIFICATION_ID,intent, PendingIntent.FLAG_UPDATE_CURRENT)

        /*
        * Build notification with Pending intent and other defaults
        * */
        return NotificationCompat.Builder(context, NotificationActivity.PRIMARY_CHANNEL_ID)
            .setContentTitle("AAD Gengway!!!")
            .setContentText("Omo, cant wait to get this certification")
            .setSmallIcon(R.drawable.ic_notification) //Image Assets> Notifications icons

            //Setting pending intent
            .setContentIntent(pendingIntent) //Attach pending intent to notification
            .setAutoCancel(true)

            //For backward compatibility, Android 7.1 and lower
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

            //Adding action that triggers broadcast receiver
            .addAction(
                R.drawable.ic_action, //Image Assets> Action bar and tab icons
                "Show toast",
                PendingIntent.getBroadcast(
                    context,
                    NotificationActivity.NOTIFICATION_ID,
                    Intent(NotificationActivity.ACTION_UPDATE_NOTIFICATION),
                    PendingIntent.FLAG_ONE_SHOT
                )
            )
    }

    private fun updateNotification(context: Context) {
        val image = BitmapFactory.decodeResource(context.resources, R.drawable.mascot_1)
        val notificationBuilder = getNotificationBuilder(context)
        notificationBuilder.setStyle(NotificationCompat.BigPictureStyle()
            .bigPicture(image)
            .setBigContentTitle("Notification Updated!"))

        mNotificationManager.notify(NotificationActivity.NOTIFICATION_ID,notificationBuilder.build())
    }

}
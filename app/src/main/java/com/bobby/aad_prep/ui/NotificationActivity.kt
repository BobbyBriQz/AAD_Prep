package com.bobby.aad_prep.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import com.bobby.aad_prep.R
import com.bobby.aad_prep.databinding.ActivityNotificationBinding
import com.google.android.material.snackbar.Snackbar

class NotificationActivity : AppCompatActivity() {

    /*
    * THERE ARE 4 MAJOR STEPS TO SET UP A NOTIFICATION
    * 1. Get Notification manager
    * 2. Create notification channel
    * 3. Build notification with notification builder
    * 4. Call notify() on the Notification manager, passing Notification ID and the built Notification
    * */

    private lateinit var mBinding: ActivityNotificationBinding

    private lateinit var mNotificationManager: NotificationManager
    private val mNotificationReceiver = NotificationBroadcastReceiver()
    companion object{
        const val PRIMARY_CHANNEL_ID = "primary_notification_channel"
        const val NOTIFICATION_ID = 0
        const val ACTION_UPDATE_NOTIFICATION = "com.bobby.aad_prep.ACTION_UPDATE_NOTIFICATION"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Setting Up View Binding
        mBinding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        initUi()
        createNotificationChannel()
        registerReceiver(mNotificationReceiver, IntentFilter(ACTION_UPDATE_NOTIFICATION))
    }

    private fun initUi() {
        setNotificationButtonState(isShown = true, isUpdated = false, isCancelled = false)
        mBinding.snackbarBtn.setOnClickListener{
            Snackbar.make(it, "SnackBar button works", Snackbar.LENGTH_INDEFINITE )
                .setAction("Yaaayyy!!!", {})
                .show()
        }

        mBinding.showNotificationBtn.setOnClickListener {
            sendNotification()
            setNotificationButtonState(isShown = false, isUpdated = true, isCancelled = true)
        }

        mBinding.updateNotificationBtn.setOnClickListener {
            updateNotification()
            setNotificationButtonState(isShown = false, isUpdated = false, isCancelled = true)
        }

        mBinding.cancelNotificationBtn.setOnClickListener {
            cancelNotification()
            setNotificationButtonState(isShown = true, isUpdated = false, isCancelled = false)
        }

        mBinding.gotoGithubBtn.setOnClickListener {
            startActivity(Intent(this, GithubActivity::class.java))
        }
    }

    private fun createNotificationChannel() {
        //Initialize notification manager
        mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        /*
        * Create notification channel if device is Oreo and above
        * Notification channel requires the "Channel ID"
        * */
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(PRIMARY_CHANNEL_ID, "AAD Notifications", NotificationManager.IMPORTANCE_HIGH).also {
                it.enableLights(true)
                it.lightColor = Color.MAGENTA
                it.enableVibration(true)
                it.description = "Cheeeeiiiii!!! Notification from AAD Prep"
            }
            mNotificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun getAADNotificationBuilder(): NotificationCompat.Builder {
        /*
        * Create intent and pending intent that is triggered when the notification is clicked
        * Pending Intent requires the NOTIFICATION_ID
        * */
        val intent = Intent(this, NotificationActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID,intent, PendingIntent.FLAG_UPDATE_CURRENT)

        /*
        * Build notification with Pending intent and other defaults
        * */
        return NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
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
                "Update Notification",
                PendingIntent.getBroadcast(
                    this,
                    NOTIFICATION_ID,
                    Intent(ACTION_UPDATE_NOTIFICATION),
                    PendingIntent.FLAG_ONE_SHOT
                )
            )
    }

    private fun sendNotification() {
        val notificationBuilder = getAADNotificationBuilder()
        mNotificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun updateNotification() {
        val image = BitmapFactory.decodeResource(resources, R.drawable.mascot_1)
        val notificationBuilder = getAADNotificationBuilder()
        notificationBuilder.setStyle(NotificationCompat.BigPictureStyle()
            .bigPicture(image)
            .setBigContentTitle("Notification Updated!"))
        mNotificationManager.notify(NOTIFICATION_ID,notificationBuilder.build())

    }

    private fun cancelNotification() {
        mNotificationManager.cancel(NOTIFICATION_ID)
    }

    private fun setNotificationButtonState(isShown: Boolean, isUpdated:Boolean, isCancelled: Boolean){
        mBinding.showNotificationBtn.isEnabled = isShown
        mBinding.updateNotificationBtn.isEnabled = isUpdated
        mBinding.cancelNotificationBtn.isEnabled = isCancelled
    }

    override fun onDestroy() {
        unregisterReceiver(mNotificationReceiver)
        super.onDestroy()
    }

}



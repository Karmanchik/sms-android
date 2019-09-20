package com.alt.karman_pc.smsmaket.Activity

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.ActivityInfo
import android.support.constraint.ConstraintLayout
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.alt.karman_pc.smsmaket.R
import com.alt.karman_pc.smsmaket.helperFiles.*
import com.alt.karman_pc.smsmaket.helperFiles.startActivity
import com.github.florent37.runtimepermission.kotlin.askPermission
import io.realm.Realm
import io.realm.RealmConfiguration


class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_start)

            Realm.init(applicationContext)
            val config =
                RealmConfiguration
                    .Builder()
                    .name("tasky.realm")
                    .schemaVersion(0)
                    .build()
            Realm.setDefaultConfiguration(config)

            val setting = SettingApp(this)
            setLocale(this, setting.language.get())

            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )

            if (setting.nightMode.get())
                findViewById<ConstraintLayout>(R.id.field1).setBackgroundResource(R.color.nightTheme)

            if (!setting.showStartLogo.get()) {
                askSmsPermission()
                throw Exception("not show start activity")
            }

            val animLogo = AnimationUtils.loadAnimation(this, R.anim.main_logo)
            animLogo.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    askSmsPermission()
                }

                override fun onAnimationRepeat(animation: Animation) {}
            })
            findViewById<ImageView>(R.id.mainLogoView).startAnimation(animLogo)
        } catch (e: Exception) {
            Log.e(TAG, "Error StartActivity onCreate: ${e.message}")
        }
    }

    override fun onRestart() {
        super.onRestart()
        finish()
    }

    fun askSmsPermission() {
        askPermission(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.CAMERA
        ) {
            startActivity(MainActivity::class.java)
        }.onDeclined { e ->
            if (e.hasDenied()) {
                e.denied.forEach {
                    var title = ""
                    var text = ""
                    when (it) {
                        Manifest.permission.READ_CONTACTS -> {
                            title = getString(R.string.uses_contacts_title)
                            text = getString(R.string.uses_contacts_message)
                        }
                        else -> {
                            title = getString(R.string.uses_sms_title)
                            text = getString(R.string.uses_sms_message)
                        }
                    }

                    AlertDialog.Builder(this)
                        .setTitle(title)
                        .setMessage(text)
                        .setPositiveButton(R.string.yes) { _, _ -> e.askAgain() }
                        .setNegativeButton(R.string.no) { dialog, _ -> dialog.dismiss() }
                        .show()
                }
            }

            if (e.hasForeverDenied()) {
                e.foreverDenied.forEach {
                    var title = ""
                    var text = ""
                    if (it == Manifest.permission.READ_CONTACTS) {
                        title = getString(R.string.uses_contacts_title)
                        text = getString(R.string.uses_contacts_message)
                    } else {
                        title = getString(R.string.uses_sms_title)
                        text = getString(R.string.uses_sms_message)
                    }
                    AlertDialog.Builder(this)
                        .setTitle(title)
                        .setMessage(text)
                        .setPositiveButton(R.string.yes) { _, _ -> e.goToSettings() }
                        .setNegativeButton(R.string.no) { dialog, _ -> dialog.dismiss() }
                        .show()
                }
            }
        }
    }
}
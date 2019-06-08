package com.alt.karman_pc.smsmaket.Activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.ViewGroup
import com.vansuita.materialabout.builder.AboutBuilder
import com.alt.karman_pc.smsmaket.R
import com.alt.karman_pc.smsmaket.helperFiles.*


class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        try {
            var cover = R.mipmap.profile_cover
            if (SettingApp(this).nightMode.get()) {
                findViewById<ConstraintLayout>(R.id.mainInfoView).setBackgroundResource(R.drawable.space)
                cover = R.drawable.obl
            }

            val view = AboutBuilder.with(this)
                .setPhoto(R.drawable.me)
                .setCover(cover)
                .setName(getString(R.string.author_full_name))
                .setSubTitle(getString(R.string.about_title))
                .setBrief(getString(R.string.author_slogan))
                .setAppIcon(R.drawable.pic)
                .setAppName(R.string.app_name)
                .addEmailLink(getString(R.string.author_mail))
                .addWebsiteLink(getString(R.string.author_vk))
                .addFiveStarsAction()
                .setVersionNameAsAppSubTitle()
                .addShareAction(R.string.app_name)
                .setWrapScrollView(true)
                .setLinksAnimated(true)
                .setShowAsCard(true)
                .build()

            addContentView(
                view, ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error AboutActivity: ${e.message}")
        }
    }
}

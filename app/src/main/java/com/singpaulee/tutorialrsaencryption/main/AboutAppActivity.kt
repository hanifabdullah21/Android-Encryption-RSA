package com.singpaulee.tutorialrsaencryption.main

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.MenuItem
import android.view.View
import com.singpaulee.tutorialrsaencryption.R
import kotlinx.android.synthetic.main.activity_about_app.*
import android.content.Intent
import android.net.Uri
import android.R.attr.versionName
import android.R.attr.versionCode
import android.content.pm.PackageInfo
import com.singpaulee.tutorialrsaencryption.helper.SharedPrefManager
import kotlinx.android.synthetic.main.activity_first_screen.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.sdk25.coroutines.onClick

class AboutAppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_app)

        val pinfo = packageManager.getPackageInfo(packageName, 0)
        val versionName = pinfo.versionName
        aaa_tv_version_name.text = versionName

        setSpannableEmail()
        setSpannableInstagram()
        setSpannableTwitter()

        logout.onClick {
            val sharedPrefManager = SharedPrefManager(this@AboutAppActivity)
            sharedPrefManager.savePrefUsername("")
            sharedPrefManager.savePrefPassword("")
            sharedPrefManager.savePrefBoolean(true)
            startActivity(intentFor<FirstScreenActivity>())
            finishAffinity()
        }
    }

    fun setSpannableEmail(){
        val text = "Email: hanifabdullah7@gmail.com"
        val spanText = SpannableString(text)
        val spanClickedListener = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intentGmail = Intent(Intent.ACTION_SEND)
                intentGmail.putExtra(Intent.EXTRA_EMAIL, "hanifabdullah7@gmail.com")
                intentGmail.type = "plain/text"
                startActivity(Intent.createChooser(intentGmail, "Kirim email dengan:"));
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }

        }
        spanText.setSpan(spanClickedListener, 7, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        aaa_tv_email.text = spanText
        aaa_tv_email.movementMethod = LinkMovementMethod.getInstance()
        aaa_tv_email.highlightColor = Color.TRANSPARENT
    }

    fun setSpannableInstagram(){
        val text = "Instagram: instagram.com/hanifabdullah21"
        val spanText = SpannableString(text)
        val spanClickedListener = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val browserIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://www.instagram.com/hanifabdullah21")
                )
                startActivity(Intent.createChooser(browserIntent,"Buka aplikasi dengan:"))
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }

        }
        spanText.setSpan(spanClickedListener, 11, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        aaa_tv_instagram.text = spanText
        aaa_tv_instagram.movementMethod = LinkMovementMethod.getInstance()
        aaa_tv_instagram.highlightColor = Color.TRANSPARENT
    }

    fun setSpannableTwitter(){
        val text = "Twitter: twitter.com/"
        val spanText = SpannableString(text)
        val spanClickedListener = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val browserIntent = Intent(Intent.ACTION_VIEW)
                browserIntent.data = Uri.parse("http://www.twitter.com/")
                startActivity(Intent.createChooser(browserIntent,"Buka aplikasi dengan:"))
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = true
            }

        }
        spanText.setSpan(spanClickedListener, 9, text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        aaa_tv_twitter.text = spanText
        aaa_tv_twitter.movementMethod = LinkMovementMethod.getInstance()
        aaa_tv_twitter.highlightColor = Color.TRANSPARENT
    }
}

package com.singpaulee.tutorialrsaencryption.main

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.singpaulee.tutorialrsaencryption.R
import com.singpaulee.tutorialrsaencryption.helper.SharedPrefManager
import kotlinx.android.synthetic.main.activity_first_screen.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class FirstScreenActivity : AppCompatActivity() {

    lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_screen)

        //TODO Initialize sharedprefmanager
        sharedPrefManager = SharedPrefManager(this)

        //TODO Check is first install
        if (!sharedPrefManager.getFirstLogin()) {
            moveToDashboard()
        }

        fsa_btn_start.onClick {
            if (!validation()) {
                return@onClick
            }
            sharedPrefManager.savePrefUsername(fsa_edt_username.text.toString())
            sharedPrefManager.savePrefPassword(fsa_edt_password.text.toString())
            sharedPrefManager.savePrefBoolean(false)

            moveToDashboard()
        }

    }

    /** Fungsi ini berisi kode untuk pindah ke aktivitas dashboard
     *
     * */
    private fun moveToDashboard() {
        finish()
        var intent: Intent = Intent(this@FirstScreenActivity, DashboardActivity::class.java)
        startActivity(intent)
    }

    /** Fungsi ini digunakan untuk memvalidasi field / edittext
     * apakah sudah sesuai dengan yang diinginkan atau belum
     * akan mengembalikan nilai true jika sudah sesuai
     *
     * */
    private fun validation(): Boolean {
        if (fsa_edt_username.text.toString().isBlank()) {
            fsa_edt_username.requestFocus()
            fsa_edt_username.error = "Harus diisi"
            return false
        }
        if (fsa_edt_password.text.toString().isBlank()) {
            fsa_edt_password.requestFocus()
            fsa_edt_password.error = "Harus diisi"
            return false
        }
        return true
    }
}

package com.singpaulee.tutorialrsaencryption.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.singpaulee.tutorialrsaencryption.R
import com.singpaulee.tutorialrsaencryption.helper.SharedPrefManager
import kotlinx.android.synthetic.main.activity_edit_profil.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class EditProfilActivity : AppCompatActivity() {

    var sharedpref: SharedPrefManager? = null

    var username = ""
    var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profil)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        sharedpref = SharedPrefManager(this)

        epa_edt_username.setText(sharedpref?.getUsername().toString())

        epa_btn_save.onClick {
            if (!validasi()) {
                return@onClick
            }
            sharedpref?.savePrefUsername(epa_edt_username.text.toString())
            if (!epa_edt_password.text.toString().isBlank()) {
                sharedpref?.savePrefPassword(epa_edt_new_password.text.toString())
            }
            toast("Profil berhasil diperbarui")
        }
    }

    private fun validasi(): Boolean {
        if (epa_edt_username.text.toString().isBlank()) {
            epa_edt_username.error = "Tidak boleh kosong"
            epa_edt_username.requestFocus()
            return false
        } else if (!epa_edt_password.text.toString().isBlank()) {
            if (epa_edt_new_password.text.toString().isBlank()) {
                epa_edt_new_password.error = "Tidak boleh kosong"
                epa_edt_new_password.requestFocus()
                return false
            } else if (epa_edt_confirm_password.text.toString().isBlank()) {
                epa_edt_confirm_password.error = "Tidak boleh kosong"
                epa_edt_confirm_password.requestFocus()
                return false
            } else if (epa_edt_password.text.toString() != sharedpref?.getPassword()) {
                epa_edt_password.error = "Password Salah"
                epa_edt_password.requestFocus()
                return false
            } else if (epa_edt_new_password.text.toString() != epa_edt_confirm_password.text.toString()) {
                epa_edt_confirm_password.error = "Password tidak sesuai"
                epa_edt_confirm_password.requestFocus()
                return false
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            //Event when click back button on top left activity
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

package com.singpaulee.tutorialrsaencryption.main

import android.app.AlertDialog
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import com.singpaulee.tutorialrsaencryption.R
import com.singpaulee.tutorialrsaencryption.db.NoteContract
import com.singpaulee.tutorialrsaencryption.db.database
import com.singpaulee.tutorialrsaencryption.helper.SharedPrefManager
import com.singpaulee.tutorialrsaencryption.rumus.RSA
import kotlinx.android.synthetic.main.activity_detail_note.*
import kotlinx.android.synthetic.main.dialog_input_password.view.*
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.select
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast


class DetailNoteActivity : AppCompatActivity() {

    var isCipherShow: Boolean = true

    var noteContractModel: NoteContract? = null

    var RSA: RSA? = null
    var eRSA: Double? = null
    var dRSA: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_note)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //TODO Initialize class
        RSA = RSA()

        //TODO Create e and d value for RSA Algorythm
        eRSA = RSA?.eValue(RSA?.Qn!!)
        dRSA = RSA?.dValue(RSA?.Qn!!, eRSA!!)

        //TODO Catch passing data from previous activity
        noteContractModel = intent.getParcelableExtra("NOTE")

        //TODO Set Content
        setItemContent()

        dna_btn_decrypt.onClick {
            //TODO Open dialog input password
            openDialog("DECRYPT")
        }

        dna_btn_encrypt.onClick {
            lottie_animation.visibility = View.VISIBLE
            lottie_animation.playAnimation()
            Handler().postDelayed({
                setItemContent()
            }, 3000)
        }

        dna_btn_edit.onClick {
            openDialog("SUNTING")
        }

        dna_btn_delete.onClick {
            openDialog("DELETE")
        }

        dna_btn_copy.onClick {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = ClipData.newPlainText("", dna_tv_message.text.toString())
            clipboard.primaryClip = clip
            toast("Teks disalin ke papan clipboard")
        }
    }

    /** Fungsi ini digunakan untuk menghapus note dari database sqlite
     *
     * */
    private fun deleteNote() {
        database.use {
            delete(
                NoteContract.TABLE_NOTE, "(${NoteContract.ID}={id})",
                "id" to noteContractModel?.id!!.toLong()
            )
            finish()
        }
    }

    /** Fungsi ini digunakan untuk menampilkan custom dialog
     *  berupa field masukan password sebagaimana syarat ketika
     *  akan melakukan dekrip
     *
     * */
    private fun openDialog(status: String) {
        //Get password value from sharedpreference
        var sp = SharedPrefManager(this@DetailNoteActivity)
        var password = sp.getPassword()
        Log.d("DetailNote", "$password")

        var builder: AlertDialog.Builder = AlertDialog.Builder(this)
        var alertDialog: AlertDialog? = null

        var layoutInflater: LayoutInflater = LayoutInflater.from(this)
        var dialog: View = layoutInflater.inflate(R.layout.dialog_input_password, null)

        dialog.dip_btn_dekrip.text = status

        dialog.dip_btn_dekrip.onClick {
            if (dialog.dip_edt_password.text.isBlank()) {
                dialog.dip_edt_password.error = "Tidak boleh kosong"
                dialog.dip_edt_password.requestFocus()
                return@onClick
            } else if (dialog.dip_edt_password.text.toString() != password) {
                dialog.dip_edt_password.error = "Kata sandi salah"
                dialog.dip_edt_password.requestFocus()
                return@onClick
            } else if (dialog.dip_edt_password.text.toString() == password) {
                when (status) {
                    "DECRYPT" -> {
                        alertDialog?.dismiss()
                        //decryptTitle()
                        lottie_animation.visibility = View.VISIBLE
                        lottie_animation.playAnimation()
                        Handler().postDelayed({
                            decryptMessage()
                            switchButton()
                        }, 3000)
                    }
                    "SUNTING" -> {
                        alertDialog?.dismiss()
                        var i = Intent(this@DetailNoteActivity, UpdateNoteActivity::class.java)
                        i.putExtra("NOTES", noteContractModel)
                        startActivity(i)
                    }
                    "DELETE" -> {
                        deleteNote()
                    }
                }
            }
        }

        builder.setView(dialog)
        alertDialog = builder.create()
        alertDialog.setCancelable(true)
        alertDialog.show()
    }

    /** Fungsi ini digunakan untuk menukar tombol yang aktif
     *
     *
     * */
    private fun switchButton() {
        if (isCipherShow) {
            isCipherShow = false
            dna_btn_decrypt.visibility = View.GONE
            dna_btn_encrypt.visibility = View.VISIBLE
        } else {
            isCipherShow = true
            dna_btn_decrypt.visibility = View.VISIBLE
            dna_btn_encrypt.visibility = View.GONE
        }
    }

    /** Fungsi ini digunakan untuk mendeskripsi judul pesan
     *
     * */
    private fun decryptTitle() {
        var plainTitle = ""
        for (i in 0 until noteContractModel?.title!!.length) {
            var character = noteContractModel?.title.toString()[i]
            var plain = RSA?.decrypt(character, dRSA!!, RSA?.n!!)
            plainTitle += plain
        }
        dna_tv_title.text = plainTitle
    }

    /** Fungsi ini digunakan untuk mendeskripsi isi pesan
     *
     * */
    private fun decryptMessage() {
        var plainTeks = ""
        for (i in 0 until noteContractModel?.message!!.length) {
            var character = noteContractModel?.message.toString()[i]
            var plain = RSA?.decrypt(character, dRSA!!, RSA?.n!!)
            plainTeks += plain
        }
        lottie_animation.visibility = View.GONE
        dna_tv_message.text = plainTeks
    }

    /** Fungsi ini digunakan untuk mengatur teks konten yang akan ditampilkan
     *
     * */
    private fun setItemContent() {
        dna_tv_date.text = noteContractModel?.createdAt
        dna_tv_title.text = noteContractModel?.title
        dna_tv_message.text = noteContractModel?.message
        lottie_animation.visibility = View.GONE
    }

    private fun selectSqlite() {
        database.use {
            val result = select(NoteContract.TABLE_NOTE)
                .whereArgs("(${NoteContract.ID} = {id})", "id" to noteContractModel?.id!!)
            noteContractModel = result.parseOpt(classParser<NoteContract>())
            setItemContent()
        }
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

    override fun onRestart() {
        super.onRestart()
        switchButton()
        selectSqlite()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (!isCipherShow) {
            toast("Encrypted")
        }
    }
}

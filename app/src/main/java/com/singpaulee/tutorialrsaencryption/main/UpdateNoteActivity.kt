package com.singpaulee.tutorialrsaencryption.main

import android.database.sqlite.SQLiteException
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import com.singpaulee.tutorialrsaencryption.R
import com.singpaulee.tutorialrsaencryption.db.NoteContract
import com.singpaulee.tutorialrsaencryption.db.database
import com.singpaulee.tutorialrsaencryption.helper.ConverterDate
import com.singpaulee.tutorialrsaencryption.rumus.RSA
import kotlinx.android.synthetic.main.activity_update_note.*
import org.jetbrains.anko.db.update
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class UpdateNoteActivity : AppCompatActivity() {

    var noteContractModel: NoteContract? = null

    var RSA: RSA? = null
    var eRSA: Double? = null
    var dRSA: Double? = null

    var cipherTeks: String = ""
    var cipherTitle: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_note)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //TODO Initialize class
        RSA = RSA()

        //TODO Create e and d value for RSA Algorythm
        eRSA = RSA?.eValue(RSA?.Qn!!)
        dRSA = RSA?.dValue(RSA?.Qn!!, eRSA!!)

        //TODO Catch object from previous activity
        noteContractModel = intent.getParcelableExtra("NOTES")

        //TODO Set date last update
        una_tv_date.text = "Terakhir diubah pada ${noteContractModel?.updateAt}"

        //TODO Decrypt Title message and set to edittext
//        decryptTitle()
        una_edt_title.setText(noteContractModel?.title)

        //TODO Decrypt Message and set to edittext
        decryptMessage()

        una_btn_save.onClick {
            //            encryptTitle()
            cipherTitle = una_edt_title.text.toString()
            encryptBody()
            updateSqlite()
        }
    }

    /** Fungsi ini digunakan untuk mengupdate SQLite
     *
     * */
    private fun updateSqlite() {
        try {
            var currentDate = ConverterDate().getCurrentDateFull()

            database.use {
                update(
                    NoteContract.TABLE_NOTE,
                    NoteContract.TITLE_MESSAGE to cipherTitle,
                    NoteContract.VALUE_MESSAGE to cipherTeks,
                    NoteContract.UPDATE_AT to currentDate
                ).whereArgs("${NoteContract.ID} = {id}", "id" to noteContractModel?.id!!).exec()
            }
            finish()
        } catch (e: SQLiteException) {
            Log.e("UNA", "Error update cause $e")
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
        una_edt_title.setText(plainTitle)
        Log.d("UNA", "Decrypt Title : $plainTitle")
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
        una_edt_message.setText(plainTeks)
        Log.d("UNA", "Decrypt Message : $plainTeks")
    }

    /** Fungsi ini digunakan untuk
     * mengenkripsi isi teks dari note tersebut
     *
     * */
    private fun encryptBody() {
        var plainteks = una_edt_message.text.toString()

        cipherTeks = ""
        for (i in 0 until plainteks.length) {
            var character = plainteks[i]
            var cipher = RSA?.encrypt(character, eRSA!!, RSA?.n!!)
            cipherTeks += cipher
        }
        Log.d("UNA", "Encrypt Message : $cipherTeks")
    }

    /** Fungsi ini digunakan untuk
     * mengenkripsi judul dari note tersebut
     *
     * */
    private fun encryptTitle() {
        var plainTitle = una_edt_title.text.toString()

        cipherTitle = ""
        for (i in 0 until plainTitle.length) {
            var character = plainTitle[i]
            var cipher = RSA?.encrypt(character, eRSA!!, RSA?.n!!)
            cipherTitle += cipher
        }
        Log.d("UNA", "Encrypt Title : $cipherTitle")
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

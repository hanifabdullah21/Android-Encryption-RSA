package com.singpaulee.tutorialrsaencryption.main

import android.annotation.SuppressLint
import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import com.singpaulee.tutorialrsaencryption.R
import com.singpaulee.tutorialrsaencryption.db.NoteContract
import com.singpaulee.tutorialrsaencryption.db.database
import com.singpaulee.tutorialrsaencryption.helper.ConverterDate
import com.singpaulee.tutorialrsaencryption.rumus.RSA
import kotlinx.android.synthetic.main.activity_add_note.*
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class AddNoteActivity : AppCompatActivity() {

    @SuppressLint("SimpleDateFormat")
//    var locale = Locale("in", "ID")
//    var dateFormat: SimpleDateFormat? = SimpleDateFormat("EEE , dd MMM yyyy , hh:mm aaa", locale)
//    var date: Date? = null
    var currentDate: String? = null

    var cipherTitle: String? = null
    var cipherTeks: String? = null

    var RSA: RSA? = null
    var eRSA: Double? = null
    var dRSA: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //TODO Initialize RSA class
        RSA = RSA()

        //TODO Create e and d value for RSA Algorythm
        eRSA = RSA?.eValue(RSA?.Qn!!)
        dRSA = RSA?.dValue(RSA?.Qn!!, eRSA!!)

        //TODO GET CURRENT DATE and Set text of date
        setTextOfDate()

        ana_btn_save.onClick {
            //            encryptTitle()
            cipherTitle = ana_edt_title.text.toString()
            encryptBody()
            saveToSqlite()
        }

    }

    /** Fungsi ini digunakan untuk mengatur tanggal dan waktu saat ini
     *
     * */
    private fun setTextOfDate() {
//        date = Calendar.getInstance().time

        var listDate = ConverterDate().getCurrentDatePart()

        ana_tv_date_day.text = listDate[0]
        ana_tv_date_number.text = listDate[1]
        ana_tv_date_month.text = listDate[2] + " " + listDate[3]
        ana_tv_date_time.text = listDate[4]
        currentDate = ConverterDate().getCurrentDateFull()
        //      dateFormat.parse(currentDate)   convert string to date
    }

    /** Fungsi ini digunakan untuk menyimpan note ke SQLite
     *
     * */
    private fun saveToSqlite() {
        try {
            database.use {
                insert(
                    NoteContract.TABLE_NOTE,
                    NoteContract.TITLE_MESSAGE to cipherTitle,
                    NoteContract.VALUE_MESSAGE to cipherTeks,
                    NoteContract.CREATED_AT to currentDate,
                    NoteContract.UPDATE_AT to currentDate
                )
                Log.d("SQLITE", "Suceess insert")
                setTextOfDate()
                ana_edt_title.text.clear()
                ana_edt_title.requestFocus()
                ana_edt_text.text.clear()
                toast("Note baru berhasil ditambahkan")
                finish()
            }
        } catch (e: SQLiteConstraintException) {
            Log.d("SQLITE", "Exception insert ${e.message.toString()}")
            toast("${e.message.toString()}")
        } catch (ex: Exception) {
            Log.d("SQLITE", "Exception insert ${ex.message.toString()}")
            toast("${ex.message.toString()}")
        }
    }

    /** Fungsi ini digunakan untuk
     * mengenkripsi isi teks dari note tersebut
     *
     * */
    private fun encryptBody() {
        var plainteks = ana_edt_text.text.toString()

        cipherTeks = ""
        for (i in 0 until plainteks.length) {
            var character = plainteks[i]
            var cipher = RSA?.encrypt(character, eRSA!!, RSA?.n!!)
            cipherTeks += cipher
        }
        Log.d("FinalEncrypt", "Cipher Teks : $cipherTeks")
    }

    /** Fungsi ini digunakan untuk
     * mengenkripsi judul dari note tersebut
     *
     * */
    private fun encryptTitle() {
        var plainTitle = ana_edt_title.text.toString()

        cipherTitle = ""
        for (i in 0 until plainTitle.length) {
            var character = plainTitle[i]
            var cipher = RSA?.encrypt(character, eRSA!!, RSA?.n!!)
            cipherTitle += cipher
        }
        Log.d("FinalEncrypt", "Cipher Title : $cipherTitle")
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

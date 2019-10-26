package com.singpaulee.tutorialrsaencryption

import android.database.sqlite.SQLiteConstraintException
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.singpaulee.tutorialrsaencryption.db.MyDatabaseHelper
import com.singpaulee.tutorialrsaencryption.db.NoteContract
import com.singpaulee.tutorialrsaencryption.db.database
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.db.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast


class MainActivity : AppCompatActivity() {

    var title: String? = null
    var value: String? = null
    var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_insert.onClick {
            title = edt_title.text.toString()
            value = edt_value.text.toString()
            password = edt_password.text.toString()

            addItem()
        }

        btn_select.onClick {
//            MyDatabaseHelper(this@MainActivity)
            database.use {
                val result = select(NoteContract.TABLE_NOTE)
                val note = result.parseList(classParser<NoteContract>())
                if (!note.isEmpty()) {
                    for (i in 0 until note.size) {
                        Log.d(
                            "SELECT",
                            "${note.get(i).id} ${note.get(i).title} ${note.get(i).message}"
                        )
                    }
                }
            }
        }

        btn_delete.onClick {
            val id = "1"
            try {
                database.use {
                    delete(
                        NoteContract.TABLE_NOTE, "(${NoteContract.ID} = {id})",
                        "id" to id
                    )
                }
                toast("Sukses")
            } catch (e: SQLiteConstraintException) {
                toast("Gagal")
            }
        }
    }

    fun addItem() {
        try {
            var contoh: String = "XXX"
            database.use {
                insert(
                    NoteContract.TABLE_NOTE,
                    NoteContract.TITLE_MESSAGE to title,
                    NoteContract.VALUE_MESSAGE to value,
                    NoteContract.CREATED_AT to "21-juni-1997",
                    NoteContract.UPDATE_AT to "21-juni-1997"
                )
            }
            toast("Berhasil")
        } catch (e: SQLiteConstraintException) {
            toast("Gagal")
        }
    }

}

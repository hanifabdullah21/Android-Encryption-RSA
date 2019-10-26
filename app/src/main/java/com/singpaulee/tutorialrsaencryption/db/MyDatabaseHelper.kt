package com.singpaulee.tutorialrsaencryption.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import org.jetbrains.anko.db.*

class MyDatabaseHelper private constructor(context: Context) :
    ManagedSQLiteOpenHelper(context, "RSAEncryption.db", null, 1) {

    init {
        instance = this
    }

    companion object {
        private var instance: MyDatabaseHelper? = null

        @Synchronized
        fun getInstance(context: Context): MyDatabaseHelper {
            if (instance == null) {
                instance = MyDatabaseHelper(context.applicationContext)
            }
            return instance as MyDatabaseHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.createTable(
            NoteContract.TABLE_NOTE, true,
            NoteContract.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            NoteContract.TITLE_MESSAGE to TEXT,
            NoteContract.VALUE_MESSAGE to TEXT,
            NoteContract.CREATED_AT to TEXT,
            NoteContract.UPDATE_AT to TEXT
        )
        Log.d("DATABASE", "database sukses dibuat")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.dropTable(NoteContract.TABLE_NOTE, true)
    }
}

// Access property for Context
val Context.database: MyDatabaseHelper
    get() = MyDatabaseHelper.getInstance(applicationContext)

val Context.sqlite: MyDatabaseHelper
    get() = MyDatabaseHelper.getInstance(applicationContext)

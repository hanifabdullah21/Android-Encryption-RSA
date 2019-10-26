package com.singpaulee.tutorialrsaencryption.helper

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager(context: Context) {

    lateinit var pref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    var context: Context = context

    //Shared Preference Mode
    val PRIVATE_MODE = 0

    private val PREF_NAME = "RSAALGORYTHM"

    val KEY_USERNAME = "MYUSERNAME"
    val KEY_PASSWORD = "MYPASSWORD"
    val KEY_FIRSTLOGIN = "ISFIRSTLOGIN"

    init {
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit()
    }

    fun savePrefBoolean(value: Boolean) {
        editor.putBoolean(KEY_FIRSTLOGIN, value)
        editor.commit()
    }

    fun savePrefUsername(value: String) {
        editor.putString(KEY_USERNAME, value)
        editor.commit()
    }

    fun savePrefPassword(value: String) {
        editor.putString(KEY_PASSWORD, value)
        editor.commit()
    }

    fun getUsername(): String {
        return pref.getString(KEY_USERNAME, null)
    }

    fun getPassword(): String? {
        return pref.getString(KEY_PASSWORD, null)
    }

    fun getFirstLogin(): Boolean {
        return pref.getBoolean(KEY_FIRSTLOGIN, true)
    }
}
package com.singpaulee.tutorialrsaencryption.db

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NoteContract(
    val id: Long?,
    val title: String?,
    val message: String?,
    val createdAt: String?,
    val updateAt: String?
) : Parcelable {
    companion object {
        const val TABLE_NOTE: String = "TABLE_NOTE"
        const val ID: String = "ID_"
        const val TITLE_MESSAGE: String = "TITLE_MESSAGE"
        const val VALUE_MESSAGE: String = "VALUE_MESSAGE"
        const val CREATED_AT: String = "CREATED_AT"
        const val UPDATE_AT: String = "UPDATE_AT"
    }
}
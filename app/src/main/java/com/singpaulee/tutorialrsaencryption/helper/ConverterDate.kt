package com.singpaulee.tutorialrsaencryption.helper

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ConverterDate() {

    var locale = Locale("in", "ID")
    var dateFormat: SimpleDateFormat? = SimpleDateFormat("EEE , dd MMM yyyy , hh:mm aaa", locale)

    /** Fungsi ini untuk mendapatkan waktu saat ini dengan lengkap
     *
     * */
    fun getCurrentDateFull(): String {
        var date = Calendar.getInstance().time
        var currentDate = dateFormat?.format(date)
        return currentDate.toString()
    }

    /** Fungsi ini untuk mendapatkan waktu saat ini yang sudah dipecah dengan urutan
     * EEEE adalah hari saat ini , ct : Sabtu
     * dd adalah tanggal saat ini , ct : 21
     * MMM adalah bulan saat ini , ct : Jan
     * yyyy adalah tahun saat ini , ct 2019
     * hh:mm aaa adalah waktu saat ini , ct : 09:06 AM
     *
     * */
    fun getCurrentDatePart(): ArrayList<String> {
        var listDate: ArrayList<String> = ArrayList()
        var date = Calendar.getInstance().time

        var fullday = SimpleDateFormat("EEEE", locale).format(date)
        listDate.add(fullday)

        var dateNumber = SimpleDateFormat("dd", locale).format(date)
        listDate.add(dateNumber)

        var halfMonth = SimpleDateFormat("MMM", locale).format(date)
        listDate.add(halfMonth)

        var year = SimpleDateFormat("yyyy", locale).format(date)
        listDate.add(year)

        var time = SimpleDateFormat("hh:mm aaa", locale).format(date)
        listDate.add(time)

        return listDate
    }

    /** Fungsi ini digunakan untuk mem-parse tanggal dari database
     * kemudian mengembalikannya dalam bentuk bagian2
     *
     * EEEE adalah hari saat ini , ct : Sabtu
     * dd adalah tanggal saat ini , ct : 21
     * MMM adalah bulan saat ini , ct : Jan
     * yyyy adalah tahun saat ini , ct 2019
     * hh:mm aaa adalah waktu saat ini , ct : 09:06 AM
     *
     * */
    fun parseDatePart(date: String?): ArrayList<String> {
        var listDate: ArrayList<String> = ArrayList()
        var dateParse = dateFormat?.parse(date)

        var fullday = SimpleDateFormat("EEEE", locale).format(dateParse)
        listDate.add(fullday)

        var dateNumber = SimpleDateFormat("dd", locale).format(dateParse)
        listDate.add(dateNumber)

        var halfMonth = SimpleDateFormat("MMM", locale).format(dateParse)
        listDate.add(halfMonth)

        var year = SimpleDateFormat("yyyy", locale).format(dateParse)
        listDate.add(year)

        var time = SimpleDateFormat("hh:mm aaa", locale).format(dateParse)
        listDate.add(time)

        return listDate
    }
}
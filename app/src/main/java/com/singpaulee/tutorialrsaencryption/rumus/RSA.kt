package com.singpaulee.tutorialrsaencryption.rumus

import android.util.Log
import java.math.BigInteger

class RSA {
    val P = 61
    val Q = 53
    val n = P * Q
    val Qn = (P - 1) * (Q - 1)


    /** Fungsi ini untuk mencari nilai e
     * dimana e didapatkan dengan rumus e=Qn/k
     * e akan valid apabila bernilai desimal
     *
     * @Qn adalah parameter untuk menghitung nilai e
     *
     * */
    fun eValue(QnParameter: Int): Double {
        var k = 0.0      //nilai k yang valid
        var e: Double    //nilai e

        //TODO Lakukan looping untuk mendapatkan nilai e
        loop@ for (i in 1..10000000) {
            k = i.toDouble()

            //TODO Lakukan perhitungan dengan rumus e=Qn/k
            e = QnParameter / i.toDouble()
            Log.d("e-Process", "e ($e) = Qn ($QnParameter) / k ($k)")

            //TODO Jika nilai e merupakan bilangan desimal, hentikan perulangan
            Log.d("e-Process", "e ($e) % 1 = " + (e % 1))
            if (e % 1 != 0.0 && e != 0.0) {

                //TODO Hentikan looping, karena nilai e sudah bilangan desimal
                break@loop
            }
        }

        //TODO Kembalikan nilai k
        return k
    }

    /** Fungsi ini untuk mencari nilai d
     * d didapatkan dengan cara k.Qn+1/e
     * d akan valid jika hasilnya adalah bilangan bulat
     *
     * @QnParameter nilai Qn untuk mencari d
     * @eParameter nilai e untuk mencari d
     *
     * */
    fun dValue(QnParameter: Int, eParameter: Double): Double {
        var d = 0.0

        //TODO Lakukan looping untuk mendapatkan nilai d
        loop@ for (k in 1..1000000) {

            //TODO Lakukan perhitungan d = k*Qn+1 / e
            d = (k * QnParameter + 1) / eParameter
            Log.d("D-Process", "d ($d) = k ($k) * Qn ($QnParameter) + 1 / e ($eParameter)")

            //TODO Cek nilai d, jika bernilai bilangan bulat, hentikan looping
            if (d % 1 == 0.0) {

                //TODO Hentikan Looping
                Log.d("D-Process", "Didapatkan d= $d , dari k= $k")
                break@loop
            }
        }
        return d
    }

    /** This function is used to encrypt text per letter using RSA Algorythm
     * and will return in String data type
     *
     * @plain is character variable that will be encrypt
     * @e is ...
     * @n is ...
     *
     * */
    fun encrypt(plain: Char, e: Double, n: Int): String {
        var startTime = System.nanoTime()
        Log.d("TIME", "start " + startTime.toString())

        //TODO Konversi character (huruf) menjadi bilangan ASCII
        val numberAscii = plain.toInt()
        Log.d("Encrypt", "Konversi Char ($plain) ke ASCII ($numberAscii)")

        //TODO Menghitung ASCII plainteks pangkat e
        var plainPangkatE = Math.pow(numberAscii.toDouble(), e)
        Log.d("Encrypt", "Menghitung hasil ASCII ($numberAscii) pangkat e ($e) = $plainPangkatE")

        //TODO Lakukan enkripsi dengan rumus p^e mod n, menghasilkan cipherteks dalam bentuk bilangan ASCII
        var cipherASCII = plainPangkatE % n
        Log.d("Encrypt", "Menghitung hasil enkripsi dari ($plainPangkatE) mod ($n) = $cipherASCII")

        //TODO Konversi cipher dari bilangan ASCII menjadi sebuah karakter
        var cipher = cipherASCII.toChar()

        var endTime = System.nanoTime()
        Log.d("TIME", "finish " + endTime.toString())
        var duration = endTime - startTime
        Log.d("TIME", "duration " + duration.toString())

        return "$cipher"
    }

    /** This function is used to decrypt text per letter using RSA Algorythm
     * and will return in String data type
     *
     * @cipher is character variable that will be decrypt
     * @d is ...
     * @n is ...
     *
     * */
    fun decrypt(cipher: Char, d: Double, n: Int): String {
        var startTime = System.nanoTime()
        Log.d("TIME", "start " + startTime.toString())

        //TODO Konversi character menjadi bilangan ASCII
        var numberAscii = cipher.toInt()
        Log.d("Decrypt", "Konversi Char ($cipher) ke ASCII ($numberAscii)")

        //TODO Menghitung ASCII cipher pangkat d
        var numberAsciiBigInt = numberAscii.toBigInteger()
        var cipherPangkatD: BigInteger = numberAsciiBigInt.pow(d.toInt())
        Log.d("Decrypt", "Menghitung hasil ASCII ($numberAscii) pangkat d ($d) = $cipherPangkatD")

        //TODO Melakukan dekrip dengan rumus c^d mod n
        var rumusDecrypt = cipherPangkatD % n.toBigInteger()
        Log.d("Decrypt", "Menghitung hasil dekrip dari () mod ($n) = $rumusDecrypt")

        //TODO Konversi hasil dekrip dari ASCII menjadi karakter
        var asciiToChar = "$rumusDecrypt".toInt().toChar()
        Log.d("Decrypt", "Hasil Dekripsi character $cipher adalah $asciiToChar")

        var endTime = System.nanoTime()
        Log.d("TIME", "finish " + endTime.toString())
        var duration = endTime - startTime
        Log.d("TIME", "duration " + duration.toString())

        return "$asciiToChar"
    }
}
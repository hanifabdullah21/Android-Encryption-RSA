package com.singpaulee.tutorialrsaencryption

import android.R.attr.publicKey
import java.security.*
import javax.crypto.BadPaddingException
import javax.crypto.Cipher
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException


class RSAAlgorythm {

    var kpg: KeyPairGenerator? = null
    var kp: KeyPair? = null
    var publicKey: PublicKey? = null
    var privateKey: PrivateKey? = null
    var encryptedBytes: ByteArray? = null
    var decryptedBytes: ByteArray? = null
    var cipher: Cipher? = null
    var cipher1: Cipher? = null
    var encrypted: String? = null
    var decrypted: String? = null

    @Throws(
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    fun RSAEncrypt(plain: String): ByteArray? {
        kpg = KeyPairGenerator.getInstance("RSA")
        kpg?.initialize(2048)
        kp = kpg?.genKeyPair()
        publicKey = kp?.public
        privateKey = kp?.private

        cipher = Cipher.getInstance("RSA")
        cipher?.init(Cipher.ENCRYPT_MODE, publicKey)
        encryptedBytes = cipher?.doFinal(plain.toByteArray())
//        println("EEncrypted?????" + String(org.apache.commons.codec.binary.Hex.encodeHex(encryptedBytes)))
        return encryptedBytes
    }

    @Throws(
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class,
        IllegalBlockSizeException::class,
        BadPaddingException::class
    )
    fun RSADecrypt(encryptedBytes: ByteArray): String {

        cipher1 = Cipher.getInstance("RSA")
        cipher1?.init(Cipher.DECRYPT_MODE, privateKey)
        decryptedBytes = cipher1?.doFinal(encryptedBytes)
        decrypted = decryptedBytes?.let { String(decryptedBytes!!) }
        System.out.println("DDecrypted?????$decrypted")
        return decrypted.toString()
    }
}
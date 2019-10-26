package com.singpaulee.tutorialrsaencryption.fragment


import android.app.AlertDialog
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.singpaulee.tutorialrsaencryption.R
import com.singpaulee.tutorialrsaencryption.db.NoteContract
import com.singpaulee.tutorialrsaencryption.db.database
import com.singpaulee.tutorialrsaencryption.helper.SharedPrefManager
import com.singpaulee.tutorialrsaencryption.rumus.RSA
import kotlinx.android.synthetic.main.activity_detail_note.*
import kotlinx.android.synthetic.main.dialog_input_password.view.*
import kotlinx.android.synthetic.main.fragment_decrypt.*
import kotlinx.android.synthetic.main.fragment_decrypt.view.*
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.sdk25.coroutines.onClick

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class DecryptFragment : Fragment() {

    var RSA: RSA? = null
    var eRSA: Double? = null
    var dRSA: Double? = null

    lateinit var parentView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        parentView = inflater.inflate(R.layout.fragment_decrypt, container, false)

        //TODO Initialize class
        RSA = RSA()

        //TODO Create e and d value for RSA Algorythm
        eRSA = RSA?.eValue(RSA?.Qn!!)
        dRSA = RSA?.dValue(RSA?.Qn!!, eRSA!!)

        parentView.df_btn_decrypt.onClick {
            if (parentView.df_edt_teks.text.toString().isNotBlank() || parentView.df_edt_teks.text.toString().isNotEmpty()) {
                openDialog()
            }
        }

        return parentView
    }

    private fun openDialog() {
        //Get password value from sharedpreference
        var sp = SharedPrefManager(activity!!.applicationContext)
        var password = sp.getPassword()
        Log.d("DetailNote", "$password")

        var builder: AlertDialog.Builder = AlertDialog.Builder(context)
        var alertDialog: AlertDialog? = null

        var layoutInflater: LayoutInflater = LayoutInflater.from(context)
        var dialog: View = layoutInflater.inflate(R.layout.dialog_input_password, null)

        dialog.dip_btn_dekrip.text = "DECRYPT"

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
                alertDialog?.dismiss()
                decrypt()
            }
        }

        activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        builder.setView(dialog)
        alertDialog = builder.create()
        alertDialog.setCancelable(true)
        alertDialog.show()
    }

    private fun decrypt() {
        var plainTeks = ""
        for (i in 0 until parentView.df_edt_teks.text.toString().length) {
            var character = parentView.df_edt_teks.text.toString()[i]
            var plain = RSA?.decrypt(character, dRSA!!, RSA?.n!!)
            plainTeks += plain
        }

        parentView.lottie_animation.visibility = View.VISIBLE
        parentView.lottie_animation.playAnimation()
        Handler().postDelayed({
            parentView.df_tv_result_dekrip.text = plainTeks
            parentView.lottie_animation.visibility = View.GONE
        }, 3000)
    }


}

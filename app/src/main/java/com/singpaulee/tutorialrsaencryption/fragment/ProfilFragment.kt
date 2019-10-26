package com.singpaulee.tutorialrsaencryption.fragment


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.singpaulee.tutorialrsaencryption.R
import com.singpaulee.tutorialrsaencryption.helper.SharedPrefManager
import com.singpaulee.tutorialrsaencryption.main.AboutAppActivity
import com.singpaulee.tutorialrsaencryption.main.EditProfilActivity
import kotlinx.android.synthetic.main.fragment_profil.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ProfilFragment : Fragment() {

    lateinit var contentView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        contentView = inflater.inflate(R.layout.fragment_profil, container, false)

        initProfil()

        contentView.pf_tv_tentang_aplikasi.onClick {
            startActivity(Intent(activity, AboutAppActivity::class.java))
        }

        contentView.pf_tv_edit_profil.onClick {
            startActivity(Intent(activity, EditProfilActivity::class.java))
        }

        return contentView
    }

    private fun initProfil() {
        var sp = SharedPrefManager(activity?.applicationContext!!)
        contentView.pf_tv_username.text = sp.getUsername()
    }


}

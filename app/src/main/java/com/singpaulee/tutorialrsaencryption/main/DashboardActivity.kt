package com.singpaulee.tutorialrsaencryption.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.ListFragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.singpaulee.tutorialrsaencryption.R
import com.singpaulee.tutorialrsaencryption.fragment.DecryptFragment
import com.singpaulee.tutorialrsaencryption.fragment.ListNoteFragment
import com.singpaulee.tutorialrsaencryption.fragment.ProfilFragment
import com.singpaulee.tutorialrsaencryption.helper.SharedPrefManager
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.app_bar_dashboard.*
import kotlinx.android.synthetic.main.nav_header_dashboard.*
import kotlinx.android.synthetic.main.nav_header_dashboard.view.*
import org.jetbrains.anko.toast

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var exitApplication = false
    var hideMenu: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            var i = Intent(this@DashboardActivity, AddNoteActivity::class.java)
            startActivity(i)
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        hideMenu = false
        supportFragmentManager.beginTransaction()
            .replace(R.id.dashboard_framelayout, ListNoteFragment())
            .commit()

        var header = nav_view.getHeaderView(0)
        header.nhd_tv_username.text = SharedPrefManager(this).getUsername().toString()

        nav_view.setCheckedItem(R.id.nav_dashboard)

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            if (!exitApplication){
                exitApplication = true
                toast("Tekan kembali sekali lagi untuk keluar aplikasi")
                Handler().postDelayed({
                    exitApplication = false
                },2000)
            }else{
                super.onBackPressed()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.dashboard, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_ascending -> {
                var listNoteFragment: ListNoteFragment = ListNoteFragment()
                var bundle = Bundle()
                bundle.putString("SORT","ASC")
                listNoteFragment.arguments = bundle

                supportFragmentManager.beginTransaction()
                    .replace(R.id.dashboard_framelayout, listNoteFragment)
                    .commit()
                return true
            }
            R.id.action_descending -> {
                var listNoteFragment: ListNoteFragment = ListNoteFragment()
                var bundle = Bundle()
                bundle.putString("SORT","DESC")
                listNoteFragment.arguments = bundle

                supportFragmentManager.beginTransaction()
                    .replace(R.id.dashboard_framelayout, listNoteFragment)
                    .commit()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (hideMenu){
            menu?.findItem(R.id.action_ascending)?.isVisible = false
            menu?.findItem(R.id.action_descending)?.isVisible = false
        }
        return super.onPrepareOptionsMenu(menu)
    }

    @SuppressLint("RestrictedApi")
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_dashboard -> {
                fab.visibility = View.VISIBLE
                hideMenu = false
                supportFragmentManager.beginTransaction()
                    .replace(R.id.dashboard_framelayout, ListNoteFragment())
                    .commit()
                invalidateOptionsMenu()
            }
            R.id.nav_profil -> {
                fab.visibility = View.GONE
                hideMenu = true
                supportFragmentManager.beginTransaction()
                    .replace(R.id.dashboard_framelayout, ProfilFragment())
                    .commit()

                invalidateOptionsMenu()
            }
            R.id.nav_decrypt -> {
                hideMenu = true
                fab.visibility = View.GONE
                supportFragmentManager.beginTransaction()
                    .replace(R.id.dashboard_framelayout, DecryptFragment())
                    .commit()
                invalidateOptionsMenu()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}

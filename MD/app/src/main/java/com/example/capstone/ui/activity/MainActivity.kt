@file:Suppress("DEPRECATION")

package com.example.capstone.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.capstone.R
import com.example.capstone.data.local.UserSession
import com.example.capstone.ui.fragment.HomeFragment
import com.example.capstone.ui.viewmodel.MainViewModel
import com.example.capstone.ui.viewmodel.factory.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "AiCare"
        val homeFragment = HomeFragment()
        val pref = UserSession.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref, this))[MainViewModel::class.java]

        mainViewModel.userToken.observe(this)
        { token: String ->
            if (token.isEmpty()) {
                val i = Intent(this, LoginActivity::class.java)
                startActivity(i)
                finish()
            }
        }

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        setCurrentFragment(homeFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> setCurrentFragment(homeFragment)
                R.id.logout -> {
                    mainViewModel.saveToken("", "", "", "", "")
                    val i = Intent(this, LoginActivity::class.java)
                    startActivity(i)
                    finish()
                }
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }

    companion object {
        val emailRegex: Regex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+\$")
        val phoneRegex: Regex = Regex("^(^\\+62|62|^08)(\\d{3,4}-?){2}\\d{3,4}\$")
    }
}
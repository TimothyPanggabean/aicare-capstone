package com.example.capstone.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.capstone.data.local.UserSession
import com.example.capstone.databinding.ActivityProfileBinding
import com.example.capstone.ui.viewmodel.ProfileViewModel
import com.example.capstone.ui.viewmodel.factory.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileActivity : AppCompatActivity() {

    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserSession.getInstance(dataStore)

        val profileViewModel =
            ViewModelProvider(this, ViewModelFactory(pref, this))[ProfileViewModel::class.java]

        profileViewModel.userToken.observe(this)
        { token: String ->
            if (token.isEmpty()) {
                val i = Intent(this, LoginActivity::class.java)
                startActivity(i)
                finish()
            }
        }

        profileViewModel.userName.observe(this) { name: String ->
            binding.tvItemNameProfile.text = name
        }

        profileViewModel.userEmail.observe(this) { email: String ->
            binding.tvItemEmailProfile.text = email
        }

        profileViewModel.userPhone.observe(this) { phone: String ->
            binding.tvItemNumberProfile.text = "0" + phone
        }
    }
}
package com.example.capstone.ui.activity

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.capstone.data.remote.api.ApiConfig
import com.example.capstone.data.remote.pojo.RegisterResponse
import com.example.capstone.databinding.ActivityRegisterBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerButton: Button
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var phoneNumberEditText: EditText
    private lateinit var fullNameEditText: EditText

    private var correctEmail = false
    private var correctPassword = false
    private var correctName = false
    private var correctPhoneNumber = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerButton = binding.registerButton
        emailEditText = binding.emailEditText
        passwordEditText = binding.passwordEditText
        phoneNumberEditText = binding.phoneNumberEditText
        fullNameEditText = binding.nameEditText

        fullNameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                correctName = !(!s.isNullOrEmpty() && s.length < 3)
                if (!correctName) fullNameEditText.error = "Minimum Name Length 3"
                setLoginButtonEnable()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        phoneNumberEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                correctPhoneNumber =
                    !s.isNullOrEmpty() && MainActivity.phoneRegex.matches(s.toString())
                if (!correctPhoneNumber) phoneNumberEditText.error = "Wrong Phone Number Format"
                setLoginButtonEnable()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                correctEmail =
                    !s.isNullOrEmpty() && MainActivity.emailRegex.matches(s.toString())
                if (!correctEmail) emailEditText.error = "Wrong Email Format"
                setLoginButtonEnable()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                correctPassword = s!!.length > 6
                if (!correctPassword) passwordEditText.error = "Minimum Password Length 6"
                setLoginButtonEnable()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.registerButton.setOnClickListener { goRegister() }

        binding.tvAccount.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    private fun goRegister() {
        showLoading(true)
        val client = ApiConfig.getApiService().register(
            fullNameEditText.text.toString(),
            emailEditText.text.toString(),
            passwordEditText.text.toString(),
            phoneNumberEditText.text.toString()
        )
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    if (responseBody.error == true) {
                        showLoading(false)
                        Toast.makeText(
                            this@RegisterActivity,
                            responseBody.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        goLogin()
                        Toast.makeText(
                            this@RegisterActivity,
                            responseBody.message,
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                } else {
                    showLoading(false)
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    Toast.makeText(this@RegisterActivity, response.message(), Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                showLoading(false)
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                Toast.makeText(this@RegisterActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    private fun goLogin() {
        val i = Intent(this, LoginActivity::class.java)
        i.putExtra("email", emailEditText.text.toString())
        i.putExtra("password", passwordEditText.text.toString())
        i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(i)
        showLoading(false)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.apply {
            visibility = if (isLoading) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun setLoginButtonEnable() {
        registerButton.isEnabled =
            correctEmail && correctPassword && correctName && correctPhoneNumber
    }
}
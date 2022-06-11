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
import androidx.lifecycle.ViewModelProvider
import com.example.capstone.R
import com.example.capstone.data.local.UserSession
import com.example.capstone.data.remote.api.ApiConfig
import com.example.capstone.data.remote.pojo.LoginInfo
import com.example.capstone.data.remote.pojo.LoginResponse
import com.example.capstone.databinding.ActivityLoginBinding
import com.example.capstone.ui.viewmodel.LoginViewModel
import com.example.capstone.ui.viewmodel.factory.ViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginButton: Button
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    private lateinit var loginViewModel: LoginViewModel

    private var correctEmail = false
    private var correctPassword = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = UserSession.getInstance(dataStore)

        loginButton = binding.loginButton
        emailEditText = binding.emailEditText
        passwordEditText = binding.passwordEditText

        if (!intent.getStringExtra("email").isNullOrEmpty()) {
            emailEditText.setText(intent.getStringExtra("email"))
            correctEmail = true
        }
        if (!intent.getStringExtra("password").isNullOrEmpty()) {
            passwordEditText.setText(intent.getStringExtra("password"))
            correctPassword = true
        }

        loginViewModel = ViewModelProvider(
            this,
            ViewModelFactory(pref, this)
        )[LoginViewModel::class.java]

        setLoginButtonEnable()

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
                if (!correctPassword) passwordEditText.error = "Minimum Password 6"
                setLoginButtonEnable()
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        loginButton.setOnClickListener {
            goLogin()
        }

        binding.tvAccount.setOnClickListener {
            val i = Intent(this, RegisterActivity::class.java)
            startActivity(i)
            finish()
        }
    }

    private fun goLogin() {
        showLoading(true)
        val client = ApiConfig.getApiService()
            .login(emailEditText.text.toString(), passwordEditText.text.toString())
        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    if (responseBody.error == true) {
                        showLoading(false)
                        Toast.makeText(this@LoginActivity, responseBody.message, Toast.LENGTH_LONG)
                            .show()
                    } else {
                        saveSession(responseBody.loginInfo as LoginInfo)
                        Toast.makeText(
                            this@LoginActivity,
                            getString(R.string.login_sucess),
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                } else {
                    showLoading(false)
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    Toast.makeText(this@LoginActivity, response.message(), Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                showLoading(false)
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
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

    private fun saveSession(loginResult: LoginInfo) {
        showLoading(false)
        loginViewModel.saveToken(loginResult.token as String, loginResult.id as String, loginResult.name as String, loginResult.email as String, loginResult.number as String)
        val i = Intent(this, MainActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
        finish()
    }

    private fun setLoginButtonEnable() {
        loginButton.isEnabled = correctEmail && correctPassword
    }
}
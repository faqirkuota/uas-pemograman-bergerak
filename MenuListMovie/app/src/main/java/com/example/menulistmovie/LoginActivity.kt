package com.example.menulistmovie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var btnRegister : Button
    private lateinit var btnLogin : Button
    private lateinit var txtEmail : EditText
    private lateinit var txtPassword : EditText
    private lateinit var otentikasi : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnRegister = findViewById(R.id.bRegister)
        txtEmail = findViewById(R.id.etEmail)
        txtPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.bLogin)
        otentikasi = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener {
            val email = txtEmail.text.toString().trim()
            val password = txtPassword.text.toString().trim()

            if (email.isEmpty()){
                txtEmail.error = "Email tidak boleh kosong"
                txtEmail.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty() || password.length < 6){
                txtPassword.error = "Password tidak boleh kosong dan minimal 6 karakter"
                txtPassword.requestFocus()
                return@setOnClickListener
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                txtEmail.error = "Format penulisan Email Salah"
                txtEmail.requestFocus()
                return@setOnClickListener
            }
            loginUser(email, password)
        }

        btnRegister.setOnClickListener {
            Intent(this@LoginActivity, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }
    }
    private fun loginUser(email: String, password: String) {
        otentikasi.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    if (email == "admin@gmail.com"){
                        Intent(this@LoginActivity, SearchActivity::class.java).also { intent ->
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                    }else{
                        Intent(this@LoginActivity, MainActivity::class.java).also { intent ->
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                    }
                }else{
                    Toast.makeText(this,"${it.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
    override fun onStart() {
        super.onStart()
        if(otentikasi.currentUser != null) {
            Intent(this@LoginActivity, SearchActivity::class.java).also { intent ->
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }
}
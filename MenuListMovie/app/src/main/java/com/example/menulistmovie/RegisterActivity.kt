package com.example.menulistmovie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var btnLogin : Button
    private lateinit var btnSimpan : Button
    private lateinit var txtEmail : EditText
    private lateinit var txtPassword : EditText
    private lateinit var otentikasi : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        otentikasi = FirebaseAuth.getInstance()
        btnSimpan = findViewById(R.id.bSimpan)
        btnLogin = findViewById(R.id.bLogin)
        txtEmail = findViewById(R.id.etEmailReg)
        txtPassword = findViewById(R.id.etPasswordReg)

        btnSimpan.setOnClickListener {
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
            registerUser(email, password)
        }
        btnLogin.setOnClickListener {
            Intent(this@RegisterActivity, MainActivity::class.java).also {
                startActivity(it)
            }
        }
    }
    private fun registerUser(email: String, password: String) {
        otentikasi.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){
                if(it.isSuccessful){
                    if (email == "admin@gmail.com"){
                        Intent(this@RegisterActivity, SearchActivity::class.java).also { intent ->
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                    }else{
                        Intent(this@RegisterActivity, MainActivity::class.java).also { intent ->
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                    }
                }else{
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }
    override fun onStart() {
        super.onStart()
        if(otentikasi.currentUser != null) {
            Intent(this@RegisterActivity, SearchActivity::class.java).also { intent ->
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }
}
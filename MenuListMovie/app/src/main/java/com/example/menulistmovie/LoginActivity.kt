package com.example.menulistmovie

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
        /**
         * @desc Membuat initilisasi variabel pada layout
         */
        btnRegister = findViewById(R.id.bRegister)
        txtEmail = findViewById(R.id.etEmail)
        txtPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.bLogin)
        otentikasi = FirebaseAuth.getInstance()

        /**
         * @desc Membuat on click listener pada btn login dan juga memvalidasi email dan passsword
         * Beserta format email
         * Serta memanggil methode loginUser
         */
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

        /**
         * @desc Membuat on click listener pada btn register agar langsung mengarahkan pada halaman
         * Register
         */
        btnRegister.setOnClickListener {
            Intent(this@LoginActivity, RegisterActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    /**
     * @desc Methode ini untuk menvalidasi otentikasi email dan password yang dimasukan
     * Dan dituliskan logic jika login sebagai admin maka diarahkan kehalaman Search Khusus admin
     * Dan Jika login sebagai non admin maka diarahkan kehalaman search khusus user
     */
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

    /**
     * @desc Methode ini untuk mendapatkan otentikasi current user yang tersimpan
     * agar user tidak perlu login lagi dan juga ada logic yang sama dengan methode loginUser
     */
    override fun onStart() {
        super.onStart()
        if(otentikasi.currentUser != null) {
            var email: String = otentikasi.currentUser!!.email.toString()
            if (email == "admin@gmail.com") {
                Intent(this@LoginActivity, SearchActivity::class.java).also { intent ->
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            } else {
                Intent(this@LoginActivity, MainActivity::class.java).also { intent ->
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
        }
    }
}
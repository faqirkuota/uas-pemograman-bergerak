package com.example.menulistmovie

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
        /**
         * @desc Membuat initilisasi variabel pada layout
         */
        otentikasi = FirebaseAuth.getInstance()
        btnSimpan = findViewById(R.id.bSimpan)
        btnLogin = findViewById(R.id.bLogin)
        txtEmail = findViewById(R.id.etEmailReg)
        txtPassword = findViewById(R.id.etPasswordReg)

        /**
         * @desc Membuat on click listener pada btn Simpan dan juga memvalidasi email dan passsword
         * Beserta format email
         * Serta memanggil methode registerUser
         */
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

        /**
         * @desc Membuat on click listener pada btn login agar langsung mengarahkan pada halaman
         * Login
         */
        btnLogin.setOnClickListener {
            Intent(this@RegisterActivity, MainActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    /**
     * @desc Methode ini untuk meregister user ke firebase serta menvalidasi otentikasi email dan password yang dimasukan
     * Dan dituliskan logic jika register sebagai admin maka diarahkan kehalaman Search Khusus admin
     * Dan Jika register sebagai non admin maka diarahkan kehalaman login khusus user
     */
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
                        Intent(this@RegisterActivity, LoginActivity::class.java).also { intent ->
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }
                    }
                } else {
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }
}
package com.zinwailinn.hiworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class SecondActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var signInBtn: Button
    private lateinit var signUpBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        mAuth = FirebaseAuth.getInstance()
        signInBtn = findViewById(R.id.signin_btn)
        signUpBtn = findViewById(R.id.signup_btn)
        signInBtn.setOnClickListener {
            val email = et_email_signin.text.toString()
            val password = et_password_signin.text.toString()
            userSignin(email, password)

        }
        signUpBtn.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }


    private fun userSignin(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(this,"Successfully SignIn", Toast.LENGTH_SHORT).show()
                val intent = Intent(this,ThirdActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this,"Failed SignIn",Toast.LENGTH_SHORT).show()
            }

        }
    }
}

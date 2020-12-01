package com.zinwailinn.hiworld

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.alert_dialog.*

class MainActivity : AppCompatActivity() {
    private lateinit var mAuth : FirebaseAuth
    private lateinit var signupBtn : Button
    private lateinit var login: TextView
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkConnection()
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        signupBtn = findViewById(R.id.signin_btn)
        login = findViewById(R.id.tv_signin)



        signupBtn.setOnClickListener {
            val email = et_email_signin.text.toString()
            val password = et_password_signin.text.toString()
            userSignup(email,password) }

        login.setOnClickListener {
            val intent = Intent(this,SecondActivity::class.java)
            startActivity(intent)
        }

    }

    private fun userSignup(email:String , password: String) {
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(this,"Successfully SignUp",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,SecondActivity::class.java)
                    startActivity(intent)
                    val currentUser = mAuth.currentUser?.uid
                    val myRef = database.getReference("Users")
                    val usrDB = myRef.child(currentUser!!)
                    usrDB.child("firstname").setValue(et_firstName.text.toString())
                    usrDB.child("lastname").setValue(et_lastName.text.toString())
                    usrDB.child("phonenumber").setValue(et_phoneNumber.text.toString())
                }else{
                    Toast.makeText(this,"fail SignUp",Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun checkConnection() {
        val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = manager.activeNetworkInfo

        if(null != networkInfo){
            if (networkInfo.type == ConnectivityManager.TYPE_WIFI){
                Toast.makeText(this,"Wifi Connected",Toast.LENGTH_SHORT).show()
            }else if (networkInfo.type == ConnectivityManager.TYPE_MOBILE) {
                Toast.makeText(this, "Mobile Data Connected", Toast.LENGTH_SHORT).show()
            }

        }else{
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.alert_dialog)
            dialog.setCanceledOnTouchOutside(false)
            dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.try_again_btn.setOnClickListener {
                recreate()
            }
            dialog.show()
        }
    }

}
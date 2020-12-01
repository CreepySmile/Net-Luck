package com.zinwailinn.hiworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_third.*
import kotlin.collections.ArrayList

private lateinit var database: FirebaseDatabase
private lateinit var firstName : TextView
private lateinit var lastName : TextView
private lateinit var logoutBtn : TextView
private lateinit var mAuth: FirebaseAuth
private lateinit var enterRoom : Button
private lateinit var createBtn : CardView
private lateinit var myRoomBtn : CardView
private var roomIdList : ArrayList<String> = ArrayList()

class ThirdActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        mAuth = FirebaseAuth.getInstance()
        firstName = findViewById(R.id.tv_first_username)
        lastName = findViewById(R.id.tv_last_username)
        logoutBtn = findViewById(R.id.tv_logout)
        createBtn = findViewById(R.id.btn_create)
        myRoomBtn = findViewById(R.id.my_room_btn)
        enterRoom = findViewById(R.id.btn_enter)
        val user = mAuth.currentUser?.uid
        database = FirebaseDatabase.getInstance()

        val myRef = database.getReference("Users").child(user!!)

        myRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val sfirstName = snapshot.child("firstname")
                val slastName = snapshot.child("lastname")
                val phoneNumber = snapshot.child("phonenumber")
                firstName.text = sfirstName.value.toString()
                lastName.text = slastName.value.toString()
                tv_phone_number.text = phoneNumber.value.toString() 

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Error>>>",error.toString())
            }

        })
        


        val myRooms = database.getReference("Rooms")

        myRooms.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children){
                    roomIdList.add(data.key.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


        enterRoom.setOnClickListener {
            val roomCode = et_room_code.text.toString()
            if(roomIdList.contains(roomCode)){
            val intent = Intent(this,FifthActivity::class.java)
            intent.putExtra("room_code",roomCode)
            startActivity(intent)
            }else{
                Toast.makeText(this,"does not exit this room code",Toast.LENGTH_SHORT).show()
            }


        }

        myRoomBtn.setOnClickListener {
            val intent = Intent(this,FourthActivity::class.java)
            startActivity(intent)
        }

        createBtn.setOnClickListener {
            startActivity(Intent(this,CreateRoomActivity::class.java))
        }


        logoutBtn.setOnClickListener {
            mAuth.signOut()
            finish()
        }

        Log.d("data>>>", roomIdList.toString())

    }



}

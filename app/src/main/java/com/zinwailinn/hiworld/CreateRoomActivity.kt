package com.zinwailinn.hiworld

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

private lateinit var slot : EditText
private lateinit var winner : EditText
private lateinit var status : EditText

private lateinit var createRoomBtn : Button
private lateinit var database: FirebaseDatabase

class CreateRoomActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_room)
        slot = findViewById(R.id.et_slot)
        winner = findViewById(R.id.et_winner)
        status = findViewById(R.id.et_status)

        createRoomBtn = findViewById(R.id.create_room_btn)
        database = FirebaseDatabase.getInstance()

        createRoomBtn.setOnClickListener {
            val getSlot = slot.text.toString()
            val getWinner = winner.text.toString()
            val getStatus = status.text.toString()

            createRoom(getSlot,getWinner,getStatus)
            startActivity(Intent(this,FourthActivity::class.java))
        }
    }

    private fun createRoom(slot: String, winner: String,status : String) {

        val myRooms = database.getReference("Rooms")
        val userID = FirebaseAuth.getInstance().currentUser?.uid
        val key = myRooms.push().key

        myRooms.child(key!!).setValue(Rooms(slot,winner,userID!!,key,status))
    }
}
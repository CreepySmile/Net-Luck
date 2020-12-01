package com.zinwailinn.hiworld

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zinwailinn.hiworld.adapter.RoomAdapter
import kotlinx.android.synthetic.main.activity_fourth.*

class FourthActivity : AppCompatActivity() , RoomAdapter.RoomItemClickListener  {
    private lateinit var database: FirebaseDatabase
    private lateinit var mAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fourth)
        database = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()

        val currentUser = mAuth.currentUser?.uid

        val roomRef = database.getReference("Rooms")
        val roomItem = ArrayList<Rooms>()
        roomRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        val roomId = data.child("roomID").value.toString()
                        val slot = data.child("slot").value.toString()
                        val winner = data.child("winner").value.toString()
                        val userId = data.child("userID").value.toString()
                        val status = data.child("status").value.toString()
                        if (currentUser == userId) {
                            roomItem.add(Rooms(slot, winner, userId, roomId,status))
                        }
                    }

                    room_recyclerView.layoutManager = LinearLayoutManager(applicationContext)
                    val roomAdapter = RoomAdapter()
                    room_recyclerView.adapter = roomAdapter
                    roomAdapter.updateRoom(roomItem.reversed())
                    roomAdapter.setOnClickListener(this@FourthActivity)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })


    }

    override fun onItemClick(roomItem: Rooms) {
       val roomCode = roomItem.roomID
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("copied Room ID",roomCode)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(this,"Room ID ${roomItem.roomID} copied to clipboard",Toast.LENGTH_SHORT).show()
    }


}
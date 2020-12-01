package com.zinwailinn.hiworld

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zinwailinn.hiworld.adapter.LuckyUserAdapter
import com.zinwailinn.hiworld.adapter.WinnerAdapter
import kotlinx.android.synthetic.main.activity_fifth.*
import kotlin.random.Random

class FifthActivity : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var luckBtn : Button
    private lateinit var drawBtn : Button
    private lateinit var mAuth : FirebaseAuth
    private lateinit var luckRecyclerView : RecyclerView
    private var userNumber = 0

    val luckyUserItem = ArrayList<LuckyUser>()
    val luckyUserIdList : ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fifth)
        database = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        luckBtn = findViewById(R.id.luck_btn)
       drawBtn = findViewById(R.id.draw_btn)
        luckRecyclerView = findViewById(R.id.luck_recyclerView)
        val intent = intent
        val roomCode = intent.getStringExtra("room_code")
        if (roomCode != null) {
            val roomsRef = database.getReference("Rooms").child(roomCode)
            roomsRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val slot = snapshot.child("slot").value.toString()
                    val winner = snapshot.child("winner").value.toString()
                    val creatorId = snapshot.child("userID").value.toString()
                    val status = snapshot.child("status").value.toString()
                    tv_slot.text = slot
                    tv_winner.text = winner
                    tv_room_code.text = roomCode
                    tv_status.text = status

                    val myRef = database.getReference("Users").child(creatorId)
                    myRef.addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val sfirstName = snapshot.child("firstname")
                            val slastName = snapshot.child("lastname")
                            tv_creator_name.text = sfirstName.value.toString()
                            tv_last_creator_name.text = slastName.value.toString()


                        }

                        override fun onCancelled(error: DatabaseError) {

                        }

                    })

                    val currentUser = mAuth.currentUser?.uid
                    if(currentUser == creatorId){
                        drawBtn.visibility = View.VISIBLE
                        luckBtn.visibility = View.GONE
                    }






                    val winnerNumberRef = roomsRef.child("winnerNumber")
                    val key = winnerNumberRef.push().key
                    drawBtn.setOnClickListener {
                        val winnerNumber = Random.nextInt(userNumber) + 1
                        winnerNumberRef.child(key!!).child("no").setValue(winnerNumber.toString())
                        luck_recyclerView.visibility = View.GONE
                        winner_recyclerView.visibility = View.VISIBLE
                    }
                    val winnerNumberList : ArrayList<String> = ArrayList()
                    winnerNumberRef.addValueEventListener(object : ValueEventListener{

                        override fun onDataChange(snapshot: DataSnapshot) {
                            val winnerNumberCount = snapshot.childrenCount.toInt().toString()
                            for (data in snapshot.children){
                                val no = data.child("no").value.toString()
                                winnerNumberList.add(no)
                            }
                            if (winnerNumberCount == winner){
                                drawBtn.visibility = View.GONE


                            }

                            val winnerUserIdList : ArrayList<Winner> = ArrayList()
                            val luckyUserRef = roomsRef.child("luckyUser")
                            luckyUserRef.addValueEventListener(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val usrNo = snapshot.childrenCount.toString()

                                    drawBtn.isEnabled = usrNo == slot
                                    for(data in snapshot.children){
                                        val userID = data.child("userID").value.toString()
                                        val luckyNumber = data.child("luckyNumber").value.toString()
                                        if (winnerNumberList.contains(luckyNumber) ){
                                        winnerUserIdList.add(Winner(userID))
                                        }
                                    }
                                    winner_recyclerView.layoutManager = LinearLayoutManager(applicationContext)
                                    val winnerAdapter = WinnerAdapter()
                                    winner_recyclerView.adapter = winnerAdapter
                                    winnerAdapter.updateWinner(winnerUserIdList)

                                }

                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }

                            })


                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
            //this is try luck user recyclerview
            val luckyUserRef = roomsRef.child("luckyUser")
            luckyUserRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    userNumber = snapshot.childrenCount.toInt()
                    tv_user_number.text = userNumber.toString()
                    for (data in snapshot.children) {
                        val luckyUserId = data.child("userID").value.toString()
                        val luckyNumber = data.child("luckyNumber").value.toString()
                        luckyUserIdList.add(luckyUserId)
                        luckyUserItem.add(LuckyUser(luckyUserId,luckyNumber))
                    }

                    luckRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
                    val luckyUserAdaper = LuckyUserAdapter()
                    luckRecyclerView.setHasFixedSize(true)
                    luckRecyclerView.adapter = luckyUserAdaper
                    luckyUserAdaper.updateLuckyUser(luckyUserItem)
                    luckRecyclerView.invalidate()

                }


                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })


            // this is add try luck user
            luckBtn.setOnClickListener {
                val currentUser = mAuth.currentUser?.uid
                val result = userNumber + 1
                val key = luckyUserRef.push().key
                if(luckyUserIdList.contains(currentUser.toString()) ){
                    Toast.makeText(this,"You're done!",Toast.LENGTH_SHORT).show()
                    luckBtn.visibility = View.GONE
                }
                else {
                    luckyUserRef.child(key!!).setValue(LuckyUser(currentUser!!, result.toString()))
                }

            }
        }
    }

}
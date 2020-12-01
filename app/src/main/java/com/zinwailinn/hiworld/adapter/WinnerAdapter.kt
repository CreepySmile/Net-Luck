package com.zinwailinn.hiworld.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.zinwailinn.hiworld.R
import com.zinwailinn.hiworld.Winner
import kotlinx.android.synthetic.main.lucky_user_list.view.*
import kotlinx.android.synthetic.main.winner_list.view.*

class WinnerAdapter() : RecyclerView.Adapter<WinnerAdapter.WinnerViewHolder>() {
    private var winnerItem : List<Winner> = ArrayList()
    inner class WinnerViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        private lateinit var database: FirebaseDatabase
        fun bind(winnerItem : Winner){
            database = FirebaseDatabase.getInstance()
            val userRef = database.getReference("Users").child(winnerItem.userID)
            userRef.addValueEventListener(object : ValueEventListener {
                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {
                    val firstName = snapshot.child("firstname").value.toString()
                    val lastName = snapshot.child("lastname").value.toString()
                    itemView.tv_winner_username.text = "$firstName $lastName"

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WinnerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.winner_list,parent,false)
        return WinnerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WinnerViewHolder, position: Int) {
        holder.bind(winnerItem[position])
    }

    override fun getItemCount(): Int {
        return winnerItem.size
    }
    fun updateWinner(winnerItem : List<Winner>){
        this.winnerItem = winnerItem
    }
}
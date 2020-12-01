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
import com.zinwailinn.hiworld.LuckyUser
import com.zinwailinn.hiworld.R
import kotlinx.android.synthetic.main.lucky_user_list.view.*


class LuckyUserAdapter : RecyclerView.Adapter<LuckyUserAdapter.LuckyUserViewModel>() {
    private var luckyUserItem : List<LuckyUser> = ArrayList()
    inner class LuckyUserViewModel(itemView : View) : RecyclerView.ViewHolder(itemView){
        private lateinit var database: FirebaseDatabase

        fun bind(luckyUser: LuckyUser){
            database = FirebaseDatabase.getInstance()
            val userRef = database.getReference("Users").child(luckyUser.userID)
            userRef.addValueEventListener(object : ValueEventListener{
                @SuppressLint("SetTextI18n")
                override fun onDataChange(snapshot: DataSnapshot) {
                    val firstName = snapshot.child("firstname").value.toString()
                    val lastName = snapshot.child("lastname").value.toString()
                    itemView.tv_lucky_username.text = "$firstName $lastName"

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
            itemView.tv_lucky_number.text = luckyUser.luckyNumber
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LuckyUserViewModel {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.lucky_user_list,parent,false)
        return LuckyUserViewModel(itemView)
    }

    override fun onBindViewHolder(holder: LuckyUserViewModel, position: Int) {
        holder.bind(luckyUserItem[position])


    }

    override fun getItemCount(): Int = luckyUserItem.size

    fun updateLuckyUser(luckyUserItem: List<LuckyUser>){
        this.luckyUserItem = luckyUserItem

    }

}
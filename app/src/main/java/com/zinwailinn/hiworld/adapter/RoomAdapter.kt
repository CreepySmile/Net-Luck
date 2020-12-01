package com.zinwailinn.hiworld.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zinwailinn.hiworld.R
import com.zinwailinn.hiworld.Rooms
import kotlinx.android.synthetic.main.room_list.view.*

class RoomAdapter : RecyclerView.Adapter<RoomAdapter.RoomViewHolder>()  {
    private var roomItem : List<Rooms> = ArrayList()
    var roomItemClickListener : RoomItemClickListener? = null
    inner class RoomViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        lateinit var roomsItems : Rooms
        fun bind(roomItem : Rooms){
            this.roomsItems = roomItem
            itemView.tv_room_id.text = roomsItems.roomID
            itemView.tv_slot_number.text = roomsItems.slot
            itemView.tv_winner_number.text = roomsItems.winner
        }
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            roomItemClickListener?.onItemClick(roomsItems)
        }
    }

    fun setOnClickListener(roomItemClickListener : RoomItemClickListener) {
        this.roomItemClickListener = roomItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.room_list,parent,false)
        return RoomViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.bind(roomItem[position])
    }

    override fun getItemCount(): Int {
        return roomItem.size
    }

    fun updateRoom(roomItem: List<Rooms>){
        this.roomItem = roomItem
    }

    interface RoomItemClickListener {
        fun onItemClick(roomItem : Rooms)
    }

}
package com.ansela.myalrmapp.adapter

import androidx.recyclerview.widget.DiffUtil
import com.ansela.myalrmapp.room.Alarm

class AlarmDiffUtil (private val oldlist: List<Alarm>,private val newList: List<Alarm>) : DiffUtil.Callback(){
    override fun getOldListSize(): Int {
        return oldlist.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldlist[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldData = oldlist[oldItemPosition]
        val newData = newList[newItemPosition]
        return oldData.id == newData.id
                && oldData.date == newData.date
                && oldData.time == newData.time
                && oldData.note == newData.note
                && oldData.type == newData.type
    }
}
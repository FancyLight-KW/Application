package com.fancylight.helpdesk.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fancylight.helpdesk.R
import com.fancylight.helpdesk.model.Personnel

class PersonnelAdapter(
    private val context: Context,
    private val list: List<Personnel>
) : RecyclerView.Adapter<PersonnelAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // 아이템뷰의 위젯 획득
        private val idText: TextView = itemView.findViewById(R.id.txt_personnel_id)
        private val nameText: TextView = itemView.findViewById(R.id.txt_personnel_name)
        private val ongoingText: TextView = itemView.findViewById(R.id.txt_number_ongoing)
        private val assignedText: TextView = itemView.findViewById(R.id.txt_number_assigned)

        fun bind(model: Personnel, listener: OnItemClickListener?) {

            idText.text = model.id
            nameText.text = model.name
            ongoingText.text = model.numberOngoing.toString()
            assignedText.text = model.numberAssigned.toString()

            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(adapterPosition)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.item_personnel, parent, false)

        return ViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(list[position], onItemClickListener)
    }

    override fun getItemCount(): Int {

        return list.size
    }


    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

}

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
        private val personnelList: List<Personnel>
) : RecyclerView.Adapter<PersonnelAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }




    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // 아이템뷰의 위젯 획득
        private val ongoingText: TextView = itemView.findViewById(R.id.txt_number_ongoing)
        private val assignedText: TextView = itemView.findViewById(R.id.txt_number_assigned)
        private val idText: TextView = itemView.findViewById(R.id.txt_personnel_id)
        private val nameText: TextView = itemView.findViewById(R.id.txt_personnel_name)


        fun bind(model: Personnel, listener: OnItemClickListener?) {

            ongoingText.text = model.DOING.toString()
            assignedText.text = model.READY.toString()
            idText.text = model.User_id
            nameText.text = model.User_name


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

        holder.bind(personnelList[position], listener)
    }


    var personnelSize = personnelList.size

    override fun getItemCount(): Int {

        return personnelSize
    }






}

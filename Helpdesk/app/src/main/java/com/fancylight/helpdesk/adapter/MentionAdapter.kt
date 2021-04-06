package com.fancylight.helpdesk.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fancylight.helpdesk.R
import com.fancylight.helpdesk.model.Mention

class MentionAdapter(private val list: List<Mention>, private val myName: String) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onQuestionClickListener: OnQuestionClickListener? = null
    fun setOnQuestionClickListener(listener: OnQuestionClickListener?) {
        onQuestionClickListener = listener
    }

    class MentionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var writerText: TextView = itemView.findViewById(R.id.txt_mention_writer)
        var messageText: TextView = itemView.findViewById(R.id.txt_mention_message)

        fun bind(model: Mention, myName: String) {
            writerText.text = model.writer
            messageText.text = model.message
            val params = itemView.layoutParams as MarginLayoutParams
            if (model.writer == myName) {
                params.marginStart = 70
                params.marginEnd = 0
            } else {
                params.marginStart = 0
                params.marginEnd = 70
            }
        }

    }

    class ButtonViewHolder(itemView: View, listener: OnQuestionClickListener?) :
            RecyclerView.ViewHolder(itemView) {
        var button: Button = itemView.findViewById(R.id.btn_question)

        init {
            if (listener != null) {
                button.setOnClickListener { v: View? -> listener.onQuestionClick() }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View
        return if (viewType == VIEW_TYPE_BUTTON) {
            itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.question_button_view, parent, false)
            ButtonViewHolder(itemView, onQuestionClickListener)
        } else {
            itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_mention, parent, false)
            MentionViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == VIEW_TYPE_MENTION) {
            val item: Mention = list[position]
            (holder as MentionViewHolder).bind(item, myName)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnQuestionClickListener {
        fun onQuestionClick()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position <= list.size) {
            VIEW_TYPE_MENTION
        } else {
            VIEW_TYPE_BUTTON
        }
    }

    companion object {
        const val VIEW_TYPE_MENTION = 0
        const val VIEW_TYPE_BUTTON = 1
    }

}
package com.example.smalltalkAndroid.feature.speech

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.paris.Paris
import com.airbnb.paris.extensions.style
import com.example.smalltalkAndroid.R
import com.example.smalltalkAndroid.model.Message
import kotlinx.android.synthetic.main.rw_conversation_item.view.*


enum class MessageOwner { CLIENT, SERVER }

class ConversationAdapter : RecyclerView.Adapter<ConversationAdapter.ConversationItemViewHolder>() {

    private val messages = mutableListOf<Message>()
    private var lastPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationItemViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.rw_conversation_item, parent, false)
        return ConversationItemViewHolder(v)
    }

    override fun getItemCount() = messages.count()

    override fun onBindViewHolder(holder: ConversationItemViewHolder, position: Int) {
        holder.updateUI(messages[position])
        setAnimation(holder.itemView, position)
    }

    fun addMessage(m: String, owner: MessageOwner) {
        messages.add(Message(m, owner))
        notifyItemInserted(itemCount - 1)
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(viewToAnimate.context, R.anim.slide_from_bottom)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    inner class ConversationItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var boxHeight = 0
        fun updateUI(message: Message) {
            itemView.conversation_item.text = message.text
            when (message.owner) {
                MessageOwner.CLIENT -> {
                    Paris.style(itemView.conversation_item).apply(R.style.MessageClient)
                }
                MessageOwner.SERVER -> {
                    Paris.style(itemView.conversation_item).apply(R.style.MessageServer)
                }
            }
        }

    }
}
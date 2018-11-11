package com.example.smalltalkAndroid.feature.speech

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.smalltalkAndroid.R
import com.example.smalltalkAndroid.model.Message
import com.mcxiaoke.koi.ext.onClick
import kotlinx.android.synthetic.main.rw_conversation_item_server.view.*


enum class MessageOwner { CLIENT, SERVER }

class ConversationAdapter : RecyclerView.Adapter<ConversationAdapter.ConversationItemViewHolder>() {

    private val messages = mutableListOf<Message>()
    private var lastPosition = -1
    var ttsCallback: (String) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationItemViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            if (viewType == 0)
                R.layout.rw_conversation_item_server
            else
                R.layout.rw_conversation_item_client,
            parent,
            false
        )
        return ConversationItemViewHolder(v)
    }

    override fun getItemCount() = messages.count()

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].owner == MessageOwner.SERVER) 0 else 1
    }

    override fun onBindViewHolder(holder: ConversationItemViewHolder, position: Int) {
        holder.updateUI(messages[position], position > lastPosition)
        setAnimation(holder.itemView, position)
    }

    override fun onViewRecycled(holder: ConversationItemViewHolder) {
        super.onViewRecycled(holder)
    }

    fun addMessage(m: String, owner: MessageOwner) {
        if (m.toLowerCase() != "reset") {
            if (m.isNotEmpty()) {
                messages.add(Message(m, owner))
                notifyItemInserted(itemCount - 1)
            }
        } else {
            messages.clear()
            lastPosition = -1
            messages.add(Message(m, owner))
            notifyDataSetChanged()
        }
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(viewToAnimate.context, R.anim.slide_from_bottom)
            viewToAnimate.startAnimation(animation)
            lastPosition = position
        }
    }

    fun updateLastMessage(partialMessage: String) {
        if (messages.last().owner != MessageOwner.CLIENT)
            addMessage(partialMessage, MessageOwner.CLIENT)
        else {
            messages[messages.size - 1].content = partialMessage
            notifyItemChanged(messages.size - 1)
        }
    }

    inner class ConversationItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun updateUI(message: Message, slowTyping: Boolean = true) {
            if (message.owner == MessageOwner.SERVER) {
                if (slowTyping)
                    itemView.conversation_item.animateText = message.content
                else
                    itemView.conversation_item.text = message.content
                itemView.conversation_tts.onClick { ttsCallback.invoke(itemView.conversation_item.text.toString()) }
            } else
                itemView.conversation_item.text = message.content
        }
    }
}
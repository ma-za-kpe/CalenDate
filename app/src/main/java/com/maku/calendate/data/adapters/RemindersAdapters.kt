package com.maku.calendate.data.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.maku.calendate.R
import com.maku.calendate.data.db.entities.Reminder
import com.maku.calendate.data.db.interfaces.ReminderInterface
import timber.log.Timber

class RemindersAdapters internal constructor(
    context: Context
) : RecyclerView.Adapter<RemindersAdapters.WordViewHolder>() {

    private var listener: ReminderInterface? = null

//    fun RemindersAdapters(context: Context, listener: ReminderInterface) {
//        this.listener = listener;
//    }

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var words = emptyList<Reminder>() // Cached copy of words

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wordItemView: TextView = itemView.findViewById(R.id.textView)
        val checkBoxDelete: CheckBox = itemView.findViewById(R.id.checkboxDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return WordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val current = words[position]
        //check if list is empty
        holder.wordItemView.text = current.description
        holder.wordItemView.setOnClickListener {
            Timber.d("date " + current.date + " timeeee " + current.time)
        }

        holder.checkBoxDelete.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Timber.d("ive been checked, so delete me")
                listener?.delete(current)
            }
        }

    }

    internal fun setWords(words: List<Reminder>) {
        this.words = words
        notifyDataSetChanged()
    }

    override fun getItemCount() = words.size
}
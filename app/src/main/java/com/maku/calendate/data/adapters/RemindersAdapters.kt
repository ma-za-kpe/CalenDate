package com.maku.calendate.data.adapters

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.maku.calendate.R
import com.maku.calendate.data.db.entities.Reminder
import com.maku.calendate.ui.fragments.list.ListViewModel
import timber.log.Timber
import java.util.concurrent.CompletionException

class RemindersAdapters(
    context: Context

) :
    RecyclerView.Adapter<RemindersAdapters.WordViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
//    private var viewModel: ListViewModel = ViewModelProvider(this).get(ListViewModel::class.java)

    private var words: List<Reminder>   // Cached copy of words

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
               // 1. STRIKE THROUGH THE TEXT TO SHOW Completion
                holder.wordItemView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                // 2. pass the item to the fragment
                Timber.d("passing back to fragment " + current)
            }
        }

    }

    internal fun setWords(words: List<Reminder>) {
        this.words = words
        notifyDataSetChanged()
    }

    override fun getItemCount() = words.size

    init {
        this.words = emptyList<Reminder>()
    }

}
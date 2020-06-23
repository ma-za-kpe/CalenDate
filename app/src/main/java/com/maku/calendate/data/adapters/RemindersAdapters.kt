package com.maku.calendate.data.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.maku.calendate.CalenDate
import com.maku.calendate.R
import com.maku.calendate.data.db.entities.Reminder
import timber.log.Timber


class RemindersAdapters(
    context: Context,
    val setAlarm : (Any) -> Unit,
    val getDetailsForAlarm : (Any) -> Unit,
    val onClickToRemove : (Any) -> Unit,
    val onClickToShowItemDetails : (Any) -> Unit
) :
    RecyclerView.Adapter<RemindersAdapters.WordViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    //private var viewModel: ListViewModel = ViewModelProvider(this).get(ListViewModel::class.java)

    private var words: List<Reminder>   // Cached copy of words

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val wordItemView: TextView = itemView.findViewById(R.id.textView)
        val checkBoxDelete: ImageButton = itemView.findViewById(R.id.checkboxDelete)
        val alarm: ImageButton = itemView.findViewById(R.id.alarm)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return WordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val current = words[position]
        getDetailsForAlarm(current)
        //check if list is empty
        holder.wordItemView.text = current.description
        holder.wordItemView.setOnClickListener {
            Timber.d("date " + current.date + " timeeee " + current.time)
            // set alarm notofication
            onClickToShowItemDetails(current)
        }

        holder.checkBoxDelete.setOnClickListener{ _ ->
                Timber.d("ive been checked, so delete me")
               // 1. STRIKE THROUGH THE TEXT TO SHOW Completion
                //holder.wordItemView.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                // 2. pass the item to the fragment
                Timber.d("passing back to fragment " + current)
                onClickToRemove(current)

        }

        holder.alarm.setOnClickListener {_ ->
            val new_image: Drawable = CalenDate.applicationContext().resources.getDrawable(R.drawable.ic_alarm_on)
            // turn alarm on when user clicks
            holder.alarm.background = new_image
            setAlarm(current)
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
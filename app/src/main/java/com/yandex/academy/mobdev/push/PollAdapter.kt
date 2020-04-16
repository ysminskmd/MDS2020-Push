package com.yandex.academy.mobdev.push

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_question.view.*
import kotlinx.android.synthetic.main.item_select.view.*

private const val viewTypeMessage = 0
private const val viewTypeSelect = 1
private const val viewTypeAnswer = 2

class PollAdapter(private val onAnswer: (Int) -> Unit) : RecyclerView.Adapter<PollViewHolder>() {

    var items = emptyList<PollItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    init {
        setHasStableIds(true)
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is PollItem.Question -> viewTypeMessage
            is PollItem.Select -> viewTypeSelect
            is PollItem.Answer -> viewTypeAnswer
        }
    }

    override fun getItemId(position: Int): Long {
        return items[position].id
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PollViewHolder {
        val resource = when (viewType) {
            viewTypeMessage -> R.layout.item_question
            viewTypeSelect -> R.layout.item_select
            viewTypeAnswer -> R.layout.item_answer
            else -> error("unknown view type: $viewType")
        }
        val itemView = LayoutInflater.from(parent.context).inflate(resource, parent, false)
        if (viewType == viewTypeSelect && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            itemView.option1.clipToOutline = true
            itemView.option2.clipToOutline = true
        }
        return PollViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PollViewHolder, position: Int) {
        when (val item = items[position]) {
            is PollItem.Question -> holder.itemView.text.text = item.text
            is PollItem.Select -> {
                val textView1 = holder.itemView.option1
                textView1.setText(item.text1)
                textView1.setOnClickListener { onAnswer(item.text1) }

                val textView2 = holder.itemView.option2
                textView2.setText(item.text2)
                textView2.setOnClickListener { onAnswer(item.text2) }
            }
            is PollItem.Answer -> holder.itemView.text.setText(item.text)
        }
    }
}

sealed class PollItem {

    abstract val id: Long

    data class Question(override val id: Long, val text: String) : PollItem()

    data class Select(
        override val id: Long,
        @StringRes val text1: Int,
        @StringRes val text2: Int
    ) : PollItem()

    data class Answer(override val id: Long, @StringRes val text: Int) : PollItem()
}

class PollViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
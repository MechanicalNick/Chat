package com.tinkoff.homework.date

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tinkoff.homework.databinding.DateItemBinding
import com.tinkoff.homework.utils.AdapterDelegate
import com.tinkoff.homework.utils.DelegateItem
import java.time.format.DateTimeFormatter

class DateDelegate : AdapterDelegate {
    companion object{
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    }

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(
            DateItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) {
        (holder as ViewHolder).bind(item.content() as DateModel)
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is DateDelegateItem

    class ViewHolder(private val binding: DateItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: DateModel) {
            with(binding) {
                date.text = model.date.format(formatter)
            }
        }
    }
}
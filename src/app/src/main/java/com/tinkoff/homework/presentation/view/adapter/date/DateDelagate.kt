package com.tinkoff.homework.presentation.view.adapter.date

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tinkoff.homework.databinding.DateItemBinding
import com.tinkoff.homework.domain.data.DateModel
import com.tinkoff.homework.presentation.view.DelegateItem
import com.tinkoff.homework.presentation.view.adapter.AdapterDelegate
import com.tinkoff.homework.utils.CustomDateFormatter
import java.time.format.DateTimeFormatter
import java.util.Locale

class DateDelegate : AdapterDelegate {
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
                date.text = CustomDateFormatter.formatDate(model.date)
            }
        }
    }
}
package com.tinkoff.homework.presentation.view.adapter.stream

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tinkoff.homework.R
import com.tinkoff.homework.domain.data.Stream
import com.tinkoff.homework.databinding.StreamItemBinding
import com.tinkoff.homework.navigation.DelegateItem
import com.tinkoff.homework.navigation.Expander
import com.tinkoff.homework.presentation.AdapterDelegate

class StreamDelegate(private val expander: Expander): AdapterDelegate {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(
            StreamItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            expander,
            parent.context
        )

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        item: DelegateItem,
        position: Int
    ) {
        (holder as ViewHolder).bind(item.content() as Stream, item)
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is StreamDelegateItem

    class ViewHolder(
        private val binding: StreamItemBinding,
        private val expander: Expander,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: Stream, item: DelegateItem) {
            with(binding) {
                streamName.text = context.getString(R.string.sharp, model.name)
                expanderView.isChecked = model.isExpanded
                val listener = View.OnClickListener {
                    if(!model.isExpanded) {
                        expander.expand(item as StreamDelegateItem);
                    }
                    else{
                        expander.collapse(item as StreamDelegateItem);
                    }
                }
                binding.root.setOnClickListener(listener)
                expanderView.setOnClickListener(listener)
            }
        }
    }
}
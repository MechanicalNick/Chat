package com.tinkoff.homework.utils.adapter.stream

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tinkoff.homework.R
import com.tinkoff.homework.data.domain.Stream
import com.tinkoff.homework.databinding.StreamItemBinding
import com.tinkoff.homework.utils.DelegateItem
import com.tinkoff.homework.utils.Expander
import com.tinkoff.homework.utils.adapter.AdapterDelegate

class StreamDelegate(private val expander: Expander): AdapterDelegate {
    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        StreamDelegate.ViewHolder(
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
                expanderView.setOnClickListener {
                    if(!model.isExpanded) {
                        expander.expand(item as StreamDelegateItem);
                    }
                    else{
                        expander.collapse(item as StreamDelegateItem);
                    }
                }
            }
        }
    }
}
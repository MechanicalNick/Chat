package com.tinkoff.homework.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tinkoff.homework.data.Emoji
import com.tinkoff.homework.databinding.EmojiItemBinding

class BottomSheetDialogAdapter(private val emojiList: List<Emoji>, private val callback: (input: Int) -> Unit) :
    RecyclerView.Adapter<BottomSheetDialogAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(EmojiItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), callback)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(emojiList[position])

    override fun getItemCount(): Int = emojiList.size

    class ViewHolder(_binding: EmojiItemBinding, private val callback: (input: Int) -> Unit)
        : RecyclerView.ViewHolder(_binding.root) {

        private var binding: EmojiItemBinding = EmojiItemBinding.bind(itemView)

        fun bind(model: Emoji) {
            binding.emojiId.text = model.getCodeString()
            binding.emojiId.setOnClickListener{
                callback(model.code)
            }
        }
    }
}
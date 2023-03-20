package com.tinkoff.homework.utils

import android.text.Editable
import com.tinkoff.homework.data.DateModel
import com.tinkoff.homework.data.EmojiWrapper
import com.tinkoff.homework.data.MessageModel
import com.tinkoff.homework.data.Reaction
import com.tinkoff.homework.utils.adapter.DeleagatesAdapter
import com.tinkoff.homework.utils.adapter.date.DateDelegateItem
import com.tinkoff.homework.utils.adapter.message.MessageDelegateItem
import java.time.LocalDate

class MessageFactory(private val adapter: DeleagatesAdapter,
                     messages: List<MessageModel>) {
    private val items: MutableList<DelegateItem> = mutableListOf()
    private var lastDate: LocalDate = LocalDate.now()

    init {
        lastDate = messages.minOf { message -> message.date }
        val groups = messages
            .sortedBy { message -> message.date }
            .groupBy { message -> message.date }
        val dates = mutableListOf<DateModel>()

        groups.entries.forEachIndexed { index, group ->
            val dateModel = DateModel(index + 1, group.key)
            dates.add(dateModel)
            items.add(DateDelegateItem(dateModel.id, dateModel))
            group.value.forEach{
                items.add(MessageDelegateItem(it.id, it))
            }
        }

        adapter.submitList(items)
    }

    fun addText(text: Editable?){
        text.let {
            val id = items.count() + 1
            val now = LocalDate.now()

            if(lastDate != now){
                val newDateItem = DateDelegateItem(id, DateModel(id, now))
                items.add(newDateItem)
                lastDate = now
            }

            val item = MessageDelegateItem(
                id,
                MessageModel(
                    id, it.toString(),
                    LocalDate.now(), mutableListOf()
                )
            )
            items.add(item)
            adapter.submitList(items)
        }
    }

    fun addEmoji(emojiWrapper: EmojiWrapper?) {
        emojiWrapper?.let {w ->
            var message = items
                .filterIsInstance<MessageDelegateItem>()
                .firstOrNull { message -> message.id == w.messageId }
            message?.let { m ->
                val model = (message.content() as MessageModel)
                val newReactions = mutableListOf<Reaction>()
                val curReaction = model.reactions.firstOrNull { r -> r.code == w.emojiCode }
                if (curReaction?.owners?.contains(Const.myId) == true)
                    return
                newReactions.addAll(model.reactions)
                if (curReaction == null) {
                    newReactions.add(Reaction(w.emojiCode, mutableListOf(Const.myId)))
                } else {
                    curReaction.owners.add(Const.myId)
                }
                val position = items.indexOf(m)
                val item = MessageDelegateItem(
                    model.id,
                    MessageModel(model.id, model.text, model.date, newReactions)
                )
                items.remove(message)
                items.add(position, item)
                adapter.notifyItemChanged(position)
            }
        }
    }

    fun removeEmoji(emojiWrapper: EmojiWrapper?) {
        emojiWrapper?.let { w ->
            var message = items
                .filterIsInstance<MessageDelegateItem>()
                .firstOrNull { message -> message.id == w.messageId }
            message?.let { m ->
                val model = (message.content() as MessageModel)
                val reaction =
                    model.reactions.first { reaction -> reaction.code == emojiWrapper.emojiCode }
                reaction.owners.remove(Const.myId)
                val position = items.indexOf(m)
                if (reaction.owners.isEmpty()) {
                    model.reactions.remove(reaction)
                }
                adapter.notifyItemChanged(position)
            }
        }
    }

}
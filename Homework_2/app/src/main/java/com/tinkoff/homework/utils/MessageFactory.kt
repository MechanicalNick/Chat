package com.tinkoff.homework.utils

import android.text.Editable
import com.tinkoff.homework.data.domain.DateModel
import com.tinkoff.homework.data.domain.EmojiWrapper
import com.tinkoff.homework.data.domain.MessageModel
import com.tinkoff.homework.data.domain.Reaction
import com.tinkoff.homework.utils.adapter.DelegatesAdapter
import com.tinkoff.homework.utils.adapter.date.DateDelegateItem
import com.tinkoff.homework.utils.adapter.message.CompanionMessageDelegateItem
import com.tinkoff.homework.utils.adapter.message.MessageDelegateItem
import com.tinkoff.homework.utils.adapter.message.MyMessageDelegateItem
import java.time.LocalDate

class MessageFactory(
    private val adapter: DelegatesAdapter,
    messages: List<MessageModel>,
    private val myId: Long
) {
    private val items: MutableList<DelegateItem> = mutableListOf()
    private var lastDate: LocalDate = LocalDate.now()

    init {
        if (messages.isNotEmpty()) {
            lastDate = messages
                .minOf { message -> message.date }
            val groups = messages
                .sortedBy { message -> message.date }
                .groupBy { message -> message.date }
            val dates = mutableListOf<DateModel>()

            groups.entries.forEachIndexed { index, group ->
                val dateModel = DateModel(index + 1L, group.key)
                dates.add(dateModel)
                items.add(DateDelegateItem(dateModel.id, dateModel))
                group.value.forEach {
                    val item = if (it.senderId == myId) MyMessageDelegateItem(it.id, it)
                        else CompanionMessageDelegateItem(it.id, it)
                    items.add(item)
                }
            }

            adapter.submitList(items)
        }
    }

    fun addText(text: Editable?){
        text.let {
            val count = items.count()
            val id = count + 1L
            val now = LocalDate.now()

            if(lastDate != now){
                val newDateItem = DateDelegateItem(id, DateModel(id, now))
                items.add(newDateItem)
                lastDate = now
            }

            val item = MyMessageDelegateItem(
                id,
                MessageModel(
                    id, myId, "", it.toString(),
                    LocalDate.now(), "", mutableListOf()
                )
            )
            items.add(item)
            adapter.notifyItemInserted(count)
        }
    }

    fun addEmoji(emojiWrapper: EmojiWrapper?) {
        emojiWrapper?.let { w ->
            var message = items
                .filterIsInstance<MessageDelegateItem>()
                .firstOrNull { message -> message.id == w.messageId }
            message?.let { m ->
                val model = (message.content() as MessageModel)
                if (model.reactions.any { r ->
                        r.emojiCode == w.emojiCode &&
                                r.userId == Const.myId
                    })
                    return
                model.reactions.add(
                    Reaction(
                        emojiWrapper.emojiCode,
                        emojiWrapper.emojiName,
                        Const.myId
                    )
                )

                val position = items.indexOf(m)
                val messageModel = MessageModel(
                    model.id, model.senderId, model.senderFullName,
                    model.text, model.date, model.avatarUrl, model.reactions
                )
                val item = if (model.senderId == Const.myId) MyMessageDelegateItem(
                    model.id,
                    messageModel
                ) else CompanionMessageDelegateItem(
                    model.id,
                    messageModel
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
                    model.reactions.firstOrNull { reaction ->
                        reaction.emojiCode == emojiWrapper.emojiCode &&
                                reaction.userId == Const.myId
                    } ?: return
                model.reactions.remove(reaction)
                val position = items.indexOf(m)
                adapter.notifyItemChanged(position)
            }
        }
    }
    fun getCount() : Int = items.count()
}
package com.tinkoff.homework.screen

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import com.tinkoff.homework.R
import com.tinkoff.homework.view.KFlexbox
import com.tinkoff.homework.presentation.view.fragment.ChatFragment
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher

class ChatScreen : KScreen<ChatScreen>() {
    override val layoutId: Int = R.layout.chart_fragment
    override val viewClass: Class<*> = ChatFragment::class.java

    val recycler = KRecyclerView({ withId(R.id.recycler) }, {
        itemType(::CompanionMessageItem)
        itemType(::MyMessageItem)
        itemType(::DateMessageItem)
    })

    val shimmer = KView { withId(R.id.shimmer) }
    val errorText = KTextView { withId(R.id.errorText) }
    val retryButton = KTextView { withId(R.id.retryButton) }

    class MyMessageItem(parent: Matcher<View>) : KRecyclerItem<MyMessageItem>(parent) {
        val myMessage = KTextView(parent) { withId(R.id.myTextMessage) }
        val myFlexbox = KFlexbox (parent) { withId(R.id.myFlexbox) }
        val bottomRecyclerView = KRecyclerView({ withId(R.id.bottomRecyclerView) }, {
            itemType(::ReactionItem)
        })
    }

    class CompanionMessageItem(parent: Matcher<View>) : KRecyclerItem<CompanionMessageItem>(parent) {
        val companionAvatar = KImageView(parent) { withId(R.id.companionAvatarView) }
        val companionMessage = KTextView(parent) { withId(R.id.companionTextMessage) }
        val companionFlexbox = KFlexbox(parent) { withId(R.id.companionFlexbox) }
    }

    class DateMessageItem(parent: Matcher<View>) : KRecyclerItem<DateMessageItem>(parent) {
        val chip = KTextView(parent) { withId(R.id.date) }
    }

    class ReactionItem(parent: Matcher<View>) : KRecyclerItem<ReactionItem>(parent) {
        val reaction = KTextView (parent) { withId(R.id.emojiId) }
    }
}
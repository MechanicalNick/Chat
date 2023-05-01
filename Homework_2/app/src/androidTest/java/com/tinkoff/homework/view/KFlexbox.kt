package com.tinkoff.homework.view

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.test.espresso.DataInteraction
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.tinkoff.homework.view.customview.ReactionView
import com.tinkoff.homework.view.viewgroup.FlexboxLayout
import io.github.kakaocup.kakao.common.actions.BaseActions
import io.github.kakaocup.kakao.common.assertions.BaseAssertions
import io.github.kakaocup.kakao.common.builders.ViewBuilder
import io.github.kakaocup.kakao.common.matchers.ChildCountMatcher
import io.github.kakaocup.kakao.common.views.KBaseView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

class KFlexbox : KBaseView<KFlexbox>, FlexboxActions, FlexboxAssertions {
    constructor(function: ViewBuilder.() -> Unit) : super(function)
    constructor(parent: Matcher<View>, function: ViewBuilder.() -> Unit) : super(parent, function)
    constructor(parent: DataInteraction, function: ViewBuilder.() -> Unit) : super(parent, function)
}

interface FlexboxActions : BaseActions {
    fun clickToReaction(reaction: String) {
        view.perform(object : ViewAction {
            override fun getDescription() = "Clicks a reaction: $reaction"

            override fun getConstraints() = ViewMatchers.isAssignableFrom(ViewGroup::class.java)

            override fun perform(uiController: UiController?, view: View?) {
                if (view is FlexboxLayout) {
                    for (children in view.children.filterIsInstance<ReactionView>()) {
                        if (children.getText() == reaction) {
                            children.performClick()
                        }
                    }
                }
            }
        })
    }
}

interface FlexboxAssertions : BaseAssertions {
    fun hasReaction(reaction: String) {
        view.check(ViewAssertions.matches(reactionMatcher(reaction)))
    }

    fun hasSize(size: Int) {
        view.check(ViewAssertions.matches(ChildCountMatcher(size)))
    }

    fun reactionMatcher(text: String): TypeSafeMatcher<View> {
        return object : TypeSafeMatcher<View>() {
            public override fun matchesSafely(view: View): Boolean {
                return (view as FlexboxLayout)
                    .children
                    .filterIsInstance<ReactionView>()
                    .any{it.getText() == text}
            }
            override fun describeTo(description: Description) {
                description.appendText("reaction must be $text")
            }
        }
    }
}


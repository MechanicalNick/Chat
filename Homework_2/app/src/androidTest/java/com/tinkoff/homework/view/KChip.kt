package com.tinkoff.homework.view

import android.view.View
import androidx.test.espresso.DataInteraction
import androidx.test.espresso.assertion.ViewAssertions
import com.google.android.material.chip.Chip
import io.github.kakaocup.kakao.common.actions.BaseActions
import io.github.kakaocup.kakao.common.assertions.BaseAssertions
import io.github.kakaocup.kakao.common.builders.ViewBuilder
import io.github.kakaocup.kakao.common.views.KBaseView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

class KChip : KBaseView<KChip>, ChipActions, ChipAssertions {
    constructor(function: ViewBuilder.() -> Unit) : super(function)
    constructor(parent: Matcher<View>, function: ViewBuilder.() -> Unit) : super(parent, function)
    constructor(parent: DataInteraction, function: ViewBuilder.() -> Unit) : super(parent, function)
}

interface ChipActions : BaseActions {

}

interface ChipAssertions : BaseAssertions {
    fun hasText(text: String) {
        view.check(ViewAssertions.matches(textMatcher(text)))
    }

    fun textMatcher(text: String): TypeSafeMatcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("chip must be $text")
            }

            override fun matchesSafely(item: View?): Boolean {
                return (item as Chip).text == text
            }
        }
    }
}
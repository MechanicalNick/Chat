package com.tinkoff.homework.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tinkoff.homework.R
import com.tinkoff.homework.data.EmojiResources
import com.tinkoff.homework.data.EmojiWrapper
import com.tinkoff.homework.databinding.BottomSheetDialogLayoutBinding
import com.tinkoff.homework.utils.BottomSheetDialogAdapter
import com.tinkoff.homework.viewmodel.MainViewModel

class BottomFragment : BottomSheetDialogFragment() {
    val viewModel: MainViewModel by activityViewModels()

    private lateinit var binding: BottomSheetDialogLayoutBinding
    private var messageId: Int = -1
    private val emojiCount = 100

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        messageId = requireArguments().getInt("modelId")
        binding = BottomSheetDialogLayoutBinding.bind(
            inflater.inflate(
                R.layout.bottom_sheet_dialog_layout,
                container
            )
        )
        val resources = EmojiResources()
        val list = resources.emojiSet.take(emojiCount)
        val adapter = BottomSheetDialogAdapter(list, this::applyEmoji)
        binding.bottomRecyclerView.adapter = adapter

        return binding.root
    }

    override fun getTheme() = R.style.CustomBottomSheetDialogTheme

    private fun applyEmoji(code: Int){
        viewModel.addEmoji.value = EmojiWrapper(code, messageId)
        dismiss()
    }
}
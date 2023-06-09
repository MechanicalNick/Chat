package com.tinkoff.homework.presentation.view.adapter.people

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tinkoff.homework.R
import com.tinkoff.homework.databinding.PeopleItemBinding
import com.tinkoff.homework.domain.data.People
import com.tinkoff.homework.domain.data.Status
import com.tinkoff.homework.presentation.view.DelegateItem
import com.tinkoff.homework.presentation.view.ToProfileRouter
import com.tinkoff.homework.presentation.view.adapter.AdapterDelegate

class PeopleDelegate(private val router: ToProfileRouter) : AdapterDelegate {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        ViewHolder(
            PeopleItemBinding.inflate(
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
        (holder as ViewHolder).bind(item.content() as People)
    }

    override fun isOfViewType(item: DelegateItem): Boolean = item is PeopleDelegateItem

    inner class ViewHolder(
        private val binding: PeopleItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(model: People) {
            with(binding) {
                binding.userAvatar.let {
                    Glide.with(binding.root)
                        .load(model.avatarUrl)
                        .circleCrop()
                        .placeholder(R.mipmap.ic_launcher_round)
                        .error(R.drawable.error_placeholder)
                        .into(it)
                }
                binding.userName.text = model.name
                binding.userEmail.text = model.email
                val id = when (model.status) {
                    Status.Online -> R.drawable.circle_online
                    Status.Idle -> R.drawable.circle_idle
                    Status.Offline -> R.drawable.circle_offline
                }
                binding.userStatus.background = ResourcesCompat
                    .getDrawable(binding.root.context.resources, id, binding.root.context.theme)

                binding.root.setOnClickListener {
                    router.goToProfile(model.userId)
                }
            }
        }
    }
}
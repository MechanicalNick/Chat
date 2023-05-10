package com.tinkoff.homework.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tinkoff.homework.R
import com.tinkoff.homework.databinding.PeopleItemBinding
import com.tinkoff.homework.domain.data.People
import com.tinkoff.homework.domain.data.Status
import com.tinkoff.homework.navigation.ToProfileRouter

class PeopleAdapter(private val router: ToProfileRouter) :
    RecyclerView.Adapter<PeopleAdapter.ViewHolder>() {
    val peoples: MutableList<People> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            PeopleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            router
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(peoples[position])

    override fun getItemCount(): Int = peoples.size

    class ViewHolder(
        _binding: PeopleItemBinding,
        private val router: ToProfileRouter
    ) : RecyclerView.ViewHolder(_binding.root) {

        var binding: PeopleItemBinding = PeopleItemBinding.bind(itemView)

        fun bind(model: People) {
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
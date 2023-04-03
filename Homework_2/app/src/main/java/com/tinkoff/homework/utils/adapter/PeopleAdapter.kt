package com.tinkoff.homework.utils.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.tinkoff.homework.R
import com.tinkoff.homework.data.Status
import com.tinkoff.homework.data.domain.People
import com.tinkoff.homework.databinding.PeopleItemBinding

class PeopleAdapter(): RecyclerView.Adapter<PeopleAdapter.ViewHolder>()  {
    val peoples: MutableList<People> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(PeopleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(peoples[position])

    override fun getItemCount(): Int = peoples.size

    class ViewHolder(_binding: PeopleItemBinding) : RecyclerView.ViewHolder(_binding.root) {

        var binding: PeopleItemBinding = PeopleItemBinding.bind(itemView)

        fun bind(model: People) {
            binding.userAvatar.let {
                Glide.with(binding.root)
                    .load(model.avatarUrl)
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
                .getDrawable(binding.root.context.resources, id, null)
        }
    }
}
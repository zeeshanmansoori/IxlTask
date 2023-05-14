package com.example.ixltask.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ixltask.data.local.model.UserEntity
import com.example.ixltask.databinding.ItemHomeBinding

class HomeAdapter(private val onItemClick: (UserEntity) -> Unit) :
    ListAdapter<UserEntity, HomeAdapter.HomeViewHolder>(diffUtil) {

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<UserEntity>() {
            override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean =
                oldItem == newItem

        }
    }

    inner class HomeViewHolder(private val binding: ItemHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onItemClick.invoke(getItem(adapterPosition))
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(data: UserEntity) = binding.run {
            userIdTv.text = "UserID : ${data.id}"
            userNameTv.text = "Name : ${data.firstName} ${data.lastName}"
            phoneNumberTv.text = "Gender : ${data.phoneNo}"
            dateOfBirthTv.text = "Gender : ${data.dob}"
            genderTv.text = "Gender : ${data.gender}"
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
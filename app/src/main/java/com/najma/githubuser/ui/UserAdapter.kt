package com.najma.githubuser.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.najma.githubuser.data.response.ListUser
import com.najma.githubuser.databinding.ItemUserBinding
import com.najma.githubuser.ui.detail.DetailActivity

class UserAdapter(private val listUser: List<ListUser>) :
    RecyclerView.Adapter<UserAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val (login, avatarUrl) = listUser[position]
        Glide.with(holder.itemView.context)
            .load(avatarUrl)
            .circleCrop()
            .into(holder.binding.imgAva)
        holder.binding.tvUname.text = login
        holder.itemView.setOnClickListener{
            val goToDetailActivity = Intent(holder.itemView.context, DetailActivity::class.java)
            goToDetailActivity.putExtra(DetailActivity.EXTRA_USERNAME, login)
            holder.itemView.context.startActivity(goToDetailActivity)
        }
    }
    override fun getItemCount(): Int = listUser.size

    class MyViewHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)

}








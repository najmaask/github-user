package com.najma.githubuser.ui.favorite

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.najma.githubuser.data.database.UserEntity
import com.najma.githubuser.databinding.ItemUserBinding
import com.najma.githubuser.ui.detail.DetailActivity

class FavoriteAdapter(private val listFavorite: List<UserEntity>) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val (login, avatarUrl) = listFavorite[position]
        Glide.with(holder.itemView.context)
            .load(avatarUrl)
            .circleCrop()
            .into(holder.binding.imgAva)
        holder.binding.tvUname.text = login
        holder.itemView.setOnClickListener {
            val goToDetailActivity = Intent(holder.itemView.context, DetailActivity::class.java)
            goToDetailActivity.putExtra(DetailActivity.EXTRA_USERNAME, login)
            holder.itemView.context.startActivity(goToDetailActivity)
        }
    }

    override fun getItemCount(): Int = listFavorite.size

    class FavoriteViewHolder(var binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)
}
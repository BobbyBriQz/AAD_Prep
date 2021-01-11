package com.bobby.aad_prep.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bobby.aad_prep.api.model.Repo
import com.bobby.aad_prep.databinding.RepoItemBinding

class RepositoryAdapter : RecyclerView.Adapter<RepositoryAdapter.ViewHolder>() {

    var repos : List<Repo> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RepoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.repoName.text = repos[position].name
    }

    fun setList(newList: List<Repo>){
        repos = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = repos.size

    class ViewHolder(binding: RepoItemBinding) : RecyclerView.ViewHolder(binding.root){
        val repoName: TextView = binding.repoName
    }
}
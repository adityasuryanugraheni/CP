package com.example.cp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cp.databinding.ItemPrivateBinding

class PrivateAdapter(
    private val list: List<PrivateModel>
) : RecyclerView.Adapter<PrivateAdapter.PrivateViewHolder>() {
    inner class PrivateViewHolder(val binding: ItemPrivateBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrivateViewHolder {
        val binding = ItemPrivateBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PrivateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PrivateViewHolder, position: Int) {
        val item = list[position]
        holder.binding.tvPrivateTitle.text = item.title
        holder.binding.tvPrivateContent.text = item.content
    }

    override fun getItemCount(): Int = list.size
}

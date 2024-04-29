package com.dev.diploma.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dev.diploma.databinding.MealElementBinding
import com.dev.diploma.domain.model.Product

class CurrentProductsAdapter :
    ListAdapter<Product, CurrentProductsAdapter.CurrentProductViewHolder>(CurrentProductDiffUtil) {
    inner class CurrentProductViewHolder(private val binding: MealElementBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                tvCategoriesElement.text = product.name
                tvCalories.text = product.calories
                Glide.with(itemView.context)
                    .load(product.photo_url)
                    .into(imgCategoriesElement)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentProductViewHolder {
        val binding = MealElementBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CurrentProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrentProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private object CurrentProductDiffUtil : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }
}
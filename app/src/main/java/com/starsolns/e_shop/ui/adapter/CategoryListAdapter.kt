package com.starsolns.e_shop.ui.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.starsolns.e_shop.databinding.CategoryListItemBinding
import com.starsolns.e_shop.ui.fragments.home.home.AddProductFragment

class CategoryListAdapter(
    private val activity: Activity,
    private val categoriesList: List<String>,
    private val selection: String
): RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val customBinding = CategoryListItemBinding.inflate(LayoutInflater.from(activity), parent, false)
        return ViewHolder(customBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentCategory = categoriesList[position]
        holder.category.text = currentCategory

        holder.itemView.setOnClickListener {
           val fragment = AddProductFragment()
            fragment.selectProductCategory(currentCategory, selection)
        }
    }

    override fun getItemCount(): Int {
        return categoriesList.size
    }

    inner class ViewHolder(binding: CategoryListItemBinding): RecyclerView.ViewHolder(binding.root){
        val category = binding.categoryName
    }

}
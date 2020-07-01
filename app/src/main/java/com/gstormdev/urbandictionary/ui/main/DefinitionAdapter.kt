package com.gstormdev.urbandictionary.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.gstormdev.urbandictionary.databinding.DefinitionListItemBinding
import com.gstormdev.urbandictionary.entity.Definition

class DefinitionAdapter : RecyclerView.Adapter<DefinitionViewHolder>() {
    private var definitions: List<Definition> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefinitionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return DefinitionViewHolder(DefinitionListItemBinding.inflate(layoutInflater, parent, false))
    }

    override fun getItemCount() = definitions.size

    override fun onBindViewHolder(holder: DefinitionViewHolder, position: Int) {
        holder.bind(definitions[position])
    }

    fun setData(definitions: List<Definition>) {
        val diffResult = DiffUtil.calculateDiff(DefinitionDiffCallback(definitions, this.definitions))
        diffResult.dispatchUpdatesTo(this)
        this.definitions = definitions
    }
}

class DefinitionViewHolder(private val binding: DefinitionListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(model: Definition) {
        binding.tvWord.text = model.word
        binding.tvDefinition.text = model.getDisplayFormattedDefinition()
        binding.tvThumbsUpCount.text = model.thumbs_up.toString()
        binding.tvThumbsDownCount.text = model.thumbs_down.toString()
    }
}

class DefinitionDiffCallback(private val newData: List<Definition>, private val data: List<Definition>) : DiffUtil.Callback() {
    override fun getOldListSize() = data.size

    override fun getNewListSize() = newData.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Check IDs to see if the items are the same
        return data[oldItemPosition].defid == newData[newItemPosition].defid
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Check deeper contents to see if the data within the item is the same
        return data[oldItemPosition] == newData[newItemPosition]
    }
}
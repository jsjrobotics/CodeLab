package com.lab49.assignment.taptosnap.ui.game

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lab49.assignment.taptosnap.databinding.CardGameBinding

class GameCard(view: View) : RecyclerView.ViewHolder(view){
    private var labelDisplay: TextView

    init {
        val binding = CardGameBinding.bind(itemView)
        labelDisplay = binding.labelDisplay
    }

    fun setData(label: String) {
        this.labelDisplay.text = label
    }

}

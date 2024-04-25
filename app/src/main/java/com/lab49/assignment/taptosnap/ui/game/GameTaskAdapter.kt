package com.lab49.assignment.taptosnap.ui.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lab49.assignment.taptosnap.R

class GameTaskAdapter(private val labels: Set<String>) : RecyclerView.Adapter<GameCard>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameCard {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_game, parent, false)
        return GameCard(view)
    }

    override fun getItemCount() = labels.size

    override fun onBindViewHolder(holder: GameCard, position: Int) {
        holder.setData(labels.elementAt(position))
    }

}

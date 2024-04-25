package com.lab49.assignment.taptosnap.ui.game

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lab49.assignment.taptosnap.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameTaskAdapter(private val labels: Set<String>) : RecyclerView.Adapter<GameCard>() {
    private val _clickedLabel = MutableStateFlow<String?>(null)
    val clickedLabel = _clickedLabel.asStateFlow()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameCard {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_game, parent, false)
        return GameCard(view)
    }

    override fun getItemCount() = labels.size

    override fun onBindViewHolder(holder: GameCard, position: Int) {
        val label = labels.elementAt(position)
        val onClickAction = buildClickAction(label)
        holder.setData(label, onClickAction)
    }

    private fun buildClickAction(label: String): View.OnClickListener {
        return View.OnClickListener { _clickedLabel.value = label }
    }
}

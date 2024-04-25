package com.lab49.assignment.taptosnap.ui.game

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.lab49.assignment.taptosnap.R
import com.lab49.assignment.taptosnap.dataStructures.GameCardState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class GameTaskAdapter(private val lifecycleScope: LifecycleCoroutineScope,
                      labels: Set<String>) : RecyclerView.Adapter<GameCard>() {
    private val cardStates : List<GameCardState> = buildDataList(labels)

    private val _clickedLabel = MutableSharedFlow<String>()
    val clickedLabel = _clickedLabel.asSharedFlow()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameCard {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_game, parent, false)
        return GameCard(view)
    }

    override fun getItemCount() = cardStates.size

    override fun onBindViewHolder(holder: GameCard, position: Int) {
        val cardState = cardStates[position]
        val onClickAction = buildClickAction(cardState)
        holder.setClickListener(onClickAction)
        holder.setLabel(cardState.label)
        holder.setImage(cardState.imageUri)
        holder.setState(cardState.validState)
    }

    private fun buildClickAction(cardState: GameCardState): View.OnClickListener {
        return View.OnClickListener {
            lifecycleScope.launch {
                _clickedLabel.emit(cardState.label)
            }
        }
    }

    private fun buildDataList(labels: Set<String>): List<GameCardState> {
        return labels.map { GameCardState(it) }
    }

    fun dataSetUpdated(gameUpdate: GameCardState) {
        val updateIndex = cardStates.indexOfFirst { it.label == gameUpdate.label }
        val localState = cardStates[updateIndex]
        gameUpdate.imageUri?.let { localState.imageUri = it }
        gameUpdate.validationState?.let { localState.validationState = it }
        gameUpdate.validState?.let { localState.validState = it }
        notifyItemChanged(updateIndex)
    }
}

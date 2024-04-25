package com.lab49.assignment.taptosnap.ui.game

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.RecyclerView
import com.lab49.assignment.taptosnap.R
import com.lab49.assignment.taptosnap.dataStructures.GameCardState
import com.lab49.assignment.taptosnap.dataStructures.GameCardValidationState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class GameTaskAdapter(private val lifecycleScope: LifecycleCoroutineScope) : RecyclerView.Adapter<GameCard>() {
    private val cardStates : MutableList<GameCardState> = mutableListOf()

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
        holder.setValidState(cardState.validState)
        holder.setValidationState(cardState.validationState)
    }

    private fun buildClickAction(cardState: GameCardState): View.OnClickListener {
        return View.OnClickListener {
            lifecycleScope.launch {
                _clickedLabel.emit(cardState.label)
            }
        }
    }

    fun dataSetUpdated(updates: List<GameCardState>) {
        updates.forEach { gameUpdate ->
            val updateIndex = cardStates.indexOfFirst { it.label == gameUpdate.label }
            if (updateIndex == -1) {
                cardStates.add(gameUpdate)
                notifyItemInserted(cardStates.size - 1)
            } else {
                cardStates[updateIndex] = gameUpdate
                notifyItemChanged(updateIndex)
            }
        }
    }

    fun clearProgressIndicators() {
        cardStates.forEachIndexed { index, gameCardState ->
            if (gameCardState.validationState == GameCardValidationState.VALIDATING) {
                gameCardState.validationState = GameCardValidationState.NOT_VALIDATING
                notifyItemChanged(index)
            }
        }

    }
}

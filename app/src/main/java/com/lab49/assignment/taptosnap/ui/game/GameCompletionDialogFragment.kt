package com.lab49.assignment.taptosnap.ui.game

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.navigation.fragment.findNavController
import com.lab49.assignment.taptosnap.R

class GameCompletionDialogFragment : AppCompatDialogFragment() {
    private var gameWon = false
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val message = if (gameWon) {
            R.string.game_won
        } else {
            R.string.better_luck
        }
        val builder = AlertDialog.Builder(requireContext(), R.style.Theme_TapToSnap_AlertDialog)
        builder.setTitle(R.string.game_over)
            .setMessage(message)
            .setPositiveButton(
                R.string.retry_permission
            ) { dialogInterface, _ ->
                dialogInterface.dismiss()
                findNavController().navigate(R.id.action_gameFragment_to_splashFragment)
            }.setNegativeButton(R.string.exit) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }
        return builder.create()
    }

    fun setWinState(gameWon: Boolean) {
        this.gameWon = gameWon
    }
}

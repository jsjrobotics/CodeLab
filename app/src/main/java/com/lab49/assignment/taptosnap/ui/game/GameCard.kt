package com.lab49.assignment.taptosnap.ui.game

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.lab49.assignment.taptosnap.R
import com.lab49.assignment.taptosnap.dataStructures.GameCardValidState
import com.lab49.assignment.taptosnap.databinding.CardGameBinding


class GameCard(view: View) : RecyclerView.ViewHolder(view){
    private var container: ConstraintLayout
    private var cameraImage: ImageView
    private var labelDisplay: TextView

    init {
        val binding = CardGameBinding.bind(itemView)
        container = binding.content
        labelDisplay = binding.labelDisplay
        cameraImage = binding.cameraImage
    }

    fun setState(state: GameCardValidState?) {
        val drawableResource = when(state) {
            GameCardValidState.NO_STATE, null -> R.drawable.button_no_state
            GameCardValidState.INVALID -> R.drawable.button_error_state
            GameCardValidState.VALID -> R.drawable.button_success_state
        }
        val drawable = AppCompatResources.getDrawable(itemView.context, drawableResource)
        container.background = drawable

    }
    fun setLabel(label: String) {
        this.labelDisplay.text = label
    }

    fun setClickListener(onClick: OnClickListener) {
        itemView.setOnClickListener(onClick)
    }

    fun setImage(source: Uri?) {
        source?.let {
            val contentResolver = itemView.context.contentResolver
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
            cameraImage.setImageBitmap(bitmap)
            cameraImage.scaleType = ImageView.ScaleType.FIT_XY
        }

    }

}

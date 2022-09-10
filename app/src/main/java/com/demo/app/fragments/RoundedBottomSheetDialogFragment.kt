package com.demo.app.fragments

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.demo.app.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


open class RoundedBottomSheetDialogFragment: BottomSheetDialogFragment(){
    var buttonLayoutParams: ConstraintLayout.LayoutParams? = null
    var collapsedMargin = 0 //Button margin in collapsed state
    var buttonHeight = 0
    var expandedHeight = 0 //Height of bottom sheet in expanded state

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    fun setupRatio(bottomSheetDialog: BottomSheetDialog, btn: TextView, recyclerView: RecyclerView,
    activity: Activity) {
        val bottomSheet = bottomSheetDialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            ?: return

        //Retrieve button parameters
        buttonLayoutParams = btn.layoutParams as ConstraintLayout.LayoutParams?

        //Retrieve bottom sheet parameters
        BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_COLLAPSED
        val bottomSheetLayoutParams = bottomSheet.layoutParams
        bottomSheetLayoutParams.height = getBottomSheetDialogDefaultHeight(activity)
        expandedHeight = bottomSheetLayoutParams.height
        val peekHeight =
            (expandedHeight / 1.3).toInt() //Peek height to 70% of expanded height (Change based on your view)

        //Setup bottom sheet
        bottomSheet.layoutParams = bottomSheetLayoutParams
        BottomSheetBehavior.from(bottomSheet).skipCollapsed = false
        BottomSheetBehavior.from(bottomSheet).peekHeight = peekHeight
        BottomSheetBehavior.from(bottomSheet).isHideable = true

        //Calculate button margin from top
        buttonHeight =
            btn.height + 40 //How tall is the button + experimental distance from bottom (Change based on your view)
        collapsedMargin = peekHeight - buttonHeight //Button margin in bottom sheet collapsed state
        buttonLayoutParams!!.topMargin = collapsedMargin
        btn.layoutParams = buttonLayoutParams

        //OPTIONAL - Setting up recyclerview margins
        val recyclerLayoutParams =
            recyclerView.layoutParams as ConstraintLayout.LayoutParams
        val k =
            (buttonHeight - 60) / buttonHeight.toFloat() //60 is amount that you want to be hidden behind button
        recyclerLayoutParams.bottomMargin =
            (k * buttonHeight).toInt() //Recyclerview bottom margin (from button)
        recyclerView.layoutParams = recyclerLayoutParams
    }

    private fun getBottomSheetDialogDefaultHeight(activity: Activity): Int {
        return getWindowHeight(activity) * 90 / 100
    }

    //Calculates window height for fullscreen use
    private fun getWindowHeight(activity: Activity): Int {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }
}
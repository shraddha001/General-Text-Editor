package com.ex.core.utils

import android.app.Activity
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.PopupWindow

class KeyboardUtils (private val mActivity: Activity) : PopupWindow(
    mActivity
), ViewTreeObserver.OnGlobalLayoutListener {
    private val rootView: View = View(mActivity)
    private var listener: SoftKeyboardToggleListener? = null
    private var heightMax = 0 // Record the maximum height of the pop content area
    fun init(): KeyboardUtils {
        if (!isShowing) {
            val view = mActivity.window.decorView
            // Delay loading popup window, if not, error will be reported
            view.post { showAtLocation(view, Gravity.NO_GRAVITY, 0, 0) }
        }
        return this
    }

    fun setListener(listener: SoftKeyboardToggleListener): KeyboardUtils {
        this.listener = listener
        return this
    }

    override fun onGlobalLayout() {
        val rect = Rect()
        rootView.getWindowVisibleDisplayFrame(rect)
        if (rect.bottom > heightMax) {
            heightMax = rect.bottom
        }

        // The difference between the two is the height of the keyboard
        val keyboardHeight = heightMax - rect.bottom
        if (listener != null) {
            listener!!.onToggleSoftKeyboard(keyboardHeight > 100, keyboardHeight)
        }
    }

    interface SoftKeyboardToggleListener {
        fun onToggleSoftKeyboard(isVisible: Boolean, keyboardHeight: Int)
    }

    init {
        // Basic configuration
        contentView = rootView

        // Monitor global Layout changes
        rootView.viewTreeObserver.addOnGlobalLayoutListener(this)
        setBackgroundDrawable(ColorDrawable(0))

        // Set width to 0 and height to full screen
        width = 0
        height = ViewGroup.LayoutParams.MATCH_PARENT

        // Set keyboard pop-up mode
        softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        inputMethodMode = INPUT_METHOD_NEEDED
    }
}

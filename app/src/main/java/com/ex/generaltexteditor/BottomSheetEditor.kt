package com.ex.generaltexteditor

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.ex.core.RichEditor
import com.ex.core.utils.KeyboardUtils
import com.ex.generaltexteditor.databinding.FragmentBottomSheetEditorBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

typealias OnSubmit = (text: String) -> Unit

class BottomSheetEditor(private val oldText: String) : BottomSheetDialogFragment() {
    private var _binding: FragmentBottomSheetEditorBinding? = null
    private val binding get() = _binding!!
    var isItalic = false
    var isBold = false
    var isList = false
    var onSubmit: OnSubmit? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        object :
            BottomSheetDialog(requireContext(), R.style.Widget_ex_BottomSheetEditorTheme) {}.apply {
            setOnShowListener {
                val bottomSheet =
                    findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
                bottomSheet.setBackgroundResource(android.R.color.transparent)
                val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
                bottomSheetBehavior.peekHeight = bottomSheet.height * 50
                bottomSheetBehavior.skipCollapsed = true
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBottomSheetEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        @Suppress("DEPRECATION")
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        binding.editor.setOnInitialLoadListener {
            if (it) setEditor()
        }
        addKeyboardToggleListener()
        // showKeyboard()
        bottomSheetBehavior()
        checkSaveState()
        setEditorState()
        if (oldText.isBlank()) binding.btnPositive.isEnabled = false

        binding.ivBold.setOnClickListener {
            isBold = !isBold
            if (isBold) binding.ivBold.setColorFilter(
                Color.GRAY, PorterDuff.Mode.SRC_ATOP
            )
            else binding.ivBold.setColorFilter(
                Color.BLACK, PorterDuff.Mode.SRC_ATOP
            )
            Log.d(javaClass.name,"______________$isBold")
            binding.editor.setBold()
        }
        binding.ivItalic.setOnClickListener {
            isItalic = !isItalic
            if (isItalic) binding.ivItalic.setColorFilter(
                Color.GRAY, PorterDuff.Mode.SRC_ATOP
            )
            else binding.ivItalic.setColorFilter(
                Color.BLACK, PorterDuff.Mode.SRC_ATOP
            )

            binding.editor.setItalic()

        }
        binding.ivList.setOnClickListener {
            isList = !isList
            if (isList) binding.ivList.setColorFilter(
               Color.GRAY, PorterDuff.Mode.SRC_ATOP
            )
            else binding.ivList.setColorFilter(
                Color.BLACK, PorterDuff.Mode.SRC_ATOP
            )
            binding.editor.setBullets()
        }
        val touchOutsideView = dialog?.window?.decorView?.findViewById<View>(
            com.google.android.material.R.id.touch_outside
        )
        touchOutsideView?.setOnClickListener {
            binding.mainLayout.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(), R.anim.slide_down
                )
            )
            Handler(Looper.getMainLooper()).postDelayed({ dismiss() }, 200)
        }
        binding.mainLayout.startAnimation(
            AnimationUtils.loadAnimation(
                requireContext(), R.anim.slide_up
            )
        )
        binding.btnNegative.setOnClickListener {
            binding.mainLayout.startAnimation(
                AnimationUtils.loadAnimation(
                    requireContext(), R.anim.slide_down
                )
            )
            Handler(Looper.getMainLooper()).postDelayed({ dismiss() }, 200)
        }

        binding.btnPositive.setOnClickListener {
            onSubmit?.invoke(binding.editor.editorHtml.trim())
            dismiss()
        }
    }


    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        requireActivity().window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        )
    }

    private fun checkSaveState() {
        binding.editor.setOnTextChangeListener { text ->
            val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY).toString()
            } else {
                Html.fromHtml(text)
            }
            binding.btnPositive.isEnabled = result?.trim()?.isNotBlank() == true
            when {
                result.trim().isBlank() && text.isBlank() -> binding.inputHint.isVisible = true
                result.isBlank() && text.equals("<br>") -> binding.inputHint.isVisible = true
                text.equals("<br>") -> binding.inputHint.isVisible = false
                else -> binding.inputHint.isVisible = false
            }
        }
    }

    private fun bottomSheetBehavior() {
        binding.ll.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
            binding.scrollView.scrollTo(0, binding.ll.bottom)
        }
    }

    private fun addKeyboardToggleListener() {
        KeyboardUtils(requireActivity()).init()
            .setListener(object : KeyboardUtils.SoftKeyboardToggleListener {
                override fun onToggleSoftKeyboard(isVisible: Boolean, keyboardHeight: Int) {
                    if (isVisible) {
                        try {
                            bottomSheetBehavior()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }
            })
    }

    private fun setEditor() {
        binding.editor.setEditorHeight(5)
        var originalString = oldText
        if (originalString.contains("\u0020") && originalString.contains("\n")) {
            originalString = originalString.replace("\u0020", "&ensp;")
            originalString = originalString.replace("\n", "<br />")
        }
        binding.inputHint.isVisible = originalString.isBlank()
        binding.editor.loadHtml(originalString)
        binding.editor.setEditorFontSize(16)
        binding.editor.setEditorFontColor(Color.BLACK)
        binding.editor.setPadding(12, 12, 12, 12)
        binding.editor.setEditorBackgroundColor(Color.TRANSPARENT)
    }

    private fun setEditorState() {
        binding.editor.setOnDecorationChangeListener { _, types ->
            types.apply {
                this?.forEach { type ->
                    when (type) {
                        RichEditor.Type.BOLD -> {
                            isBold = true
                            binding.ivBold.post {
                                binding.ivBold.setColorFilter(
                                    Color.GRAY,
                                    PorterDuff.Mode.SRC_ATOP
                                )
                            }
                        }
                        RichEditor.Type.ITALIC -> {
                            isItalic = true
                            binding.ivItalic.post {
                                binding.ivItalic.setColorFilter(
                                    Color.GRAY,
                                    PorterDuff.Mode.SRC_ATOP
                                )
                            }
                        }
                        RichEditor.Type.ORDEREDLIST, RichEditor.Type.UNORDEREDLIST -> {
                            isList = true
                            binding.ivList.post {
                                binding.ivList.setColorFilter(
                                    Color.GRAY,
                                    PorterDuff.Mode.SRC_ATOP
                                )
                            }
                        }
                        else -> {
                            resetState()
                        }
                    }
                }
            }
        }
    }

    private fun resetState() {
        binding.ivBold.setColorFilter(
            Color.BLACK, PorterDuff.Mode.SRC_ATOP
        )
        binding.ivItalic.setColorFilter(
            Color.BLACK, PorterDuff.Mode.SRC_ATOP
        )
        binding.ivList.setColorFilter(
            Color.BLACK, PorterDuff.Mode.SRC_ATOP
        )
    }

    companion object {
        fun newInstance(oldText: String) = BottomSheetEditor(oldText)
    }
}
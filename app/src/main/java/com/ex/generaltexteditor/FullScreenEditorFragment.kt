package com.ex.generaltexteditor

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ex.core.RichEditor
import com.ex.generaltexteditor.databinding.FragmentFullScreenEditorBinding


class FullScreenEditorFragment : Fragment() {
    private var _binding: FragmentFullScreenEditorBinding? = null
    private val binding get() = _binding!!
    var isItalic = false
    var isBold = false
    var isList = false
    var isNumList = false
    var isUnderline = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFullScreenEditorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editor.setPadding(12, 12, 12, 12)
        binding.editor.setEditorBackgroundColor(Color.TRANSPARENT)

        binding.editor.setEditorHeight(getDeviceHeight())
        setEditorState()
        binding.ivBold.setOnClickListener {
            isBold = !isBold
            if (isBold) binding.ivBold.setColorFilter(
                Color.GRAY, PorterDuff.Mode.SRC_ATOP
            )
            else binding.ivBold.setColorFilter(
                Color.BLACK, PorterDuff.Mode.SRC_ATOP
            )
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
        binding.ivUndo.setOnClickListener {
            binding.editor.undo()
        }
        binding.ivRedo.setOnClickListener {
            binding.editor.redo()
        }
        binding.ivUnderline.setOnClickListener {
            isUnderline = !isUnderline
            if (isUnderline) binding.ivUnderline.setColorFilter(
                Color.GRAY, PorterDuff.Mode.SRC_ATOP
            )
            else binding.ivUnderline.setColorFilter(
                Color.BLACK, PorterDuff.Mode.SRC_ATOP
            )
            binding.editor.setUnderline()
        }
        binding.ivNumList.setOnClickListener {
            isNumList = !isNumList
            if (isNumList) binding.ivNumList.setColorFilter(
                Color.GRAY, PorterDuff.Mode.SRC_ATOP
            )
            else binding.ivNumList.setColorFilter(
                Color.BLACK, PorterDuff.Mode.SRC_ATOP
            )
            binding.editor.setNumbers()
        }
    }

    private fun getDeviceHeight(): Int {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    private fun setEditorState() {
        binding.editor.setOnDecorationChangeListener { _, types ->
            types.apply {
                this?.forEach { type ->
                    Log.d(javaClass.name, "_____________$type")
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
                        RichEditor.Type.UNORDEREDLIST -> {
                            isList = true
                            binding.ivList.post {
                                binding.ivList.setColorFilter(
                                    Color.GRAY,
                                    PorterDuff.Mode.SRC_ATOP
                                )
                            }
                        }
                        RichEditor.Type.UNDERLINE -> {
                            isUnderline = true
                            binding.ivUnderline.post {
                                binding.ivUnderline.setColorFilter(
                                    Color.GRAY,
                                    PorterDuff.Mode.SRC_ATOP
                                )
                            }
                        }
                        RichEditor.Type.ORDEREDLIST -> {
                            isNumList = true
                            binding.ivNumList.post {
                                binding.ivNumList.setColorFilter(
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
        binding.ivUnderline.setColorFilter(
            Color.BLACK, PorterDuff.Mode.SRC_ATOP
        )
        binding.ivNumList.setColorFilter(
            Color.BLACK, PorterDuff.Mode.SRC_ATOP
        )
    }
}
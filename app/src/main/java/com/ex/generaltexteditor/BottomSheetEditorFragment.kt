package com.ex.generaltexteditor

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ex.generaltexteditor.databinding.FragmentBottomSheetEditorMainBinding

class BottomSheetEditorFragment : Fragment() {
    private var _binding: FragmentBottomSheetEditorMainBinding? = null
    private val binding get() = _binding!!
    lateinit var bottomSheetEditor: BottomSheetEditor
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBottomSheetEditorMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.editor.setPadding(12, 12, 12, 12)
        binding.editor.setBackgroundColor(Color.TRANSPARENT)
        bottomSheetEditor = BottomSheetEditor(
            if (binding.editor.editorHtml != null) binding.editor.editorHtml
            else ""
        )
        binding.editor.setOnTouchListener { _, _ -> true }
        binding.btnEdit.setOnClickListener {
            resetObject()
            bottomSheetEditor.onSubmit = {
                binding.editor.loadHtml(setContentWithoutBlankBottom(it))
            }
            bottomSheetEditor.showNow(childFragmentManager, null)
        }
    }

    private fun setContentWithoutBlankBottom(content: String): String {
        var originalString = content
        if (!originalString.startsWith("<div>") && !originalString.endsWith("</div>")) {
                if (originalString.endsWith("</i></b>") || originalString.endsWith("</b></i>")) {
                    originalString =   originalString.substring(0, originalString.length - 8)
                } else if (originalString.endsWith("<br>")) {
                    originalString =   originalString.substring(0, originalString.length - 4)
                }
            while (originalString.endsWith("<br>")) {
                originalString = originalString.substring(0, originalString.length - 4)
            }
        } else {
            if (originalString.endsWith(
                    "<div><font face=\"roboto_medium, roboto_regular\"><b><br></b></font>"
                ) || originalString.endsWith(
                    "<div><font face=\"roboto_medium, roboto_regular\"><b><i><br></i></b></font></div>"
                ) || originalString.endsWith(
                    "<div><font face=\"roboto_medium, roboto_regular\"><b><br></b></font></div>"
                )|| originalString.endsWith(
                    "<div><font face=\"roboto_medium, roboto_regular\"></font></div>")
            ) {
                originalString =
                    originalString.replace("<font face=\"roboto_medium, roboto_regular\">", "")
                        .replace("</font>", "")
            }
            if (originalString.endsWith("<div><i style=\"\"><br></i></div>") ||
                originalString.endsWith("<div><i style=\"\"><b><br></b></i></div>")
            ) {
                originalString = originalString.replace(
                    "<div><i style=\"\"><br></i></div>",
                    ""
                ).replace("<div><i style=\"\"><b><br></b></i></div>", "")
            }
            if (originalString.endsWith("<div><br></div>")) {
                originalString = originalString.replace("<div><br></div>", "")
            }
            if (originalString.endsWith("<div><b><br></b></div>") ||
                originalString.endsWith("<div><i><br></i></div>")
            ) {
                originalString = originalString.replace("<div><b><br></b></div>", "")
                    .replace("<div><i><br></i></div>", "")
            }
            if (originalString.endsWith("<div><b><i><br></i></b></div>") ||
                originalString.endsWith("<div><i><b><br></b></i></div>")
            ) {
                originalString = originalString.replace("<div><b><i><br></i></b></div>", "")
                    .replace("<div><i><b><br></b></i></div>", "")
            }
        }
        Log.d(javaClass.name, "__________________$originalString")
        Log.d(javaClass.name, "__________________$content ___****************************")
        return originalString.trim()
    }

    private fun resetObject() {
        bottomSheetEditor = BottomSheetEditor(
            if (binding.editor.editorHtml != null) binding.editor.editorHtml
            else ""
        )
    }
}
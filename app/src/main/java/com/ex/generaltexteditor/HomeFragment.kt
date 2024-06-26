package com.ex.generaltexteditor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.ex.generaltexteditor.databinding.FragmentBottomSheetEditorMainBinding
import com.ex.generaltexteditor.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBottomSheetEditor.setOnClickListener {
            findNavController().navigate(R.id.bottomSheetEditor)
        }

        binding.btnFullScreenEditor.setOnClickListener {
            findNavController().navigate(R.id.fullScreenEditor)
        }

        binding.btnEditorHtml.setOnClickListener {
            findNavController().navigate(R.id.editorWithHtml)
        }
    }
}
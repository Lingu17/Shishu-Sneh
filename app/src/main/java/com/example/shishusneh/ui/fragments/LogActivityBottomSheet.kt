package com.example.shishusneh.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shishusneh.databinding.BottomSheetLogActivityBinding
import com.example.shishusneh.ui.utils.VoiceFeedbackManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LogActivityBottomSheet(private val onSelect: (String) -> Unit) : BottomSheetDialogFragment() {
    private var _binding: BottomSheetLogActivityBinding? = null
    private val binding get() = _binding!!
    private lateinit var voiceManager: VoiceFeedbackManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomSheetLogActivityBinding.inflate(inflater, container, false)
        voiceManager = VoiceFeedbackManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        voiceManager.speak("Select activity to log: Feeding, Sleep, Growth, or Diaper")

        binding.btnLogFeeding.setOnClickListener { onSelect("Feeding"); dismiss() }
        binding.btnLogSleep.setOnClickListener { onSelect("Sleep"); dismiss() }
        binding.btnLogGrowth.setOnClickListener { onSelect("Growth"); dismiss() }
        binding.btnLogDiaper.setOnClickListener { onSelect("Diaper"); dismiss() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        voiceManager.shutdown()
        _binding = null
    }
}

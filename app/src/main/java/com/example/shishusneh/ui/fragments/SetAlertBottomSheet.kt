package com.example.shishusneh.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shishusneh.databinding.BottomSheetSetAlertBinding
import com.example.shishusneh.ui.utils.VoiceFeedbackManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SetAlertBottomSheet(private val onIntervalSelected: (Int) -> Unit) : BottomSheetDialogFragment() {
    private var _binding: BottomSheetSetAlertBinding? = null
    private val binding get() = _binding!!
    private lateinit var voiceManager: VoiceFeedbackManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomSheetSetAlertBinding.inflate(inflater, container, false)
        voiceManager = VoiceFeedbackManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        voiceManager.speak("Select feeding reminder interval")

        binding.btn2Hours.setOnClickListener { onIntervalSelected(2); dismiss() }
        binding.btn3Hours.setOnClickListener { onIntervalSelected(3); dismiss() }
        binding.btn4Hours.setOnClickListener { onIntervalSelected(4); dismiss() }
        binding.btnDisable.setOnClickListener { onIntervalSelected(0); dismiss() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        voiceManager.shutdown()
        _binding = null
    }
}

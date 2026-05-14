package com.example.shishusneh.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shishusneh.databinding.FragmentMilestonesBinding
import com.example.shishusneh.ui.adapters.MilestoneAdapter
import com.example.shishusneh.viewmodel.MainViewModel
import com.example.shishusneh.ui.utils.VoiceFeedbackManager
import kotlinx.coroutines.launch

class MilestonesFragment : Fragment() {

    private var _binding: FragmentMilestonesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels({ requireActivity() })
    private lateinit var voiceManager: VoiceFeedbackManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMilestonesBinding.inflate(inflater, container, false)
        voiceManager = VoiceFeedbackManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = MilestoneAdapter(
            onCheckChanged = { milestone ->
                viewModel.updateMilestone(milestone)
                if (milestone.isAchieved) {
                    voiceManager.speak("Milestone completed")
                }
            },
            onMilestoneClick = { milestone ->
                voiceManager.speak("Typical age: ${milestone.monthNumber} months. ${milestone.description}")
                android.widget.Toast.makeText(context, milestone.description, android.widget.Toast.LENGTH_SHORT).show()
            }
        )
        binding.rvMilestones.adapter = adapter
        binding.rvMilestones.layoutManager = LinearLayoutManager(context)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.milestones.collect { milestones ->
                    adapter.submitList(milestones)
                    val achieved = milestones.count { it.isAchieved }
                    val total = milestones.size
                    binding.tvProgressText.text = "$achieved / $total"
                    if (total > 0) {
                        binding.progressIndicator.progress = (achieved * 100) / total
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        voiceManager.shutdown()
        _binding = null
    }
}
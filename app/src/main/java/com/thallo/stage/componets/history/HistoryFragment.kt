package com.thallo.stage.componets.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.thallo.stage.R
import com.thallo.stage.componets.bookmark.BookmarkAdapter
import com.thallo.stage.database.history.HistoryViewModel
import com.thallo.stage.databinding.FragmentHistoryBinding
import com.thallo.stage.session.createSession
import com.thallo.stage.utils.GroupUtils

class HistoryFragment : Fragment() {
    lateinit var binding:FragmentHistoryBinding
    lateinit var historyViewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        historyViewModel= ViewModelProvider(this)[HistoryViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentHistoryBinding.inflate(LayoutInflater.from(requireContext()))
        var historyAdapter= HistoryAdapter()
        binding.historyRecyclerview.adapter=historyAdapter
        binding.historyRecyclerview.layoutManager = LinearLayoutManager(context)
        historyViewModel.allHistoriesLive?.observe(requireActivity()){
            historyAdapter.submitList(it)

        }

        historyAdapter.select= object : HistoryAdapter.Select {
            override fun onSelect(url: String) {
                createSession(url,requireActivity())
            }

        }

        return binding.root
    }


}
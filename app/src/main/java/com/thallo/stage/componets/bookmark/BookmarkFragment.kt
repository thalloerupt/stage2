package com.thallo.stage.componets.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.thallo.stage.R
import com.thallo.stage.componets.bookmark.sync.SyncBookmarkAdapter
import com.thallo.stage.database.bookmark.BookmarkViewModel
import com.thallo.stage.databinding.FragmentBookmarkBinding
import com.thallo.stage.session.createSession

/**
 * A simple [Fragment] subclass.
 * Use the [BookmarkFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookmarkFragment : Fragment() {
    lateinit var binding:FragmentBookmarkBinding
    private lateinit var bookmarkViewModel: BookmarkViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bookmarkViewModel = ViewModelProvider(requireActivity()).get<BookmarkViewModel>(BookmarkViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentBookmarkBinding.inflate(LayoutInflater.from(requireContext()))

        var bookmarkAdapter= BookmarkAdapter()
        binding.bookmarkRecyclerview.adapter=bookmarkAdapter
        binding.bookmarkRecyclerview.layoutManager = LinearLayoutManager(context)
        bookmarkViewModel.allBookmarksLive?.observe(requireActivity()){
            bookmarkAdapter.submitList(it)
        }
        bookmarkAdapter.select= object : BookmarkAdapter.Select {
            override fun onSelect(url: String) {
                createSession(url,requireActivity())
            }

        }
        return binding.root
    }


}
package com.thallo.stage.broswer.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.thallo.stage.R
import com.thallo.stage.broswer.bookmark.sync.SyncBookmarkAdapter
import com.thallo.stage.database.bookmark.Bookmark
import com.thallo.stage.database.bookmark.BookmarkViewModel
import com.thallo.stage.databinding.FragmentBookmarkBinding
import com.thallo.stage.session.createSession
import com.thallo.stage.utils.GroupUtils

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
            val groupUtils= it?.let { it1 -> GroupUtils(it1) }
            if (groupUtils != null) {
                bookmarkAdapter.submitList(groupUtils.groupBookmark())
            }
        }

        bookmarkAdapter.select= object : BookmarkAdapter.Select {
            override fun onSelect(url: String) {
                createSession(url,requireActivity())
            }

        }
        bookmarkAdapter.longClick= object : BookmarkAdapter.LongClick {
            override fun onLongClick(bean: Bookmark) {
                bookmarkViewModel.deleteBookmarks(bean)
            }


        }

        return binding.root
    }


}
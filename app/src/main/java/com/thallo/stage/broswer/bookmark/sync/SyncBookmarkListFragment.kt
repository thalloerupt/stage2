package com.thallo.stage.broswer.bookmark.sync

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.thallo.stage.R
import com.thallo.stage.databinding.FragmentSyncBookmarkListBinding
import com.thallo.stage.session.createSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mozilla.components.browser.storage.sync.PlacesBookmarksStorage
import mozilla.components.concept.storage.BookmarkNode
import mozilla.components.service.fxa.SyncEngine
import mozilla.components.service.fxa.sync.GlobalSyncableStoreProvider


class SyncBookmarkListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var guid: String? = null
    private lateinit var bookmarkNodes:ArrayList<BookmarkNode>
    lateinit var binding:FragmentSyncBookmarkListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        guid = arguments?.getString("guid")
        bookmarkNodes=ArrayList<BookmarkNode>()
        binding = FragmentSyncBookmarkListBinding.inflate(LayoutInflater.from(requireContext()))

        //guid?.let { Log.d("arguments?.getString", it) }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var syncBookmarkItemAdapter= SyncBookmarkItemAdapter()
        val bookmarksStorage = lazy {
            PlacesBookmarksStorage(this.requireContext())
        }

        binding.constraintLayout16.setOnClickListener {
            findNavController().navigate(R.id.action_syncBookmarkListFragment_to_syncBookmarkFragment)
        }

        GlobalSyncableStoreProvider.configureStore(SyncEngine.Bookmarks to bookmarksStorage)
        lifecycleScope.launch {
            binding.recyclerView5.adapter=syncBookmarkItemAdapter
            binding.recyclerView5.layoutManager = LinearLayoutManager(context)
            syncBookmarkItemAdapter.select= object : SyncBookmarkItemAdapter.Select {
                override fun onSelect(url: String) {
                    createSession(url,requireActivity())

                }

            }



            syncBookmarkItemAdapter.submitList(withContext(Dispatchers.IO) {
                val bookmarksRoot =
                    guid?.let { bookmarksStorage.value?.getTree(it, recursive = true) }
                if (bookmarksRoot == null) {
                    bookmarkNodes
                } else {
                    var bookmarksRootAndChildren = "BOOKMARKS\n"
                    fun addTreeNode(node: BookmarkNode, depth: Int) {
                        Log.d("BookmarkNode: ", node.type.name)
                        if(node.type.name == "ITEM")
                            bookmarkNodes.add(node)
                        node.children?.forEach {
                            addTreeNode(it, depth + 1)
                        }
                    }
                    addTreeNode(bookmarksRoot, 0)
                    bookmarkNodes

                }
            }.toList())

        }

        return binding.root
    }


}
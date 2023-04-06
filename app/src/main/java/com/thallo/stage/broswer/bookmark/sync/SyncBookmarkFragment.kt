package com.thallo.stage.broswer.bookmark.sync

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.thallo.stage.R
import com.thallo.stage.componets.HomeLivedata
import com.thallo.stage.databinding.FragmentSyncBookmarkBinding
import com.thallo.stage.session.GeckoViewModel
import com.thallo.stage.session.SeRuSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mozilla.components.browser.storage.sync.PlacesBookmarksStorage
import mozilla.components.concept.storage.BookmarkNode
import mozilla.components.service.fxa.SyncEngine
import mozilla.components.service.fxa.sync.GlobalSyncableStoreProvider
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession

/**
 * A simple [Fragment] subclass.
 * Use the [SyncBookmarkFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SyncBookmarkFragment : Fragment()  {

    lateinit var binding: FragmentSyncBookmarkBinding
    lateinit var bookmarkNodes:ArrayList<BookmarkNode>
    lateinit var geckoViewModel: GeckoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // GlobalSyncableStoreProvider.configureStore(SyncEngine.Bookmarks to bookmarksStorage)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bookmarkNodes=ArrayList<BookmarkNode>()

        val bookmarksStorage = lazy {
            PlacesBookmarksStorage(this.requireContext())
        }
        GlobalSyncableStoreProvider.configureStore(SyncEngine.Bookmarks to bookmarksStorage)
        binding= FragmentSyncBookmarkBinding.inflate(LayoutInflater.from(context))
        var bookmarkAdapter= SyncBookmarkFolderAdapter()
        geckoViewModel = activity?.let { ViewModelProvider(it)[GeckoViewModel::class.java] }!!

        lifecycleScope.launch {
            binding.syncBookmarkRecyclerView.adapter=bookmarkAdapter
            binding.syncBookmarkRecyclerView.layoutManager = LinearLayoutManager(context)
            bookmarkAdapter.select= object : SyncBookmarkFolderAdapter.Select {
                override fun onSelect(bean: BookmarkNode) {
                    //createSession(url)
                    val bundle = Bundle()
                    bundle.putString("guid", bean.guid)
                    findNavController().navigate(R.id.action_syncBookmarkFragment_to_syncBookmarkListFragment,bundle)
                }

            }



            bookmarkAdapter.submitList(withContext(Dispatchers.IO) {
                val bookmarksRoot =
                    bookmarksStorage.value?.getTree("root________", recursive = true)
                if (bookmarksRoot == null) {
                    bookmarkNodes
                } else {
                    var bookmarksRootAndChildren = "BOOKMARKS\n"
                    fun addTreeNode(node: BookmarkNode, depth: Int) {
                        Log.d("BookmarkNode: ", node.type.name)
                        if(node.type.name == "FOLDER")
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

    fun createSession(uri: String) {
        val session = GeckoSession()
        val sessionSettings = session.settings
        SeRuSettings(sessionSettings, requireActivity())

        context?.let { GeckoRuntime.getDefault(it) }?.let { session.open(it) }
        session.loadUri(uri)
        geckoViewModel.changeSearch(session)
        HomeLivedata.getInstance().Value(false)

    }
}
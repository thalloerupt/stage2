package com.thallo.stage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.thallo.stage.componets.BookmarkAdapter
import com.thallo.stage.componets.HomeLivedata
import com.thallo.stage.databinding.FragmentSyncBookmarkBinding
import com.thallo.stage.session.GeckoViewModel
import com.thallo.stage.session.SeRuSettings
import com.thallo.stage.utils.getSizeName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mozilla.components.browser.storage.sync.PlacesBookmarksStorage
import mozilla.components.concept.storage.BookmarkNode
import mozilla.components.service.fxa.SyncEngine
import mozilla.components.service.fxa.sync.GlobalSyncableStoreProvider
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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
        binding=FragmentSyncBookmarkBinding.inflate(LayoutInflater.from(context))
        var bookmarkAdapter= BookmarkAdapter()
        geckoViewModel = activity?.let { ViewModelProvider(it)[GeckoViewModel::class.java] }!!

        lifecycleScope.launch {
            binding.syncBookmarkRecyclerView.adapter=bookmarkAdapter
            binding.syncBookmarkRecyclerView.layoutManager = LinearLayoutManager(context)
            bookmarkAdapter.select= object : BookmarkAdapter.Select{
                override fun onSelect(url: String) {
                    createSession(url)
                }

            }

            bookmarkAdapter.submitList(withContext(Dispatchers.IO) {
                val bookmarksRoot = bookmarksStorage.value?.getTree("root________", recursive = true)
                if (bookmarksRoot == null) {
                    bookmarkNodes
                } else {
                    var bookmarksRootAndChildren = "BOOKMARKS\n"
                    fun addTreeNode(node: BookmarkNode, depth: Int) {
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
        if (context?.let { getSizeName(it) } =="large")
            SeRuSettings(sessionSettings, GeckoRuntime.getDefault(requireContext()).settings,true)
        else {
            context?.let { GeckoRuntime.getDefault(it).settings }?.let {
                SeRuSettings(sessionSettings,
                    it,false)
            }
        }
        context?.let { GeckoRuntime.getDefault(it) }?.let { session.open(it) }
        session.loadUri(uri)
        geckoViewModel.changeSearch(session)
        HomeLivedata.getInstance().Value(false)

    }
}
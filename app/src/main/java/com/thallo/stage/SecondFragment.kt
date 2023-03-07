package com.thallo.stage

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.thallo.stage.componets.HomeLivedata
import com.thallo.stage.databinding.FragmentSecondBinding
import com.thallo.stage.session.*
import com.thallo.stage.tab.AddTabLiveData
import com.thallo.stage.tab.DelegateListLiveData
import com.thallo.stage.tab.RemoveTabLiveData
import com.thallo.stage.utils.filePicker.FilePicker
import com.thallo.stage.utils.filePicker.PickUtils
import kotlinx.coroutines.launch
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession

/**
 * 2023.1.4创建，1.21除夕
 * 2023.2.11 19:10 正月廿一 记录
 * thallo
 **/
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    lateinit var session: GeckoSession
    lateinit var geckoViewModel: GeckoViewModel
    lateinit var delegate: ArrayList<SessionDelegate>
    lateinit var uri: Uri

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var active:Int=-1
    lateinit var filePicker: FilePicker
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        geckoViewModel = activity?.let { ViewModelProvider(it)[GeckoViewModel::class.java] }!!
        return binding.root

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var getContent = registerForActivityResult(ActivityResultContracts.OpenDocument()
        ) {
            filePicker.putUri(Uri.parse("file://" + it?.let { it1 ->
                PickUtils.getPath(requireContext(), it1)
            }))
        }

        filePicker=FilePicker(getContent,requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        delegate = ArrayList<SessionDelegate>()
        lifecycleScope.launch {
            geckoViewModel.data.collect { value: GeckoSession ->
                openSession(value)
                //Toast.makeText(context,"ok",Toast.LENGTH_SHORT).show()
            }
        }

        DelegateListLiveData.getInstance().observe(viewLifecycleOwner){
            delegate=it
        }
        RemoveTabLiveData.getInstance().observe(viewLifecycleOwner){
            Log.d("RemoveTabLiveData",""+delegate.size)
            if (delegate.size!=0) {
                delegate[it].close()
                delegate.removeAt(it)

                if (delegate.getOrNull(it) == null) {
                    if (delegate.getOrNull(it - 1) == null) HomeLivedata.getInstance().Value(true)
                    else DelegateLivedata.getInstance().Value(delegate[it - 1])
                } else
                    DelegateLivedata.getInstance().Value(delegate[it])

                DelegateListLiveData.getInstance().Value(delegate)
            }
        }



        DelegateLivedata.getInstance().observe(viewLifecycleOwner){
            for (i in delegate){
                if (it != i)
                    i.active=false
            }
            it.active=true
            active=delegate.indexOf(it)
            AddTabLiveData.getInstance().Value(active)
            binding.geckoview.session?.setActive(false)
            binding.geckoview.releaseSession()
            GeckoRuntime.getDefault(requireContext()).webExtensionController.setTabActive(it.session, true)
            binding.geckoview.setSession(it.session)
        }





    }





    fun openSession(session: GeckoSession) {
        binding.geckoview.releaseSession()
        val sessionDelegate: SessionDelegate? = activity?.let { SessionDelegate(it, session,filePicker) }
        if (sessionDelegate != null) {
            sessionDelegate.setpic = object : SessionDelegate.Setpic {
                override fun onSetPic() {
                    binding.geckoview.capturePixels().accept {
                        if (it != null) {
                            sessionDelegate.bitmap=it
                        }
                    }


                }

            }

        }
        if (delegate.size==0)
            sessionDelegate?.let { delegate.add(it) }
        else
            sessionDelegate?.let { delegate.add(active+1, it) }
        sessionDelegate?.let {DelegateLivedata.getInstance().Value(it) }
        DelegateListLiveData.getInstance().Value(delegate)
        binding.geckoview.setSession(session)
    }
    fun getPath(context: Context, uri: Uri): String? {
        if ("content".equals(uri.scheme, ignoreCase = true)) {
            val projection = arrayOf("_data")
            var cursor: Cursor? = null
            try {
                cursor = context.contentResolver.query(uri, projection, null, null, null)
                val column_index: Int? = cursor?.getColumnIndexOrThrow("_data")
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        return column_index?.let { cursor.getString(it) }
                    }
                }
            } catch (e: Exception) {
            }
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }

}
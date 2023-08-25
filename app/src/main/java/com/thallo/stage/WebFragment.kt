package com.thallo.stage

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.material.icons.materialIcon
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.thallo.stage.componets.HomeLivedata
import com.thallo.stage.databinding.FragmentSecondBinding
import com.thallo.stage.session.DelegateLivedata
import com.thallo.stage.session.GeckoViewModel
import com.thallo.stage.session.PrivacyFlow
import com.thallo.stage.session.SessionDelegate
import com.thallo.stage.tab.AddTabLiveData
import com.thallo.stage.tab.DelegateListLiveData
import com.thallo.stage.tab.RemoveTabLiveData
import com.thallo.stage.utils.filePicker.FilePicker
import com.thallo.stage.utils.filePicker.PickUtils.getPath
import com.thallo.stage.utils.filePicker.ResultContract
import kotlinx.coroutines.launch
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.WebRequestError
import kotlin.math.abs


/**
 * 2023.1.4创建，1.21除夕
 * 2023.2.11 19:10 正月廿一 记录
 * thallo
 **/
class WebFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    lateinit var session: GeckoSession
    lateinit var geckoViewModel: GeckoViewModel
    lateinit var privacyFlow: PrivacyFlow
    lateinit var delegate: ArrayList<SessionDelegate>
    lateinit var uri: Uri
    lateinit var sessiondelegate: SessionDelegate
    var mPosX:Float = 0f
    var mPosY:Float = 0f
    var mCurPosX:Float = 0f
    var mCurPosY:Float = 0f

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var active:Int=-1
    lateinit var filePicker: FilePicker
    var isPrivacy : Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        geckoViewModel = ViewModelProvider(requireActivity())[GeckoViewModel::class.java]
        privacyFlow = ViewModelProvider(requireActivity())[PrivacyFlow::class.java]
        return binding.root

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val launcher: ActivityResultLauncher<Boolean> =
            registerForActivityResult(ResultContract(), object : ActivityResultCallback<Intent?> {
                override fun onActivityResult(result: Intent?) {
                    if (result == null) {
                        return
                    }
                    val uri = result.data
                    //文件路径
                    val mFilePath = getPath(context!!, uri!!)
                    Log.d("ActivityResultLauncher",mFilePath)
                    filePicker.putUri(Uri.parse("file://$mFilePath"))
                }
            })


        filePicker=FilePicker(launcher,requireActivity())
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        delegate = ArrayList<SessionDelegate>()

        lifecycleScope.launch {
            privacyFlow
                .data
                .collect(){
                    v->isPrivacy=v
                Log.d("privacyFlow",""+v)

            }
        }
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
            sessiondelegate = it
        }







    }

    fun openSession(session: GeckoSession) {
        binding.geckoview.releaseSession()
        val sessionDelegate: SessionDelegate? = activity?.let { SessionDelegate(it, session,filePicker,isPrivacy) }
        if (sessionDelegate != null) {
            sessionDelegate.setpic = object : SessionDelegate.Setpic {
                override fun onSetPic() {
                    binding.geckoview.capturePixels().accept {
                        if (it != null) {
                            if (sessionDelegate.privacy)
                                sessionDelegate.bitmap= requireActivity().getDrawable(R.drawable.close_outline)?.toBitmap()!!
                            else
                                sessionDelegate.bitmap=it
                        }
                    }


                }

            }



            sessionDelegate.pageError = object : SessionDelegate.PageError {
                override fun onPageChange() {
                    binding.errorpage.visibility = View.GONE
                }
                override fun onPageError(
                    session: GeckoSession,
                    uri: String?,
                    error: WebRequestError
                ) {
                    binding.errorpage.visibility = View.VISIBLE
                    binding.errorCodeText.text = "Error:${error.code}"
                    binding.errorRetryButton.setOnClickListener { session.reload() }

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

    override fun onResume() {
        super.onResume()

    }


}
package com.thallo.stage

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.thallo.stage.componets.HomeLivedata
import com.thallo.stage.databinding.FragmentSecondBinding
import com.thallo.stage.fxa.Fxa
import com.thallo.stage.session.DelegateLivedata
import com.thallo.stage.session.GeckoViewModel
import com.thallo.stage.session.SessionDelegate
import com.thallo.stage.session.SessionViewModel
import com.thallo.stage.tab.AddTabLiveData
import com.thallo.stage.tab.DelegateListLiveData
import com.thallo.stage.tab.RemoveTabLiveData
import kotlinx.coroutines.launch
import mozilla.components.service.fxa.FxaAuthData
import mozilla.components.service.fxa.manager.FxaAccountManager
import mozilla.components.service.fxa.toAuthType
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoSessionSettings

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

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var active:Int=-1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        geckoViewModel = activity?.let { ViewModelProvider(it)[GeckoViewModel::class.java] }!!
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        delegate = ArrayList<SessionDelegate>()
        //createSession("https://www.baidu.com/")
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
        val sessionDelegate: SessionDelegate? = activity?.let { SessionDelegate(it, session) }
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


}
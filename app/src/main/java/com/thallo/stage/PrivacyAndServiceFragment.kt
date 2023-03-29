package com.thallo.stage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thallo.stage.databinding.FragmentPrivacyAndServiceBinding

class PrivacyAndServiceFragment : Fragment() {
    lateinit var fragmentPrivacyAndServiceBinding: FragmentPrivacyAndServiceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentPrivacyAndServiceBinding = FragmentPrivacyAndServiceBinding.inflate(LayoutInflater.from(context))
        fragmentPrivacyAndServiceBinding.webView2.loadUrl("file:///android_asset/privacy.txt")
        // Inflate the layout for this fragment
        return fragmentPrivacyAndServiceBinding.root
    }


}
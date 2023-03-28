package com.thallo.stage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.thallo.stage.componets.AddonsAdapter
import com.thallo.stage.databinding.FragmentAddonsManagerBinding
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.WebExtension
import org.mozilla.geckoview.WebExtensionController

class AddonsManagerFragment : Fragment() {
    lateinit var binding: FragmentAddonsManagerBinding
    private lateinit var SheetBehavior:BottomSheetBehavior<ConstraintLayout>
    lateinit var webExtensionController: WebExtensionController
    var adapter= AddonsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentAddonsManagerBinding.inflate(LayoutInflater.from(requireContext()))
        webExtensionController=GeckoRuntime.getDefault(requireContext()).webExtensionController
        binding.AddonsManagerRecycler.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        binding.AddonsManagerRecycler.adapter=adapter
        webExtensionController.list().accept {
            adapter.submitList(it)
        }
        if (binding.managerDrawer!=null) {
            SheetBehavior = BottomSheetBehavior.from(binding.managerDrawer as ConstraintLayout)
            SheetBehavior.peekHeight=0
        }
        adapter.select= object : AddonsAdapter.Select {
            override fun onSelect(bean: WebExtension) {
                openSheet(bean)
            }
        }
        binding.materialButton4.setOnClickListener {
            val intent = Intent(requireContext(),MainActivity::class.java)
            intent.data = Uri.parse("https://addons.mozilla.org/zh-CN/firefox/")
            intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
        return binding.root
    }
    fun openSheet(extension: WebExtension){
        var metadata:WebExtension.MetaData = extension.metaData
        metadata.icon.getBitmap(72).accept { binding.imageView7.setImageBitmap(it) }
        binding.textView12.text = metadata.name
        binding.textView18.text = metadata.creatorName
        binding.textView19.text = metadata.version
        binding.textView20.text = metadata.description
        SheetBehavior.state=BottomSheetBehavior.STATE_HALF_EXPANDED
        if (metadata.optionsPageUrl!=null)
            binding.button3.visibility=View.VISIBLE
        else
            binding.button3.visibility=View.GONE

        binding.button3.setOnClickListener {
            val intent = Intent(requireContext(),MainActivity::class.java)
            intent.data = Uri.parse(metadata.optionsPageUrl)
            intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)

        }
        binding.button2.setOnClickListener {
            webExtensionController.uninstall(extension).accept {
                webExtensionController.list().accept {
                adapter.submitList(it)
            }
            }
            SheetBehavior.state=BottomSheetBehavior.STATE_COLLAPSED

        }
    }


}
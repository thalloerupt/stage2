package com.thallo.stage.componets

import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.thallo.stage.MainActivity
import com.thallo.stage.R
import com.thallo.stage.databinding.PopupTabBinding
import com.thallo.stage.session.GeckoViewModel
import com.thallo.stage.session.SeRuSettings
import com.thallo.stage.tab.DelegateListLiveData
import com.thallo.stage.tab.TabListAdapter
import com.thallo.stage.utils.getSizeName
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession

class TabPopup {
    val context:MainActivity
    private val bottomSheetDialog: BottomSheetDialog
    private val binding:PopupTabBinding
    val  geckoViewModel: GeckoViewModel
    constructor(context: MainActivity) {
        this.context = context
        bottomSheetDialog = BottomSheetDialog(context,R.style.BottomSheetDialog )
        binding = PopupTabBinding.inflate(LayoutInflater.from(context))
        bottomSheetDialog.setContentView(binding.root)
        geckoViewModel= ViewModelProvider(context).get(GeckoViewModel::class.java)

    }
    fun show(){
        val adapter=TabListAdapter()
        binding.recyclerView.adapter=adapter
        adapter.select=object :TabListAdapter.Select{
            override fun onSelect() {
                bottomSheetDialog.dismiss()
            }

        }

        binding.recyclerView.layoutManager = GridLayoutManager(context, 2);
        DelegateListLiveData.getInstance().observe(context){
            adapter.submitList(it.toList())
        }
        binding.popAddButton.setOnClickListener {
            HomeLivedata.getInstance().Value(true)
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }
    fun createSession(uri: String) {
        val session = GeckoSession()
        val sessionSettings = session.settings
        if (getSizeName(context)=="large")
            SeRuSettings(sessionSettings, GeckoRuntime.getDefault(context).settings,true)
        else {
            SeRuSettings(sessionSettings, GeckoRuntime.getDefault(context).settings,false)
        }
        session.open(GeckoRuntime.getDefault(context) )
        session.loadUri(uri)
        geckoViewModel.changeSearch(session)

    }
}
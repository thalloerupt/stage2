package com.thallo.stage


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.webkit.URLUtil
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.sidesheet.SideSheetBehavior
import com.kongzue.dialogx.dialogs.PopTip
import com.kongzue.dialogx.interfaces.OnBindView
import com.thallo.stage.broswer.SearchEngine
import com.thallo.stage.broswer.dialog.SearchDialog
import com.thallo.stage.broswer.home.TipsAdapter
import com.thallo.stage.componets.BookmarkDialog
import com.thallo.stage.componets.CollectionAdapter
import com.thallo.stage.componets.HomeLivedata
import com.thallo.stage.componets.popup.MenuPopup
import com.thallo.stage.componets.popup.TabPopup
import com.thallo.stage.database.history.HistoryViewModel
import com.thallo.stage.databinding.ActivityMainBinding
import com.thallo.stage.databinding.PrivacyAgreementLayoutBinding
import com.thallo.stage.download.DownloadTaskLiveData
import com.thallo.stage.session.*
import com.thallo.stage.tab.AddTabLiveData
import com.thallo.stage.tab.DelegateListLiveData
import com.thallo.stage.tab.RemoveTabLiveData
import com.thallo.stage.tab.TabListAdapter
import com.thallo.stage.utils.FullScreen
import com.thallo.stage.utils.SoftKeyBoardListener
import com.thallo.stage.utils.SoftKeyBoardListener.OnSoftKeyBoardChangeListener
import com.thallo.stage.utils.StatusUtils
import com.thallo.stage.utils.getSizeName
import com.thallo.stage.webextension.WebextensionSession
import org.mozilla.geckoview.GeckoResult
import org.mozilla.geckoview.GeckoRuntime


/**
 * 2023.1.4创建，1.21除夕
 * 2023.2.11 19:10 正月廿一 记录
 * thallo
 **/
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var privacyAgreementLayoutBinding: PrivacyAgreementLayoutBinding
    var fragments = listOf<Fragment>(FirstFragment(),SecondFragment())
    private lateinit var geckoViewModel: GeckoViewModel
    var sessionDelegates=ArrayList<SessionDelegate>()
    private val adapter= TabListAdapter()
    var bottomSheetBehavior:BottomSheetBehavior<ConstraintLayout>?=null
    lateinit var sideSheetBehavior: SideSheetBehavior<ConstraintLayout>
    var isHome:Boolean = true
    lateinit var historyViewModel: HistoryViewModel
    var searching = ""

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        privacyAgreementLayoutBinding = PrivacyAgreementLayoutBinding.inflate(layoutInflater)
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        privacyAgreementLayoutBinding.materialButton14.setOnClickListener {
            setContentView(binding.root)
            prefs.edit().putBoolean("privacy1",true).commit()
        }
        privacyAgreementLayoutBinding.materialButton17.setOnClickListener {
            setContentView(binding.root)
            prefs.edit().putBoolean("privacy1",true).commit()
        }
        if (prefs.getBoolean("privacy1",false))
            setContentView(binding.root)
        else
            setContentView(privacyAgreementLayoutBinding.root)
        privacyAgreementLayoutBinding.webView.loadUrl("file:///android_asset/privacy.txt")

        StatusUtils.init(this)
        WebextensionSession(this)

        onBackPressedDispatcher.addCallback(this, onBackPress)
        geckoViewModel = ViewModelProvider(this)[GeckoViewModel::class.java]
        historyViewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
        if (binding.content.drawer!=null) {
            bottomSheetBehavior =
                BottomSheetBehavior.from(binding.content.drawer as ConstraintLayout)
            bottomSheetBehavior!!.peekHeight = 0
            bottomSheetBehavior!!.isDraggable = false
        }

        adapter.select=object :TabListAdapter.Select{ override fun onSelect() {} }

        binding.SearchText?.imeOptions = EditorInfo.IME_ACTION_SEARCH


        binding.materialButton13?.setOnClickListener {
            var searchDialog=SearchDialog(this)
            searchDialog.setOnDismissListener {
                when(SearchEngine(this)){
                    getString(com.thallo.stage.R.string.baidu) -> binding.materialButton13?.text =
                        getString(R.string.EngineTips,getString(R.string.Baidu))
                    getString(com.thallo.stage.R.string.google) -> binding.materialButton13?.text =
                        getString(R.string.EngineTips,getString(R.string.Google))
                    getString(com.thallo.stage.R.string.bing) -> binding.materialButton13?.text =
                        getString(R.string.EngineTips,getString(R.string.Bing))
                    getString(com.thallo.stage.R.string.sogou) -> binding.materialButton13?.text =
                        getString(R.string.EngineTips,getString(R.string.Sougou))
                }
                if (prefs.getBoolean("switch_diy",false))
                    binding.materialButton13?.text = getString(R.string.EngineTips,getString(R.string.diySearching))
            }
            searchDialog.show()

        }
        binding.SearchText?.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus){
                binding.bottomMotionLayout?.transitionToEnd()
                binding.constraintLayout10?.visibility =View.VISIBLE
                when(SearchEngine(this)){
                    getString(com.thallo.stage.R.string.baidu) -> binding.materialButton13?.text =
                        getString(R.string.EngineTips,getString(R.string.Baidu))
                    getString(com.thallo.stage.R.string.google) -> binding.materialButton13?.text =
                        getString(R.string.EngineTips,getString(R.string.Google))
                    getString(com.thallo.stage.R.string.bing) -> binding.materialButton13?.text =
                        getString(R.string.EngineTips,getString(R.string.Bing))
                    getString(com.thallo.stage.R.string.sogou) -> binding.materialButton13?.text =
                        getString(R.string.EngineTips,getString(R.string.Sougou))
                }
                if (prefs.getBoolean("switch_diy",false))
                    binding.materialButton13?.text = getString(R.string.EngineTips,getString(R.string.diySearching))
            }

            else{
                binding.bottomMotionLayout?.transitionToStart()
                binding.constraintLayout10?.visibility =View.GONE
                if (!isHome)
                    binding.SearchText?.setText(binding.user?.u)
            }
        }
        binding.materialButtonClear?.setOnClickListener { binding.SearchText?.setText("") }
        var tipsAdapter= TipsAdapter()
        binding.recyclerView4?.adapter =tipsAdapter
        binding.recyclerView4?.layoutManager = LinearLayoutManager(this)
        tipsAdapter.select = object : TipsAdapter.Select {
            override fun onSelect(url: String) {
                searching(url)
                val imm: InputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                // 隐藏软键盘
                imm.hideSoftInputFromWindow(
                    window.decorView.windowToken,
                    0
                )

            }

        }
        binding.SearchText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                var s1=s.toString().trim()
                if (s1!=""){
                    historyViewModel.findHistoriesWithMix(s1)?.observe(this@MainActivity){
                        tipsAdapter.submitList(it)
                    }
                }


            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.SearchText?.setOnKeyListener(View.OnKeyListener { _, i, keyEvent ->
            if (KeyEvent.KEYCODE_ENTER == i && keyEvent.action == KeyEvent.ACTION_DOWN) {
                var value= binding.SearchText!!.text.toString()
                searching(value)
                searching = value
            }

            false
        })
        binding.materialButtonEdit?.setOnClickListener { binding.SearchText?.setText(searching)   }
        binding.urlText?.setOnKeyListener(View.OnKeyListener { _, i, keyEvent ->
            if (KeyEvent.KEYCODE_ENTER == i && keyEvent.action == KeyEvent.ACTION_DOWN) {
                var value= binding.urlText!!.text.toString()
                searching(value)

            }
            false
        })
        binding.recyclerView2?.adapter =adapter
        binding.recyclerView2?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        binding.content.viewPager.adapter= CollectionAdapter(this,fragments)
        binding.content.viewPager.isUserInputEnabled=false
        binding.materialButtonMenu?.setOnClickListener { MenuPopup(this).show() }
        binding.materialButtonHome?.setOnClickListener { HomeLivedata.getInstance().Value(true) }
        binding.materialButtonTab?.setOnClickListener { TabPopup(this).show() }
        binding.addButton?.setOnClickListener { HomeLivedata.getInstance().Value(true) }
        binding.content.popupCloseButton?.setOnClickListener { bottomSheetBehavior?.state=BottomSheetBehavior.STATE_COLLAPSED }
        if (binding.content.viewPager.currentItem==1) {

            binding.reloadButtonL?.isClickable=false
            binding.forwardButtonL?.isClickable=false
            binding.backButtonL?.isClickable=false
        }else{
            binding.backButtonL?.setOnClickListener {
                if(binding.user?.canBack == true)
                    binding.user?.session?.goBack()
                else RemoveTabLiveData.getInstance().Value(sessionDelegates.indexOf(binding.user))
            }
            binding.forwardButtonL?.setOnClickListener {
                if(binding.user?.canForward == true)
                    binding.user?.session?.goForward()
            }
            binding.reloadButtonL?.setOnClickListener {
                binding.user?.session?.reload()
            }
        }
        binding.menuButton?.setOnClickListener {
            openMenu()
        }
        binding.addonsButton?.setOnClickListener {
            if(!isHome)
                BookmarkDialog(this, binding.user!!.mTitle, binding.user!!.u).show()
        }
        binding.content.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position==0)
                    binding.SearchText?.setText("")
            }

        })




        DelegateLivedata.getInstance().observe(this){
            binding.user=it
        }
        DelegateListLiveData.getInstance().observe(this){
            binding.SizeText?.setText(it.size.toString())
            adapter.submitList(it.toList())
            sessionDelegates=it
        }
        HomeLivedata.getInstance().observe(this){
            isHome=it
            if (it){
                binding.content.viewPager.currentItem=0
                binding.urlText?.setText("")
            }else {
                binding.content.viewPager.currentItem=1
                bottomSheetBehavior?.state  =BottomSheetBehavior.STATE_COLLAPSED
            }
        }
        AddTabLiveData.getInstance().observe(this){
            binding.recyclerView2?.smoothScrollToPosition(it)
        }
        DownloadTaskLiveData.getInstance().observe(this){
            PopTip.build()
                .setCustomView(object : OnBindView<PopTip?>(com.thallo.stage.R.layout.pop_mytip) {
                    override fun onBind(dialog: PopTip?, v: View) {
                        v.findViewById<TextView>(R.id.textView17).text = "正在下载"
                        v.findViewById<MaterialButton>(R.id.materialButton7).setOnClickListener {
                            var intent=Intent(this@MainActivity, HolderActivity::class.java)
                            intent.putExtra("Page","DOWNLOAD")
                            startActivity(intent)
                        }
                    }
                })
                .show()
        }

        if (getSizeName(this)=="large")
            FullScreen(this)

        val uri: Uri? = intent?.data
        if (uri != null) {
            createSession(uri.toString(),this)
        }



    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {

        return super.onCreateView(name, context, attrs)
    }

    private val onBackPress = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if(binding.user?.canBack == true)
                binding.user?.session?.goBack()
            else {
                if(sessionDelegates.indexOf(binding.user)!=-1)
                    RemoveTabLiveData.getInstance().Value(sessionDelegates.indexOf(binding.user))
                else {
                    finish()
                }
            }


        }
    }

    private fun openMenu() {
        bottomSheetBehavior?.state  = BottomSheetBehavior.STATE_EXPANDED
        var navController = findNavController(R.id.fragmentContainerView3)
        binding.content.navigationrail?.setupWithNavController(navController)
        binding.content.appbar?.setupWithNavController(
            navController,
            AppBarConfiguration(navController.graph)
        )
        if (isHome)
            binding.content.navigationrail?.headerView?.findViewById<FloatingActionButton>(R.id.floatingActionButton)?.visibility=View.GONE
        else
            binding.content.navigationrail?.headerView?.findViewById<FloatingActionButton>(R.id.floatingActionButton)?.visibility=View.VISIBLE

        binding.content.navigationrail?.headerView?.findViewById<FloatingActionButton>(R.id.floatingActionButton)
            ?.setOnClickListener {
                navController.navigate(R.id.addonsPopupFragment2)
            }


    }



    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val uri: Uri? = intent?.data
        if (uri != null) {
            createSession(uri.toString(),this)
        }
        GeckoRuntime.getDefault(this).activityDelegate= GeckoRuntime.ActivityDelegate {
            Log.d("test",uri.toString())
            GeckoResult.fromValue(Intent())
        }
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()
        SoftKeyBoardListener.setListener(this, object : OnSoftKeyBoardChangeListener {
            override fun keyBoardShow(height: Int) {
            }

            override fun keyBoardHide(height: Int) {
                binding.constraintLayout10?.visibility =View.GONE

                binding.bottomMotionLayout?.transitionToStart()
                binding.SearchText?.clearFocus()
            }
        })
    }

    fun searching(value:String){
        if (Patterns.WEB_URL.matcher(value).matches() || URLUtil.isValidUrl(value)) {
            if(binding.content.viewPager.currentItem==1)
                binding.user?.session?.loadUri(value)
            else
                createSession(value,this)

        } else {
            if(binding.content.viewPager.currentItem==1)
                binding.user?.session?.loadUri("${SearchEngine(this)}$value")
            else
                createSession("${SearchEngine(this)}$value",this)
        }
    }






}
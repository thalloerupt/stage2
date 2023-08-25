package com.thallo.stage.componets

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.kongzue.dialogx.dialogs.PopTip
import com.kongzue.dialogx.interfaces.OnBindView
import com.thallo.stage.R
import com.thallo.stage.databinding.DiaContextmenuBinding
import com.thallo.stage.download.DownloadTask
import com.thallo.stage.download.DownloadTaskLiveData
import com.thallo.stage.utils.UriUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.mozilla.geckoview.GeckoSession.ContentDelegate.ContextElement

class ContextMenuDialog(var context: FragmentActivity, element: ContextElement) :
    AlertDialog(context) {
    var binding: DiaContextmenuBinding
    var type: String? = null
    var downloadTasks=ArrayList<DownloadTask>()

    init {
        binding = DiaContextmenuBinding.inflate(LayoutInflater.from(context))
        Glide.with(context).load(element.srcUri).into(binding.imageView19)
        DownloadTaskLiveData.getInstance().observe(context){
             downloadTasks = it
        }
        binding.diaContextmenuDownloadButton.setOnClickListener(View.OnClickListener {
            PopTip.build()
                .setCustomView(object : OnBindView<PopTip?>(com.thallo.stage.R.layout.pop_mytip) {
                    override fun onBind(dialog: PopTip?, v: View) {
                        v.findViewById<TextView>(R.id.textView17).text = "网页希望下载文件"
                        v.findViewById<MaterialButton>(R.id.materialButton7).setOnClickListener {
                            context.lifecycleScope.launch {
                                var downloadTask = element.srcUri?.let { it1 ->
                                    DownloadTask(context, it1,
                                        withContext(Dispatchers.IO) {
                                            UriUtils.getFileName(it1)
                                        })
                                }
                                if (downloadTask != null) {
                                    downloadTask.open()
                                    downloadTasks.add(downloadTask)
                                    DownloadTaskLiveData.getInstance().Value(downloadTasks)
                                }
                                }
                        }
                    }
                })
                .show()
        })
        binding.diaContextmenuCopyButton.setOnClickListener(View.OnClickListener {
            copyToClipboard(context, element.srcUri)
            dismiss()
        })
        if (element.srcUri == null) binding.diaContextmenuOpenButton.setVisibility(View.GONE)
        binding.diaContextmenuOpenButton.setOnClickListener(View.OnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_VIEW
            if (element.type == ContextElement.TYPE_IMAGE) type =
                "image/*" else if (element.type == ContextElement.TYPE_VIDEO) type = "video/*"
            val uri = Uri.parse(element.srcUri)
            intent.setDataAndType(uri, type)
            context.startActivity(intent)
        })
        setView(binding.getRoot())
    }

    fun open() {
        window!!.setBackgroundDrawable(context.getDrawable(R.drawable.bg_dialog))
        show()
    }

    companion object {
        fun copyToClipboard(context: Context, content: String?) {
            // 从 API11 开始 android 推荐使用 android.content.ClipboardManager
            // 为了兼容低版本我们这里使用旧版的 android.text.ClipboardManager，虽然提示 deprecated，但不影响使用。
            val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            // 将文本内容放到系统剪贴板里。
            cm.text = content
            Toast.makeText(context, "已复制到剪切板", Toast.LENGTH_SHORT).show()
        }
    }
}
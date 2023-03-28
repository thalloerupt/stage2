package com.thallo.stage.componets.popup

import android.content.Context
import android.view.LayoutInflater
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.thallo.stage.R
import com.thallo.stage.broswer.dialog.AlertDialog
import com.thallo.stage.broswer.dialog.JsChoiceDialog
import com.thallo.stage.databinding.PopupAddonsBinding
import kotlinx.coroutines.launch
import org.mozilla.geckoview.GeckoResult
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.WebExtension

class AddonsPopup {
    val context: Context
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var binding: PopupAddonsBinding
    constructor(
        context: Context,
    ) {
        this.context = context
        bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialog )
        binding = PopupAddonsBinding.inflate(LayoutInflater.from(context))
        bottomSheetDialog.setContentView(binding.root)
        bottomSheetDialog.behavior.isDraggable=false
        binding.button.setOnClickListener { bottomSheetDialog.dismiss()}


    }
    fun show(session:GeckoSession, extension: WebExtension){
        session.open(GeckoRuntime.getDefault(context))
        session.promptDelegate = object : GeckoSession.PromptDelegate {
            override fun onChoicePrompt(
                session: GeckoSession,
                prompt: GeckoSession.PromptDelegate.ChoicePrompt
            ): GeckoResult<GeckoSession.PromptDelegate.PromptResponse>? {
                //prompt.
                val jsChoiceDialog =
                    JsChoiceDialog(
                        context,
                        prompt
                    )
                jsChoiceDialog.showDialog()
                return GeckoResult.fromValue(prompt.confirm(jsChoiceDialog.dialogResult.toString()))
            }

            override fun onAlertPrompt(
                session: GeckoSession,
                prompt: GeckoSession.PromptDelegate.AlertPrompt
            ): GeckoResult<GeckoSession.PromptDelegate.PromptResponse>? {
                val alertDialog =
                    AlertDialog(context, prompt)
                alertDialog.showDialog()
                return GeckoResult.fromValue(alertDialog.getDialogResult())
            }
        }
        context as LifecycleOwner
        context.lifecycleScope.launch {
            extension.metaData.icon.getBitmap(72).accept { binding.imageView5.setImageBitmap(it) }

            binding.textView.text=extension.metaData.name
        }
        binding.addonsView.setSession(session)
        bottomSheetDialog.show()
    }
}
package com.thallo.stage.broswer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import org.mozilla.geckoview.GeckoSession;

public class ConfirmDialog extends androidx.appcompat.app.AlertDialog {
    GeckoSession.PromptDelegate.PromptResponse dialogResult;
    Handler mHandler ;
    Context context;
    public ConfirmDialog(@NonNull Context context, GeckoSession.PromptDelegate.RepostConfirmPrompt repostConfirmPrompt) {
        super(context);
        this.context=context;
        onCreate(repostConfirmPrompt,context);
    }
    public void onCreate(GeckoSession.PromptDelegate.RepostConfirmPrompt repostConfirmPrompt, Context context) {
        setTitle(repostConfirmPrompt.title);
        //setMessage(repostConfirmPrompt.message);
        setButton(BUTTON_POSITIVE, "确认", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                endDialog(repostConfirmPrompt.dismiss());
            }
        });


    }
    public void endDialog(GeckoSession.PromptDelegate.PromptResponse result)
    {
        setDialogResult(result);
        super.dismiss();
        Message m = mHandler.obtainMessage();
        mHandler.sendMessage(m);
        Log.d("endDia",result+"");





    }
    @SuppressLint("HandlerLeak")
    public GeckoSession.PromptDelegate.PromptResponse showDialog()
    {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message mesg) {
                // process incoming messages here
                //super.handleMessage(msg);
                throw new RuntimeException();
            }
        };
        super.show();
        try {
            Looper.getMainLooper().loop();
        }
        catch(RuntimeException e2)
        {
        }
        return dialogResult;
    }

    public GeckoSession.PromptDelegate.PromptResponse getDialogResult() {
        return dialogResult;
    }

    public void setDialogResult(GeckoSession.PromptDelegate.PromptResponse dialogResult) {
        this.dialogResult = dialogResult;
    }
}

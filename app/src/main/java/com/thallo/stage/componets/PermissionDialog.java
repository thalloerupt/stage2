package com.thallo.stage.componets;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.thallo.stage.R;
import com.thallo.stage.databinding.DiaInstallBinding;

import org.mozilla.geckoview.WebExtension;

import java.util.Arrays;

public class PermissionDialog extends MaterialAlertDialogBuilder
{
    int dialogResult;
    Handler mHandler ;
    DiaInstallBinding binding;
    Activity activity;


    public PermissionDialog(Activity context, WebExtension webExtension)
    {

        super(context);
        onCreate(webExtension);
        this.activity=context;


    }
    public int getDialogResult()
    {
        return dialogResult;
    }
    public void setDialogResult(int dialogResult)
    {
        this.dialogResult = dialogResult;
    }
    /** Called when the activity is first created. */

    public void onCreate(WebExtension webExtension) {
        binding=DiaInstallBinding.inflate(LayoutInflater.from(getContext()));
        binding.textView23.setText("要添加"+webExtension.metaData.name+"吗？");
        binding.textView22.setText("需要以下权限：");
        setIcon(R.drawable.extension_puzzle);
        binding.diaPer.setText(Arrays.toString(webExtension.metaData.permissions)
                .replaceAll("\\[", "• ")
                .replaceAll(",","\n•")
                .replaceAll("\\]","")
                .replaceAll(getContext().getString(R.string.per_tabs), getContext().getString(R.string.per_tabs_cn))
                .replaceAll(getContext().getString(R.string.per_bookmarks), getContext().getString(R.string.per_bookmarks_cn))
                .replaceAll(getContext().getString(R.string.per_clipboardRead), getContext().getString(R.string.per_clipboardRead_cn))
                .replaceAll(getContext().getString(R.string.per_browserSettings), getContext().getString(R.string.per_browserSettings_cn))
                .replaceAll(getContext().getString(R.string.per_browsingData), getContext().getString(R.string.per_browsingData_cn))
                .replaceAll(getContext().getString(R.string.per_downloads), getContext().getString(R.string.per_downloads_cn))
                .replaceAll(getContext().getString(R.string.per_geolocation), getContext().getString(R.string.per_geolocation_cn))
                .replaceAll(getContext().getString(R.string.per_notifications), getContext().getString(R.string.per_notifications_cn))
        );



        setView(binding.getRoot());
        setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                endDialog(0);
            }
        });
        setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                endDialog(1);
            }
        });


    }

    public void endDialog(int result)
    {
        setDialogResult(result);
        Message m = mHandler.obtainMessage();
        mHandler.sendMessage(m);
    }

    @SuppressLint("HandlerLeak")
    public int showDialog()
    {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message mesg) {
                // process incoming messages here
                //super.handleMessage(msg);
                throw new RuntimeException();
            }
        };
        super.show().getWindow().setBackgroundDrawable(activity.getDrawable(R.drawable.bg_dialog));
        try {
            Looper.getMainLooper().loop();
        }
        catch(RuntimeException e2)
        {
        }
        return dialogResult;
    }

}
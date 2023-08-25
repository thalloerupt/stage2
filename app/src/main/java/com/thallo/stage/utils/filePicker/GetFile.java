package com.thallo.stage.utils.filePicker;

import android.app.Activity;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.fragment.app.FragmentActivity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class GetFile {
    Uri uri;
    Handler mHandler ;
    FilePicker filePicker;
    public GetFile(FragmentActivity activity,FilePicker filePicker){
        this.filePicker=filePicker;

        filePicker.putUriListener(new FilePicker.UriListener() {
            @Override
            public void UriGet(Uri uri) {
                close(uri);
            }
        });
    }
    public void open(FragmentActivity activity, @Nullable String[] mimeTypes){
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message mesg) {
                // process incoming messages here
                //super.handleMessage(msg);
                throw new RuntimeException();
            }
        };
        filePicker.open(activity,mimeTypes);
        try {
            Looper.getMainLooper().loop();
        }
        catch(RuntimeException e2)
        {
        }

    }
    public void close(Uri uri){
        setUri(uri);
        Message m = mHandler.obtainMessage();
        mHandler.sendMessage(m);
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }


}

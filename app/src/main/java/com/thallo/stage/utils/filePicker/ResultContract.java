package com.thallo.stage.utils.filePicker;

import android.content.Context;
import android.content.Intent;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ResultContract extends ActivityResultContract<Boolean, Intent> {
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Boolean input) {
        Intent intent = new Intent();
        intent.setType("*/*");//同时选择视频和图片
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        //  Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        return intent;
    }
    @Override
    public Intent parseResult(int resultCode, @Nullable Intent intent) {
        return intent;
    }
}


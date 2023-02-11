package com.thallo.stage.componets;

import android.content.Context;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class MyBottomSheetDialog extends BottomSheetDialog {
    private BottomSheetBehavior<FrameLayout> behavior;
    int mode;
    public MyBottomSheetDialog(@NonNull Context context, int theme, int mode) {
        super(context, theme);
        this.mode=mode;
    }


    @Override
    protected void onStart() {
        super.onStart();
        switch (mode){
            case 0:
                if (behavior != null && behavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;
            case 1:
                BottomSheetBehavior behavior = getBehavior();
                behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                break;
            case 2:
                behavior = getBehavior();
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
        }



        }

    }


package com.thallo.stage.broswer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyEditText extends androidx.appcompat.widget.AppCompatEditText {



    private Paint mPaint;
    private int mViewHeight = 0;
    private Rect mTextBound = new Rect();
    private LinearGradient mLinearGradient;

    public MyEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        mViewHeight = getMeasuredHeight();
        mPaint = getPaint();
        String mText = getText().toString();
        mPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
        mLinearGradient = new LinearGradient(0, 0, 0, mViewHeight,new int[]{0xFF8EDA4D, 0xFF4EB855}, null, Shader.TileMode.REPEAT);
        mPaint.setShader(mLinearGradient);
        canvas.drawText(mText, getMeasuredWidth() / 2 - mTextBound.width() / 2, getMeasuredHeight() / 2 + mTextBound.height() / 2, mPaint);
    }

}


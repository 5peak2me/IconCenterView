package com.jinlin.custom.iconcenterview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by J!nl!n on 2015/1/26.
 */
public class IconCenterEditText extends EditText implements View.OnFocusChangeListener, View.OnKeyListener {
    private static final String TAG = IconCenterEditText.class.getSimpleName();
    /**
     * 是否是默认图标再左边的样式
     */
    private boolean isLeft = false;
    /**
     * 是否点击软键盘搜索
     */
    private boolean pressSearch = false;
    /**
     * 软键盘搜索键监听
     */
    private OnSearchClickListener listener;

    public void setOnSearchClickListener(OnSearchClickListener listener) {
        this.listener = listener;
    }

    public IconCenterEditText(Context context) {
        this(context, null);
        init();
    }

    public IconCenterEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
        init();
    }

    public IconCenterEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOnFocusChangeListener(this);
        setOnKeyListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isLeft) { // 如果是默认样式，则直接绘制
            super.onDraw(canvas);
        } else { // 如果不是默认样式，需要将图标绘制在中间
            Drawable[] drawables = getCompoundDrawables();
            Drawable drawableLeft = drawables[0];
            Drawable drawableRight = drawables[2];
            translate(drawableLeft, canvas);
            translate(drawableRight, canvas);
//            if (drawableLeft != null) {
//                float textWidth = getPaint().measureText(getHint().toString());
//                int drawablePadding = getCompoundDrawablePadding();
//                int drawableWidth = drawableLeft.getIntrinsicWidth();
//                float bodyWidth = textWidth + drawableWidth + drawablePadding;
//
//                canvas.translate((getWidth() - bodyWidth - getPaddingLeft() - getPaddingRight()) / 2, 0);
//            }
//            if (drawableRight != null) {
//                float textWidth = getPaint().measureText(getHint().toString()); // 文字宽度
//                int drawablePadding = getCompoundDrawablePadding(); // 图标间距
//                int drawableWidth = drawableRight.getIntrinsicWidth(); // 图标宽度
//                float bodyWidth = textWidth + drawableWidth + drawablePadding;
//                setPadding(getPaddingLeft(), getPaddingTop(), (int)(getWidth() - bodyWidth - getPaddingLeft()), getPaddingBottom());
//                canvas.translate((getWidth() - bodyWidth - getPaddingLeft()) / 2, 0);
//            }
            super.onDraw(canvas);
        }

    }

    public void translate(Drawable drawable, Canvas canvas) {
        if (drawable != null) {
            float textWidth = getPaint().measureText(getHint().toString());
            int drawablePadding = getCompoundDrawablePadding();
            int drawableWidth = drawable.getIntrinsicWidth();
            float bodyWidth = textWidth + drawableWidth + drawablePadding;
            if (drawable == getCompoundDrawables()[0]) {
                canvas.translate((getWidth() - bodyWidth - getPaddingLeft() - getPaddingRight()) / 2, 0);
            } else {
                setPadding(getPaddingLeft(), getPaddingTop(), (int)(getWidth() - bodyWidth - getPaddingLeft()), getPaddingBottom());
                canvas.translate((getWidth() - bodyWidth - getPaddingLeft()) / 2, 0);
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Log.d(TAG, "onFocusChange execute");
        // 恢复EditText默认的样式
        if (!pressSearch && TextUtils.isEmpty(getText().toString())) {
            isLeft = hasFocus;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        pressSearch = (keyCode == KeyEvent.KEYCODE_ENTER);
        if (pressSearch && listener != null) {
            /*隐藏软键盘*/
            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive()) {
                imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
            }
            if (event.getAction() == KeyEvent.ACTION_UP) {
                listener.onSearchClick(v);
            }
        }
        return false;
    }

    public interface OnSearchClickListener {
        void onSearchClick(View view);
    }

}
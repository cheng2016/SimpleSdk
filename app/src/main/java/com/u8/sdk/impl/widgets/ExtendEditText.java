package com.u8.sdk.impl.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.TextView;
import com.u8.sdk.utils.ResourceHelper;

public class ExtendEditText extends EditText {
  public OndelTouched OndelTouched;
  
  private boolean isHasFocus;
  
  private boolean isVisb = false;
  
  private Drawable mRightDrawable;
  
  public ExtendEditText(Context paramContext) {
    super(paramContext);
    if (!isInEditMode())
      init(); 
  }
  
  public ExtendEditText(Context paramContext, AttributeSet paramAttributeSet) {
    super(paramContext, paramAttributeSet);
    if (!isInEditMode())
      init(); 
  }
  
  public ExtendEditText(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
    super(paramContext, paramAttributeSet, paramInt);
    if (!isInEditMode())
      init(); 
  }
  
  private void init() {
    this.mRightDrawable = ResourceHelper.getDrawable(getContext(), "R.drawable.x_common_input_clear");
    this.mRightDrawable.setBounds(0, 0, this.mRightDrawable.getMinimumWidth(), this.mRightDrawable.getMinimumHeight());
    setOnFocusChangeListener(new FocusChangeListenerImpl());
    addTextChangedListener(new TextWatcherImpl());
    setOnEditorActionListener(new TextView.OnEditorActionListener() {
          public boolean onEditorAction(TextView param1TextView, int param1Int, KeyEvent param1KeyEvent) {
            ExtendEditText.this.setClearDrawableVisible(false);
            return false;
          }
        });
    setClearDrawableVisible(false);
    setImeOptions(268435462);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent) {
    boolean bool = true;
    switch (paramMotionEvent.getAction()) {
      default:
        return super.onTouchEvent(paramMotionEvent);
      case 0:
        break;
    } 
    if (this.isVisb) {
      boolean bool1;
      if (paramMotionEvent.getX() > (getWidth() - getTotalPaddingRight()) && paramMotionEvent.getX() < (getWidth() - getPaddingRight())) {
        bool1 = true;
      } else {
        bool1 = false;
      } 
      if (bool1) {
        setText("");
        if (this.OndelTouched != null)
          this.OndelTouched.onItemOntouch(); 
      } 
    } 
    if (getText().toString().length() < 1)
      bool = false; 
    setClearDrawableVisible(bool);
    performClick();
  }
  
  public boolean performClick() { return super.performClick(); }
  
  public void setClearDrawableVisible(boolean paramBoolean) {
    Drawable drawable;
    if (paramBoolean) {
      if (!this.isVisb) {
        drawable = this.mRightDrawable;
        this.isVisb = true;
      } else {
        return;
      } 
    } else if (this.isVisb) {
      drawable = null;
      this.isVisb = false;
    } else {
      return;
    } 
    setCompoundDrawables(null, null, drawable, null);
  }
  
  public void setOndelTouched(OndelTouched paramOndelTouched) { this.OndelTouched = paramOndelTouched; }
  
  public void setShakeAnimation() { setAnimation(shakeAnimation(5)); }
  
  public void setnextedit(final ExtendEditText editText) {
    setImeOptions(268435461);
    setOnEditorActionListener(new TextView.OnEditorActionListener() {
          public boolean onEditorAction(TextView param1TextView, int param1Int, KeyEvent param1KeyEvent) {
            if (param1Int == 5) {
              editText.setFocusable(true);
              editText.setFocusableInTouchMode(true);
              editText.requestFocus();
              return true;
            } 
            return false;
          }
        });
  }
  
  public Animation shakeAnimation(int paramInt) {
    TranslateAnimation translateAnimation = new TranslateAnimation(0.0F, 10.0F, 0.0F, 10.0F);
    translateAnimation.setInterpolator(new CycleInterpolator(paramInt));
    translateAnimation.setDuration(1000L);
    return translateAnimation;
  }
  
  private class FocusChangeListenerImpl implements View.OnFocusChangeListener {
    private FocusChangeListenerImpl() {}
    
    public void onFocusChange(View param1View, boolean param1Boolean) {
      boolean bool = true;
      ExtendEditText.access$202(ExtendEditText.this, param1Boolean);
      if (ExtendEditText.this.isHasFocus) {
        if (ExtendEditText.this.getText().toString().length() >= 1) {
          param1Boolean = bool;
        } else {
          param1Boolean = false;
        } 
        ExtendEditText.this.setClearDrawableVisible(param1Boolean);
        return;
      } 
      ExtendEditText.this.setClearDrawableVisible(false);
    }
  }
  
  public static interface OndelTouched {
    void onItemOntouch();
  }
  
  private class TextWatcherImpl implements TextWatcher {
    private TextWatcherImpl() {}
    
    public void afterTextChanged(Editable param1Editable) {
      boolean bool = true;
      if (ExtendEditText.this.isHasFocus) {
        if (ExtendEditText.this.getText().toString().length() < 1)
          bool = false; 
        ExtendEditText.this.setClearDrawableVisible(bool);
        return;
      } 
      ExtendEditText.this.setClearDrawableVisible(false);
    }
    
    public void beforeTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
    
    public void onTextChanged(CharSequence param1CharSequence, int param1Int1, int param1Int2, int param1Int3) {}
  }
}



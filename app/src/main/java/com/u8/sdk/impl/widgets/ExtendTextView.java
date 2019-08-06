package com.u8.sdk.impl.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

public class ExtendTextView extends TextView {
  public ExtendTextView(Context paramContext) { super(paramContext); }
  
  public ExtendTextView(Context paramContext, AttributeSet paramAttributeSet) { super(paramContext, paramAttributeSet); }
  
  public ExtendTextView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) { super(paramContext, paramAttributeSet, paramInt); }
  
  protected void onDraw(Canvas paramCanvas) {
    Drawable[] arrayOfDrawable = getCompoundDrawables();
    if (arrayOfDrawable != null) {
      Drawable drawable = arrayOfDrawable[0];
      if (drawable != null) {
        float f1 = getPaint().measureText(getText().toString());
        int i = getCompoundDrawablePadding();
        float f2 = drawable.getIntrinsicWidth();
        float f3 = i;
        paramCanvas.translate((getWidth() - f2 + f1 + f3) / 2.0F, 0.0F);
      } 
    } 
    super.onDraw(paramCanvas);
  }
}


/* Location:              C:\Users\mitni\Desktop\gitwork\AndroidTool\classes-dex2jar.jar!\co\\u8\sdk\impl\widgets\ExtendTextView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.6
 */
package com.u8.sdk.impl.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.u8.sdk.PayParams;
import com.u8.sdk.impl.DefaultSDKPlatform;
import com.u8.sdk.impl.common.PayPlatformType;
import com.u8.sdk.impl.listeners.ISDKPayListener;
import com.u8.sdk.impl.services.SdkManager;
import com.u8.sdk.impl.widgets.GridPayAdapter;
import com.u8.sdk.utils.ResourceHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PayActivity extends Activity {
  public static final int TAG_HELP_BACK = 102;
  
  public static final int TAG_HELP_ENTER = 103;
  
  public static final int TAG_ORDER_RESULT = 110;
  
  public static final int TAG_PAY_COIN = 105;
  
  public static final int TAG_PAY_ENTER = 104;
  
  public static final int TAG_PAY_OTHER = 106;
  
  public static final int TAG_PAY_RESULT = 111;
  
  public static final int TAG_PAY_TYPE_SELECTED = 101;
  
  public static final int TAG_RESULT_FAIL_ENTER = 109;
  
  public static final int TAG_RESULT_SUC_ENTER = 108;
  
  public static final int TAG_SEND_RESULT = 112;
  
  private GridPayAdapter.GridPayTypeData currSelected;
  
  private Animation leftin;
  
  private Animation leftout;
  
  private Handler mHandler = new Handler() {
      public void handleMessage(Message param1Message) {
        switch (param1Message.what) {
          default:
            return;
          case 104:
            PayActivity.this.payView.setVisibility(0);
            PayActivity.this.payView.startAnimation(PayActivity.this.leftin);
            return;
          case 101:
            break;
        } 
        int i = Integer.valueOf(param1Message.obj.toString()).intValue();
        Iterator iterator = PayActivity.this.payTypeList.iterator();
        while (iterator.hasNext())
          ((GridPayAdapter.GridPayTypeData)iterator.next()).isChecked = false; 
        GridPayAdapter.GridPayTypeData gridPayTypeData = (GridPayAdapter.GridPayTypeData)PayActivity.this.payTypeList.get(i);
        gridPayTypeData.isChecked = true;
        PayActivity.access$302(PayActivity.this, gridPayTypeData);
        PayActivity.this.payAdapter.notifyDataSetChanged();
      }
    };
  
  private GridPayAdapter payAdapter;
  
  private List<GridPayAdapter.GridPayTypeData> payTypeList;
  
  private LinearLayout payView;
  
  private boolean payed = false;
  
  private Animation rightin;
  
  private Animation rightout;
  
  private void doGenerateTestData() {
    GridPayAdapter.GridPayTypeData gridPayTypeData1 = new GridPayAdapter.GridPayTypeData();
    gridPayTypeData1.typeID = PayPlatformType.ALIPAY;
    gridPayTypeData1.imgID = ResourceHelper.getIdentifier(this, "R.drawable.x_c_zfb");
    gridPayTypeData1.name = ResourceHelper.getString(this, "R.string.x_pay_t_alipay");
    gridPayTypeData1.isChecked = false;
    gridPayTypeData1.canChecked = true;
    this.currSelected = gridPayTypeData1;
    GridPayAdapter.GridPayTypeData gridPayTypeData2 = new GridPayAdapter.GridPayTypeData();
    gridPayTypeData2.typeID = PayPlatformType.WEIXIN;
    gridPayTypeData2.imgID = ResourceHelper.getIdentifier(this, "R.drawable.x_c_weixin");
    gridPayTypeData2.name = ResourceHelper.getString(this, "R.string.x_pay_t_weixin");
    gridPayTypeData2.isChecked = false;
    gridPayTypeData2.canChecked = true;
    GridPayAdapter.GridPayTypeData gridPayTypeData3 = new GridPayAdapter.GridPayTypeData();
    gridPayTypeData3.typeID = PayPlatformType.UNION;
    gridPayTypeData3.imgID = ResourceHelper.getIdentifier(this, "R.drawable.x_c_payco");
    gridPayTypeData3.name = ResourceHelper.getString(this, "R.string.x_pay_t_union");
    gridPayTypeData3.isChecked = false;
    gridPayTypeData3.canChecked = true;
    GridPayAdapter.GridPayTypeData gridPayTypeData4 = new GridPayAdapter.GridPayTypeData();
    gridPayTypeData4.typeID = PayPlatformType.XCOIN;
    gridPayTypeData4.imgID = ResourceHelper.getIdentifier(this, "R.drawable.x_c_xmpay");
    gridPayTypeData4.name = ResourceHelper.getString(this, "R.string.x_pay_t_xcoin");
    gridPayTypeData4.isChecked = true;
    gridPayTypeData4.canChecked = true;
    this.payTypeList.add(gridPayTypeData4);
    this.payTypeList.add(gridPayTypeData1);
    this.payTypeList.add(gridPayTypeData2);
    this.payTypeList.add(gridPayTypeData3);
  }
  
  private void doPay() {
    if (this.currSelected == null) {
      ResourceHelper.showTip(this, "R.string.x_pay_select_tip");
      return;
    } 
    PayParams payParams = DefaultSDKPlatform.getInstance().getLastPayData();
    if (payParams == null) {
      Log.e("U8SDK", "sdk pay data is null");
      return;
    } 
    switch (this.currSelected.typeID) {
      default:
        return;
      case ALIPAY:
        ResourceHelper.showTip(this, "R.string.x_pay_method_tip");
        return;
      case WEIXIN:
        ResourceHelper.showTip(this, "R.string.x_pay_method_tip");
        return;
      case UNION:
        ResourceHelper.showTip(this, "R.string.x_pay_method_tip");
        return;
      case XCOIN:
        break;
    } 
    SdkManager.getInstance().pay(this, payParams, new ISDKPayListener() {
          public void onFailed(int param1Int) {
            DefaultSDKPlatform.getInstance().payFailCallback();
            PayActivity.this.finish();
          }
          
          public void onSuccess(String param1String) {
            DefaultSDKPlatform.getInstance().paySucCallback();
            PayActivity.this.finish();
          }
        });
  }
  
  private void doScreenAdaptation(GridView paramGridView) {
    int i = this.payTypeList.size();
    DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    float f = displayMetrics.density;
    int j = (int)((i * 71) * f);
    int k = (int)(67 * f);
    paramGridView.setLayoutParams(new LinearLayout.LayoutParams(j, -2));
    paramGridView.setColumnWidth(k);
    paramGridView.setHorizontalSpacing(3);
    paramGridView.setStretchMode(0);
    paramGridView.setNumColumns(i);
  }
  
  private void initGrid() {
    ((TextView)ResourceHelper.getView(this, "R.id.x_payhelpabout")).setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) { PayActivity.this.mHandler.sendEmptyMessage(103); }
        });
    this.payView = (LinearLayout)ResourceHelper.getView(this, "R.id.x_pay_main");
    ((LinearLayout)ResourceHelper.getView(this, "R.id.x_pay_back")).setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            DefaultSDKPlatform.getInstance().payFailCallback();
            PayActivity.this.finish();
          }
        });
    PayParams payParams = DefaultSDKPlatform.getInstance().getLastPayData();
    if (payParams != null)
      ((TextView)ResourceHelper.getView(this, "R.id.x_payorderinfomsg")).setText("¥" + payParams.getPrice()); 
    GridView gridView = (GridView)ResourceHelper.getView(this, "R.id.x_paygridview");
    this.payTypeList = new ArrayList();
    this.payAdapter = new GridPayAdapter(this, this.payTypeList, this.mHandler);
    doGenerateTestData();
    doScreenAdaptation(gridView);
    gridView.setAdapter(this.payAdapter);
  }
  
  private void initUI() {
    setRequestedOrientation(4);
    setContentView(ResourceHelper.getIdentifier(this, "R.layout.x_pay"));
    ((Button)ResourceHelper.getView(this, "R.id.x_paybtn")).setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) { PayActivity.this.doPay(); }
        });
    initAnimations();
    initGrid();
    this.currSelected = (GridPayAdapter.GridPayTypeData)this.payTypeList.get(0);
    this.mHandler.sendEmptyMessage(104);
  }
  
  public void initAnimations() {
    this.leftin = AnimationUtils.loadAnimation(this, ResourceHelper.getIdentifier(this, "R.anim.x_appear_to_left"));
    this.leftout = AnimationUtils.loadAnimation(this, ResourceHelper.getIdentifier(this, "R.anim.x_disappear_to_left"));
    this.rightin = AnimationUtils.loadAnimation(this, ResourceHelper.getIdentifier(this, "R.anim.x_appear_to_right"));
    this.rightout = AnimationUtils.loadAnimation(this, ResourceHelper.getIdentifier(this, "R.anim.x_disappear_to_right"));
  }
  
  public void onBackPressed() {
    if (this.payed) {
      this.payed = false;
    } else {
      DefaultSDKPlatform.getInstance().payFailCallback();
    } 
    finish();
  }
  
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    try {
      initUI();
      return;
    } catch (Exception paramBundle) {
      paramBundle.printStackTrace();
      return;
    } 
  }
  
  protected void onDestroy() { super.onDestroy(); }
}


/* Location:              C:\Users\mitni\Desktop\gitwork\AndroidTool\classes-dex2jar.jar!\co\\u8\sdk\impl\activities\PayActivity.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.6
 */
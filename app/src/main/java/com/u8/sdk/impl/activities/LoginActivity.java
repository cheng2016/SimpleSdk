package com.u8.sdk.impl.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.u8.sdk.impl.DefaultSDKPlatform;
import com.u8.sdk.impl.listeners.ISDKLoginListener;
import com.u8.sdk.impl.listeners.ISDKRegisterOnekeyListener;
import com.u8.sdk.impl.services.SdkManager;
import com.u8.sdk.impl.widgets.ExtendEditText;
import com.u8.sdk.impl.widgets.ExtendTextView;
import com.u8.sdk.utils.ResourceHelper;
import com.u8.sdk.utils.StoreUtils;
import java.lang.ref.WeakReference;

public class LoginActivity extends Activity {
  private final String KEY_NAME = "u8_key_login_name";
  
  private final String KEY_PASSWORD = "u8_key_password";
  
	private Animation.AnimationListener animationListener = new Animation.AnimationListener() {
		public void onAnimationEnd(Animation arg0) {
			canTouch = true;
		}

		public void onAnimationRepeat(Animation arg0) {
		}

		public void onAnimationStart(Animation arg0) {
			canTouch = false;
		}
	};
  
  private boolean canTouch = true;
  
  private MyHandler handler = new MyHandler(this);
  
  private Animation left_in;
  
  private Animation left_out;
  
  private Animation right_in;
  
  private Animation right_out;
  
  private boolean showLoginPwd;
  
  private boolean showRegUserPwd = true;
  
  private String view_resource;
  
  private ImageView x_login_showpwdImg;
  
  private ExtendEditText x_login_username;
  
  private ExtendEditText x_login_userpwd;
  
  private ExtendEditText x_register_name;
  
  private ExtendEditText x_register_pwd;
  
  private void doLogin() {
    final String username = this.x_login_username.getText().toString();
    final String password = this.x_login_userpwd.getText().toString();
    if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
      ResourceHelper.showTip(this, "R.string.x_register_invalid_name_tip");
      return;
    } 
    SdkManager.getInstance().login(username, password, new ISDKLoginListener() {
          public void onFailed(int param1Int) { LoginActivity.this.runOnUiThread(new Runnable() {
                  public void run() {
                    ResourceHelper.showTip(LoginActivity.this, "R.string.x_login_fail");
                    DefaultSDKPlatform.getInstance().loginFailCallback();
                  }
                }); }
          
          public void onSuccess(final String id, final String name) { LoginActivity.this.runOnUiThread(new Runnable() {
                  public void run() {
                    StoreUtils.putString(LoginActivity.this, "u8_key_login_name", username);
                    StoreUtils.putString(LoginActivity.this, "u8_key_password", password);
                    ResourceHelper.showTip(LoginActivity.this, "R.string.x_login_suc");
                    DefaultSDKPlatform.getInstance().loginSucCallback(id, name);
                    finish();
                    overridePendingTransition(ResourceHelper.getIdentifier(LoginActivity.this, "R.anim.x_appear_to_right"), ResourceHelper.getIdentifier(LoginActivity.this, "R.anim.x_disappear_to_right"));
                  }
                }); }
        });
  }
  
  private void doRegister(final String username, final String password, boolean paramBoolean) {
        if (paramBoolean) {
          SdkManager.getInstance().registerOnekey("", new ISDKRegisterOnekeyListener() {
                public void onFailed(int param1Int) { LoginActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                          ResourceHelper.showTip(LoginActivity.this, "R.string.x_fastlogin_accout_fail");
                          x_register_name.setText("");
                          x_register_pwd.setText("");
                          DefaultSDKPlatform.getInstance().loginFailCallback();
                        }
                      }); }

                public void onSuccess(final String id, final String name, final String password) { LoginActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                          StoreUtils.putString(LoginActivity.this, "u8_key_login_name", name);
                          StoreUtils.putString(LoginActivity.this, "u8_key_password", password);
                          StringBuffer stringBuffer = new StringBuffer();
                          stringBuffer.append(ResourceHelper.getString(LoginActivity.this, "R.string.x_fastlogin_accout")).append(name).append("\n").append(ResourceHelper.getString(LoginActivity.this, "R.string.x_fastlogin_pwd")).append(password);
                          Toast.makeText(LoginActivity.this, stringBuffer.toString(), 0).show();
                          DefaultSDKPlatform.getInstance().loginSucCallback(id, name);
                          finish();
                          overridePendingTransition(ResourceHelper.getIdentifier(LoginActivity.this, "R.anim.x_appear_to_right"), ResourceHelper.getIdentifier(LoginActivity.this, "R.anim.x_disappear_to_right"));
                        }
                      }); }
              });
          return;
        } 
        if ((TextUtils.isEmpty(username) || TextUtils.isEmpty(password))) {
          Toast.makeText(this, ResourceHelper.getString(this, "R.string.x_register_invalid_name_tip"), 0).show();
          return;
        } 
        SdkManager.getInstance().register(username, password, new ISDKLoginListener() {
            public void onFailed(int param1Int) { LoginActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                      ResourceHelper.showTip(LoginActivity.this, "R.string.x_register_fail");
                      x_register_name.setText("");
                      x_register_pwd.setText("");
                      DefaultSDKPlatform.getInstance().loginFailCallback();
                    }
                }); }
          
          public void onSuccess(final String id, final String name) { LoginActivity.this.runOnUiThread(new Runnable() {
                  public void run() {
                    StoreUtils.putString(LoginActivity.this, "u8_key_login_name", username);
                    StoreUtils.putString(LoginActivity.this, "u8_key_password", password);
                    ResourceHelper.showTip(LoginActivity.this, "R.string.x_register_suc");
                    DefaultSDKPlatform.getInstance().loginSucCallback(id, name);
                    finish();
                    overridePendingTransition(ResourceHelper.getIdentifier(LoginActivity.this, "R.anim.x_appear_to_right"), ResourceHelper.getIdentifier(LoginActivity.this, "R.anim.x_disappear_to_right"));
                  }
                }); }
        });
  }
  
  private void initAni() {
    this.left_in = AnimationUtils.loadAnimation(this, ResourceHelper.getIdentifier(this, "R.anim.x_appear_to_left"));
    this.left_out = AnimationUtils.loadAnimation(this, ResourceHelper.getIdentifier(this, "R.anim.x_disappear_to_left"));
    this.right_in = AnimationUtils.loadAnimation(this, ResourceHelper.getIdentifier(this, "R.anim.x_appear_to_right"));
    this.right_out = AnimationUtils.loadAnimation(this, ResourceHelper.getIdentifier(this, "R.anim.x_disappear_to_right"));
    this.left_in.setAnimationListener(this.animationListener);
    this.left_out.setAnimationListener(this.animationListener);
    this.right_in.setAnimationListener(this.animationListener);
    this.right_out.setAnimationListener(this.animationListener);
  }
  
  private void initLogin() {
    this.x_login_username = (ExtendEditText)ResourceHelper.getView(this, "R.id.x_login_username");
    this.x_login_userpwd = (ExtendEditText)ResourceHelper.getView(this, "R.id.x_login_userpwd");
    this.x_login_username.setnextedit(this.x_login_userpwd);
    ((Button)ResourceHelper.getView(this, "R.id.x_login_go")).setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) { LoginActivity.this.doLogin(); }
        });
    this.x_login_showpwdImg = (ImageView)ResourceHelper.getView(this, "R.id.x_login_showpwdImg");
    LinearLayout linearLayout = (LinearLayout)ResourceHelper.getView(this, "R.id.x_login_showpwd");
    linearLayout.setEnabled(true);
    linearLayout.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
              showLoginPwd = !showLoginPwd;
              if (showLoginPwd) {
                x_login_userpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                x_login_showpwdImg.setBackgroundResource(ResourceHelper.getIdentifier(LoginActivity.this,
                    "R.drawable.x_login_hidepwd"));
              } else {
                x_login_userpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                x_login_showpwdImg.setBackgroundResource(ResourceHelper.getIdentifier(LoginActivity.this,
                    "R.drawable.x_login_showpwd"));
              }
          }
        });
    ((TextView)ResourceHelper.getView(this, "R.id.x_login_faststart")).setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) { LoginActivity.this.doRegister("", "", true); }
        });
    ExtendTextView extendTextView = (ExtendTextView)ResourceHelper.getView(this, "R.id.x_login_register");
    if (extendTextView != null)
      extendTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) { LoginActivity.this.handler.sendEmptyMessage(4); }
          }); 
    ((ExtendTextView)findViewById(ResourceHelper.getIdentifier(this, "R.id.x_login_findpwd"))).setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) { ResourceHelper.showTip(LoginActivity.this, "R.string.x_function_not_impl"); }
        });
  }
  
  private void initRegister() {
    TextView textView = (TextView)findViewById(ResourceHelper.getIdentifier(this, "R.id.x_register_user_jump"));
    textView.setEnabled(true);
    textView.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) { ResourceHelper.showTip(LoginActivity.this, "R.string.x_function_not_impl"); }
        });
    this.x_register_name = (ExtendEditText)ResourceHelper.getView(this, "R.id.x_register_name");
    this.x_register_pwd = (ExtendEditText)ResourceHelper.getView(this, "R.id.x_register_pwd");
    this.x_register_name.setnextedit(this.x_register_pwd);
    ((Button)ResourceHelper.getView(this, "R.id.x_register_user_go")).setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) { LoginActivity.this.doRegister(LoginActivity.this.x_register_name.getText().toString(), LoginActivity.this.x_register_pwd.getText().toString(), false); }
        });
    LinearLayout linearLayout1 = (LinearLayout)findViewById(ResourceHelper.getIdentifier(this, "R.id.x_register_user_back"));
    linearLayout1.setEnabled(true);
    linearLayout1.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) { LoginActivity.this.handler.sendEmptyMessage(2); }
        });
    final ImageView x_login_showpwdImg = (ImageView)ResourceHelper.getView(this, "R.id.x_regu_showpwdImg");
    LinearLayout linearLayout2 = (LinearLayout)ResourceHelper.getView(this, "R.id.x_regu_showpwd");
    linearLayout2.setEnabled(true);
    this.x_register_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
    linearLayout2.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
              showRegUserPwd = !showRegUserPwd;
              if (showRegUserPwd) {
                x_register_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                x_login_showpwdImg.setBackgroundResource(ResourceHelper.getIdentifier(LoginActivity.this,
                    "R.drawable.x_login_hidepwd"));
              } else {
                x_register_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                x_login_showpwdImg.setBackgroundResource(ResourceHelper.getIdentifier(LoginActivity.this,
                    "R.drawable.x_login_showpwd"));
              }
          }
        });
  }
  
  private void initView() {
    initAni();
    initLogin();
    initRegister();
  }
  
  private void viewBack(String paramString) {
    LinearLayout linearLayout1 = (LinearLayout)findViewById(ResourceHelper.getIdentifier(this, this.view_resource));
    LinearLayout linearLayout2 = (LinearLayout)findViewById(ResourceHelper.getIdentifier(this, paramString));
    linearLayout1.setVisibility(8);
    linearLayout1.startAnimation(this.right_out);
    linearLayout2.setVisibility(0);
    linearLayout2.startAnimation(this.right_in);
    this.view_resource = paramString;
  }
  
  private void viewSet(String paramString) {
    LinearLayout linearLayout1 = (LinearLayout)findViewById(ResourceHelper.getIdentifier(this, this.view_resource));
    LinearLayout linearLayout2 = (LinearLayout)findViewById(ResourceHelper.getIdentifier(this, paramString));
    linearLayout1.setVisibility(8);
    linearLayout1.startAnimation(this.left_out);
    linearLayout2.setVisibility(0);
    linearLayout2.startAnimation(this.left_in);
    this.view_resource = paramString;
  }
  
  public boolean dispatchTouchEvent(MotionEvent paramMotionEvent) {
    if (!this.canTouch)
      return false; 
    if (paramMotionEvent.getAction() == 0) {
      View view = getCurrentFocus();
      if (isShouldHideInput(view, paramMotionEvent)) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService("input_method");
        this.x_login_userpwd.setClearDrawableVisible(false);
        this.x_login_username.setClearDrawableVisible(false);
        if (inputMethodManager != null)
          inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0); 
      } 
      return super.dispatchTouchEvent(paramMotionEvent);
    } 
    return getWindow().superDispatchTouchEvent(paramMotionEvent) ? true : onTouchEvent(paramMotionEvent);
  }
  
  public boolean isShouldHideInput(View paramView, MotionEvent paramMotionEvent) {
    if (paramView != null && paramView instanceof ExtendEditText) {
      int[] arrayOfInt = new int[2];
      paramView.getLocationInWindow(arrayOfInt);
      int i = arrayOfInt[0];
      int j = arrayOfInt[1];
      int k = paramView.getHeight();
      int m = paramView.getWidth();
      if (paramMotionEvent.getRawX() <= i || paramMotionEvent.getRawX() >= (i + m) || paramMotionEvent.getRawY() <= j || paramMotionEvent.getRawY() >= (j + k))
        return true; 
    } 
    return false;
  }
  
  public void onBackPressed() {
    if (!this.canTouch)
      return; 
    if (this.view_resource.equals("R.id.x_login_view")) {
      finish();
      overridePendingTransition(ResourceHelper.getIdentifier(this, "R.anim.x_appear_to_right"), ResourceHelper.getIdentifier(this, "R.anim.x_disappear_to_right"));
      return;
    } 
    this.handler.sendEmptyMessage(2);
  }
  
  @SuppressLint({"InlinedApi"})
  protected void onCreate(Bundle paramBundle) {
    super.onCreate(paramBundle);
    requestWindowFeature(1);
    getWindow().setFlags(128, 128);
    setContentView(ResourceHelper.getIdentifier(this, "R.layout.x_user_login"));
    initView();
    this.handler = new MyHandler(this);
    this.handler.sendEmptyMessage(1);
  }
  
  public void startLogin() {
    this.view_resource = "R.id.x_login_view";
    findViewById(ResourceHelper.getIdentifier(this, this.view_resource)).setVisibility(0);
    String str1 = StoreUtils.getString(this, "u8_key_login_name");
    String str2 = StoreUtils.getString(this, "u8_key_password");
    if (!TextUtils.isEmpty(str1))
      this.x_login_username.setText(str1); 
    if (!TextUtils.isEmpty(str2))
      this.x_login_userpwd.setText(str2); 
    overridePendingTransition(ResourceHelper.getIdentifier(this, "R.anim.x_appear_to_left"), ResourceHelper.getIdentifier(this, "R.anim.x_disappear_to_left"));
  }
  
  public class HANDLER_KEY {
    static final int BACK_FINDPWD_ONE_VIEW = 12;
    
    static final int BACK_FINDPWD_SELECT_VIEW = 11;
    
    static final int BACK_LGOIN_VIEW = 2;
    
    static final int INIT_LOGIN = 1;
    
    static final int SET_REGISTER_PHONE_VIEW = 3;
    
    static final int SET_REGISTER_USER_VIEW = 4;
  }
  
  public class LAYOUT_KEY {
    static final String anim_appear_to_left = "R.anim.x_appear_to_left";
    
    static final String anim_appear_to_right = "R.anim.x_appear_to_right";
    
    static final String anim_disappear_to_left = "R.anim.x_disappear_to_left";
    
    static final String anim_disappear_to_right = "R.anim.x_disappear_to_right";
    
    static final String x_login_view = "R.id.x_login_view";
    
    static final String x_register_user_view = "R.id.x_register_user_view";
  }
  
  private static class MyHandler extends Handler {
    private final WeakReference<LoginActivity> mActivity;
    
    public MyHandler(LoginActivity param1LoginActivity) { this.mActivity = new WeakReference(param1LoginActivity); }
    
    public void handleMessage(Message param1Message) {
      LoginActivity loginActivity = (LoginActivity)this.mActivity.get();
      if (loginActivity != null) {
        super.handleMessage(param1Message);
        switch (param1Message.what) {
          default:
            return;
          case 1:
            loginActivity.startLogin();
            return;
          case 2:
            loginActivity.viewBack("R.id.x_login_view");
            return;
          case 4:
            break;
        } 
        loginActivity.viewSet("R.id.x_register_user_view");
        return;
      } 
    }
  }
}



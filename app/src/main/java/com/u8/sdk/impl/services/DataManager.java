package com.u8.sdk.impl.services;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.u8.sdk.impl.data.SimpleUser;
import com.u8.sdk.utils.Base64;
import com.u8.sdk.utils.StoreUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;

public class DataManager {
  private static DataManager instance;
  
  private long coinNum;
  
  private SimpleUser currLoginedUser;
  
  /**
	 * 添加一个已经登录的帐号
	 * 
	 * @param user
	 */
	private void addLoginedUser(Context context, SimpleUser user) {
		if (user == null)
			return;
		List<SimpleUser> users = getAllLoginedUsers(context);
		for (SimpleUser u : users) {
			if (u.getUsername().equals(user.getUsername())) {
				users.remove(u);
				break;
			}
		}
		users.add(0, user);
		saveLoginedUsers(context, users);
  }
  
  private List<SimpleUser> getAllLoginedUsers(Context paramContext) {
    ArrayList arrayList = new ArrayList();
    try {
      JSONArray jSONArray = getJSONArray(paramContext);
      for (byte b = 0; b < jSONArray.length(); b++) {
        SimpleUser simpleUser = new SimpleUser();
        simpleUser.fromJSON(jSONArray.getJSONObject(b));
        arrayList.add(simpleUser);
      } 
      return arrayList;
    } catch (JSONException e) {
      e.printStackTrace();
      return arrayList;
    } catch (Exception e) {
      e.printStackTrace();
      return arrayList;
    } 
  }
  
  public static DataManager getInstance() {
    if (instance == null)
      instance = new DataManager(); 
    return instance;
  }
  
  public void addLoginedUser(Context paramContext, String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean) { addLoginedUser(paramContext, new SimpleUser(paramString1, paramString2, paramString3, paramString4, paramBoolean)); }
  
  public long getCoinNum() { return this.coinNum; }
  
  public SimpleUser getCurrLoginedUser() { return this.currLoginedUser; }
  
  JSONArray getJSONArray(Context paramContext) throws JSONException, Exception {
    String str = StoreUtils.getString(paramContext, "plocal_users");
    if (!TextUtils.isEmpty(str))
      return new JSONArray(Base64.decodeString(str)); 
    Log.e("U8SDK", "userJsonStr is empty.");
    return new JSONArray();
  }
  
  public SimpleUser getLastLoginedUser(Context paramContext) {
    for (SimpleUser simpleUser : getAllLoginedUsers(paramContext)) {
      if (simpleUser.isCurrSelected())
        return simpleUser; 
    } 
    return null;
  }
  
	public void removeLoginedUser(Context context, SimpleUser user) {
		if (user == null)
			return;
		List<SimpleUser> users = getAllLoginedUsers(context);
		for (SimpleUser u : users) {
			if (u.getUsername().equals(user.getUsername())) {
				users.remove(u);
				break;
			}
		}
		saveLoginedUsers(context, users);
  }
  
	public void saveLoginedUsers(Context context, List<SimpleUser> users) {
      if (users == null || users.size() == 0) {
        return;
      }
      // 检查当前使用的帐号
      int selectedIndex = -1;
      for (int i = 0; i < users.size(); i++) {
        SimpleUser u = users.get(i);
        if (selectedIndex >= 0 && u.isCurrSelected()) {
          u.setCurrSelected(false);
          continue;
        }
        if (u.isCurrSelected()) {
          selectedIndex = i;
        }
      }
      try {
        JSONArray array = new JSONArray();
        for (SimpleUser user : users) {
          JSONObject obj = user.toJSON();
          array.put(obj);
        }
        StoreUtils.putString(context, Consts.SKEY_USER, Base64.encode(array.toString().getBytes()));
      } catch (Exception e) {
        e.printStackTrace();
      }
	}
  
  public void setCoinNum(long paramLong) { this.coinNum = paramLong; }
  
  public void setCurrLoginedUser(SimpleUser paramSimpleUser) { this.currLoginedUser = paramSimpleUser; }
  
  public void setCurrLoginedUser(String paramString1, String paramString2, String paramString3, String paramString4) { this.currLoginedUser = new SimpleUser(paramString1, paramString2, paramString3, paramString4, true); }
}



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
  
  private void addLoginedUser(Context paramContext, SimpleUser paramSimpleUser) {
    if (paramSimpleUser == null)
      return; 
    List list = getAllLoginedUsers(paramContext);
    for (SimpleUser simpleUser : list) {
      if (simpleUser.getUsername().equals(paramSimpleUser.getUsername())) {
        list.remove(simpleUser);
        break;
      } 
    } 
    list.add(0, paramSimpleUser);
    saveLoginedUsers(paramContext, list);
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
  
  public void removeLoginedUser(Context paramContext, SimpleUser paramSimpleUser) {
    if (paramSimpleUser == null)
      return; 
    List list = getAllLoginedUsers(paramContext);
    for (SimpleUser simpleUser : list) {
      if (simpleUser.getUsername().equals(paramSimpleUser.getUsername())) {
        list.remove(simpleUser);
        break;
      } 
    } 
    saveLoginedUsers(paramContext, list);
  }
  
  public void saveLoginedUsers(Context paramContext, List<SimpleUser> paramList) {
    JSONArray jSONArray;
    if (paramList == null || paramList.size() == 0)
      return; 
    byte b2 = -1;
    for (byte b1 = 0; b1 < paramList.size(); b1++) {
      jSONArray = (SimpleUser)paramList.get(b1);
      if (b2 >= 0 && jSONArray.isCurrSelected()) {
        jSONArray.setCurrSelected(false);
      } else if (jSONArray.isCurrSelected()) {
        b2 = b1;
      } 
    } 
    try {
      jSONArray = new JSONArray();
      Iterator iterator = paramList.iterator();
      while (iterator.hasNext())
        jSONArray.put(((SimpleUser)iterator.next()).toJSON()); 
    } catch (Exception e) {
      e.printStackTrace();
      return;
    } 
    StoreUtils.putString(paramContext, "plocal_users", Base64.encode(jSONArray.toString().getBytes()));
  }
  
  public void setCoinNum(long paramLong) { this.coinNum = paramLong; }
  
  public void setCurrLoginedUser(SimpleUser paramSimpleUser) { this.currLoginedUser = paramSimpleUser; }
  
  public void setCurrLoginedUser(String paramString1, String paramString2, String paramString3, String paramString4) { this.currLoginedUser = new SimpleUser(paramString1, paramString2, paramString3, paramString4, true); }
}



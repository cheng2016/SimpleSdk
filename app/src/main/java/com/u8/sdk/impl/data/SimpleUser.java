package com.u8.sdk.impl.data;

import org.json.JSONException;
import org.json.JSONObject;

public class SimpleUser {
  private String id;
  
  private boolean isCurrSelected;
  
  private String token;
  
  private String typeName;
  
  private String username;
  
  public SimpleUser() {}
  
  public SimpleUser(String paramString1, String paramString2, String paramString3, String paramString4, boolean paramBoolean) {
    this.id = paramString1;
    this.typeName = paramString2;
    this.username = paramString3;
    this.token = paramString4;
    this.isCurrSelected = paramBoolean;
  }
  
  public void fromJSON(JSONObject paramJSONObject) throws JSONException {
    this.id = paramJSONObject.getString("id");
    this.typeName = paramJSONObject.getString("typeName");
    this.username = paramJSONObject.getString("username");
    this.token = paramJSONObject.getString("token");
    this.isCurrSelected = paramJSONObject.getBoolean("currSelected");
  }
  
  public String getId() { return this.id; }
  
  public String getToken() { return this.token; }
  
  public String getTypeName() { return this.typeName; }
  
  public String getUsername() { return this.username; }
  
  public boolean isCurrSelected() { return this.isCurrSelected; }
  
  public void setCurrSelected(boolean paramBoolean) { this.isCurrSelected = paramBoolean; }
  
  public void setToken(String paramString) { this.token = paramString; }
  
  public void setUsername(String paramString) { this.username = paramString; }
  
  public JSONObject toJSON() throws JSONException {
    JSONObject jSONObject = new JSONObject();
    jSONObject.put("id", this.id);
    jSONObject.put("typeName", this.typeName);
    jSONObject.put("username", this.username);
    jSONObject.put("token", this.token);
    jSONObject.put("currSelected", this.isCurrSelected);
    return jSONObject;
  }
}


/* Location:              C:\Users\mitni\Desktop\gitwork\AndroidTool\classes-dex2jar.jar!\co\\u8\sdk\impl\data\SimpleUser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.6
 */
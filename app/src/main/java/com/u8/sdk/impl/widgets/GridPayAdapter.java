package com.u8.sdk.impl.widgets;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.u8.sdk.impl.common.PayPlatformType;
import com.u8.sdk.utils.ResourceHelper;
import java.util.List;

public class GridPayAdapter extends BaseAdapter {
  private List<GridPayTypeData> dataList;
  
  private Handler handler;
  
  private LayoutInflater inflater;
  
  public GridPayAdapter(Activity paramActivity, List<GridPayTypeData> paramList, Handler paramHandler) {
    this.dataList = paramList;
    this.inflater = LayoutInflater.from(paramActivity);
    this.handler = paramHandler;
  }
  
  public int getCount() { return this.dataList.size(); }
  
  public Object getItem(int paramInt) { return this.dataList.get(paramInt); }
  
  public long getItemId(int paramInt) { return paramInt; }
  
  public View getView(final int position, View paramView, ViewGroup paramViewGroup) {
    ViewHolder viewHolder;
    if (paramView == null) {
      paramView = this.inflater.inflate(ResourceHelper.getIdentifier(this.inflater.getContext(), "R.layout.x_pay_grid_item"), paramViewGroup, false);
      viewHolder = new ViewHolder();
      viewHolder.payTypeImage = (ImageView)ResourceHelper.getViewByParent(paramView, "R.id.x_pay_img");
      viewHolder.payCheckedImage = (ImageView)ResourceHelper.getViewByParent(paramView, "R.id.x_pay_slt");
      viewHolder.payTypeName = (TextView)ResourceHelper.getViewByParent(paramView, "R.id.x_pay_c_name");
      paramView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder)paramView.getTag();
    } 
    GridPayTypeData gridPayTypeData = (GridPayTypeData)this.dataList.get(paramInt);
    viewHolder.payTypeName.setText(gridPayTypeData.name);
    viewHolder.payTypeImage.setImageResource(gridPayTypeData.imgID);
    if (gridPayTypeData.isChecked) {
      viewHolder.payCheckedImage.setVisibility(0);
      paramView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View param1View) {
              if (GridPayAdapter.this.handler != null) {
                Message message = new Message();
                message.what = 101;
                message.obj = Integer.valueOf(position);
                GridPayAdapter.this.handler.sendMessage(message);
              } 
            }
          });
      return paramView;
    } 
    viewHolder.payCheckedImage.setVisibility(8);
    paramView.setOnClickListener(new View.OnClickListener() {
          public void onClick(View param1View) {
            if (GridPayAdapter.this.handler != null) {
              Message message = new Message();
              message.what = 101;
              message.obj = Integer.valueOf(position);
              GridPayAdapter.this.handler.sendMessage(message);
            } 
          }
        });
    return paramView;
  }
  
  public static class GridPayTypeData {
    public boolean canChecked = true;
    
    public int imgID;
    
    public boolean isChecked = false;
    
    public String name;
    
    public PayPlatformType typeID;
  }
  
  public class ViewHolder {
    public ImageView payCheckedImage;
    
    public ImageView payTypeImage;
    
    public TextView payTypeName;
  }
}


/* Location:              C:\Users\mitni\Desktop\gitwork\AndroidTool\classes-dex2jar.jar!\co\\u8\sdk\impl\widgets\GridPayAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.0.6
 */
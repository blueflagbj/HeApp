package com.heclient.heapp.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.heclient.heapp.R;
import com.heclient.heapp.util.Phone;
import com.heclient.heapp.util.PhoneNumAdapter;

import java.util.List;

public class ResultInfoAdapter extends ArrayAdapter<ResultInfo> {
    private int resourceId;
    // 适配器的构造函数，把要适配的数据传入这里
    public ResultInfoAdapter(Context context, int textViewResourceId, List<ResultInfo> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    // convertView 参数用于将之前加载好的布局进行缓存
    @SuppressLint("ResourceType")
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ResultInfo resultInfo=getItem(position); //获取当前项的Phone实例
        // 加个判断，以免ListView每次滚动时都要重新加载布局，以提高运行效率
        View view;
        ResultInfoAdapter.ViewHolder viewHolder;
        if (convertView==null){
            // 避免ListView每次滚动时都要重新加载布局，以提高运行效率
            view= LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
            // 避免每次调用getView()时都要重新获取控件实例
            viewHolder=new ResultInfoAdapter.ViewHolder();
            /* viewHolder.PhoneImage=view.findViewById(R.id.phone_image);*/
            viewHolder._id=view.findViewById(R.id.resultInfo_id);
            viewHolder.phone=view.findViewById(R.id.resultInfo_phone);
            viewHolder.user_name=view.findViewById(R.id.resultInfo_user_namen);
            viewHolder.returnMsg=view.findViewById(R.id.resultInfo_returnMsg);

            // 将ViewHolder存储在View中（即将控件的实例存储在其中）
            view.setTag(viewHolder);
        } else{
            view=convertView;
            viewHolder=(ResultInfoAdapter.ViewHolder) view.getTag();
        }

        // 获取控件实例，并调用set...方法使其显示出来
        //viewHolder.PhoneImage.setImageResource(phone.getImageId());
        String _id=String.valueOf(resultInfo.get_id());
        viewHolder._id.setText(_id);
        String phone=resultInfo.getPhone();
        viewHolder.phone.setText(phone);
        String returnMsg=resultInfo.getReturnMsg();
        viewHolder.returnMsg.setText(returnMsg);
        return view;
    }

    // 定义一个内部类，用于对控件的实例进行缓存
    class ViewHolder{
        // ImageView PhoneImage;
        TextView _id;
        TextView phone;
        TextView user_name;
        TextView returnMsg;

/*      Long _id, String X_RESULTINFO, String returnCode,
        String user_name, String returnMsg, String user_full_name, String phone,
        String isVip, String preSave, String currentOfferId, String state,
        String starLevelCreditLimit, String brandName, String brandId,
        String username, String balOrgName, String brandCode,
        String historyCost, String validateLevel, String createDate,
        String offerName, String regionId, String noticeFlag, String banlance,
        String balOrgId, String regionName, String cost, String is4G,
        String starLevel, String point, String currentOweFee,
        String starLevelValue, String age, String broadbandSpeed,
        String amountCredit, String X_RESULTCODE, String X_RECORDNUM*/
    }
}

package com.geekband.huzhouapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.geekband.huzhouapp.R;
import com.geekband.huzhouapp.vo.ManageInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/19
 */
public class ManageListAdapter extends BaseAdapter {

    ArrayList<ManageInfo> manageList;
    Context mContext;

    public ManageListAdapter(ArrayList<ManageInfo> manageList, Context context) {
        this.manageList = manageList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return manageList.size();
    }

    @Override
    public Object getItem(int position) {
        return manageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_manage_text, null);
            viewHolder.manage_class = (TextView) convertView.findViewById(R.id.manage_class);
            viewHolder.manage_content = (TextView) convertView.findViewById(R.id.manage_content);
            viewHolder.manage_image = (ImageView) convertView.findViewById(R.id.manage_image);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.manage_class.setText(manageList.get(position).getTitle());
        viewHolder.manage_content.setText(manageList.get(position).getContent());
        viewHolder.manage_image.setImageResource(manageList.get(position).getImageId());

        return convertView;
    }

    class ViewHolder {
        private TextView manage_class;
        private TextView manage_content;
        private ImageView manage_image;

    }
}

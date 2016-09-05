package com.kingja.raphael;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pizidea.imagepicker.ImgLoader;
import com.pizidea.imagepicker.UilImgLoader;
import com.pizidea.imagepicker.Util;
import com.pizidea.imagepicker.bean.ImageItem;

import java.util.List;

/**
 * Description：TODO
 * Create Time：2016/9/5 16:46
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class SelectAdapter extends BaseAdapter {
    private Context context;
    private List<ImageItem> items;
    private final ImgLoader presenter;
    private final int width;
    private final int ADD = 0;
    private final int IMG = 1;
    private ViewGroup.LayoutParams addParam;
    private ViewGroup.LayoutParams ivParam;
    private  LinearLayout.LayoutParams mParams;

    public SelectAdapter(Activity context, List<ImageItem> items) {
        this.context = context;
        this.items = items;
        presenter = new UilImgLoader();
        int screenWidth = context.getWindowManager().getDefaultDisplay().getWidth();
        width = (screenWidth - Util.dp2px(context, 2 * 2)) / 3;
        mParams = new LinearLayout.LayoutParams(width, width);
    }

    @Override
    public int getCount() {
        if (items.size() < 6) {
            return items.size() + 1;
        } else {
            return items.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return items.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.size() < 6) {
            return position == (getCount() - 1) ? ADD : IMG;
        } else {
            return IMG;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (getItemViewType(position) == ADD) {
            convertView = View.inflate(context, R.layout.item_add, null);
            ImageView iv_add = (ImageView) convertView.findViewById(R.id.iv_add);
            iv_add.setLayoutParams(mParams);
        } else {
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_select, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.iv.setLayoutParams(mParams);
            presenter.onPresentImage(viewHolder.iv, items.get(position).path, width);
        }

        return convertView;
    }

    public class ViewHolder {
        public final ImageView iv;
        public final View root;

        public ViewHolder(View root) {
            iv = (ImageView) root.findViewById(R.id.iv);
            this.root = root;
        }
    }


    public void setRefresh(List<ImageItem> items) {
        this.items = items;
        this.notifyDataSetChanged();
    }
}

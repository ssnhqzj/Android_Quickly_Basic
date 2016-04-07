package com.qzj.logistics.view.deliver.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qzj.logistics.R;
import com.qzj.logistics.bean.impl.SpinnerAdapterIf;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {

    private Context context;
    private List<SpinnerAdapterIf> list;
    private int layoutId;

    public SpinnerAdapter(Context context,int layoutId,List<SpinnerAdapterIf> list){
        this.context = context;
        this.layoutId = layoutId;
        this.list = list;
    }


    @Override
    public int getCount() {
        return list == null?0:list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holderView = null;
        if (convertView == null){
            holderView = new HolderView();
            convertView = View.inflate(context, layoutId,null);
            holderView.tv = (TextView) convertView.findViewById(R.id.spinner_item_text);
            convertView.setTag(holderView);
        }else{
            holderView = (HolderView) convertView.getTag();
        }

        if (list != null && list.size() > 0){
            SpinnerAdapterIf saInterface = list.get(position);
            if (saInterface != null){
                holderView.tv.setText(saInterface.findSpinnerText());
                holderView.tv.setTag(saInterface.findSpinnerId());
            }
        }

        return convertView;
    }

    public void setList(List<SpinnerAdapterIf> list) {
        this.list = list;
    }

    class HolderView {
        public TextView tv;
    }
}

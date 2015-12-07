package gzt.com.apptest;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import gzt.com.apptest.Chat.ChatItemLayout;

/**
 * Created by jianghejie on 15/11/26.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private static final int POSITION_LEFT = 1;
    private static final int POSITION_RIGHT = 2;
    public ArrayList<String> datas = null;

    public MyAdapter(ArrayList<String> datas) {
        this.datas = datas;
    }

    @Override
    public int getItemViewType(int position) {
        if(position%2 == 0){
            return POSITION_LEFT;
        }else{
            return POSITION_RIGHT;
        }
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(viewType,viewGroup,false);
        ViewHolder vh = null;
        View view = null;
        if (viewType == POSITION_LEFT){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item,viewGroup,false);
        }else{
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item1,viewGroup,false);
        }

        vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
//        viewHolder.mTextView.setText(datas.get(position));
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return datas.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ViewHolder(View view){
            super(view);
            mTextView = (TextView) view.findViewById(R.id.text);
            ChatItemLayout root = (ChatItemLayout) view.findViewById(R.id.chat_item_layout);
        }
    }
}

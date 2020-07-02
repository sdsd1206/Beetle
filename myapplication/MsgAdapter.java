package com.example.jack.myapplication;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder>{
    private List<chatmsg> mMsgList;
    //ViewHolder内部类
    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        LinearLayout leftL_name_layout;
        LinearLayout right_name_layout;
        TextView leftMsg;
        TextView rightMsg;
        TextView leftname;
        TextView rightname;
        //构造方法
        public ViewHolder(View view){
            super(view);
            leftL_name_layout = (LinearLayout)view.findViewById(R.id.left_name_layout);
            right_name_layout = (LinearLayout)view.findViewById(R.id.right_name_layout);
            leftLayout = (LinearLayout)view.findViewById(R.id.left_layout);
            rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
            leftMsg = (TextView)view.findViewById(R.id.left_msg);
            rightMsg = (TextView)view.findViewById(R.id.right_msg);
            leftname = (TextView) view.findViewById(R.id.left_name);
            rightname = (TextView)view.findViewById(R.id.riget_name);
        }
    }

    //MsgAdapter构造方法,参数是消息类的集合
    public MsgAdapter(List<chatmsg> msgList){
        mMsgList = msgList;
    }

    @Override
    //创建ViewHolder实例
    public ViewHolder onCreateViewHolder(ViewGroup parent , int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item ,parent,false);
        return new ViewHolder(view);
    }
    //当子项滚动到屏幕时，执行此方法
    @Override
    public void onBindViewHolder(ViewHolder holder ,int position){
        if(mMsgList.get(position).who.equals("0")){
            //如果是收到的消息，则显示左边的消息布局，将右边的消息布局隐藏
            holder.leftL_name_layout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.right_name_layout.setVisibility(View.GONE);
            holder.leftname.setText(mMsgList.get(position).getTime());
            holder.leftMsg.setText(mMsgList.get(position).getRoomcontext());
            //holder.leftname.setText(mMsgList.get(position).getUsername());
        }else if(mMsgList.get(position).who.equals("1")){
            //如果是发出去的消息，则显示右边的消息布局，将左边的消息布局隐藏
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftL_name_layout.setVisibility(View.GONE);
            holder.right_name_layout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightMsg.setText(mMsgList.get(position).getRoomcontext());
            holder.rightname.setText(mMsgList.get(position).getTime());
            //holder.righttime.setText(mMsgList.get((position)).getTime());
        }
    }

    @Override
    public int getItemCount(){
        return mMsgList.size();
    }
}

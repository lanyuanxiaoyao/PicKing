package com.lanyuan.picking.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lanyuan.picking.R;
import com.lanyuan.picking.util.FrescoUtil;

import java.util.List;

public class BaseContentsAdapter extends RecyclerView.Adapter<BaseContentsAdapter.MyViewHolder> {
    private List<BaseInfo> data;
    private int width;
    private Context context;
    private OnItemClickListener mListener;

    public BaseContentsAdapter(Context context, List<BaseInfo> data, int width) {
        this.context = context;
        this.data = data;
        this.width = width;
    }

    public void addMore(List<BaseInfo> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void removeAll() {
        this.data.removeAll(data);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void ItemClickListener(View view, int position, BaseInfo baseInfo);

        void ItemLongClickListener(View view, int position);
    }

    public void setOnClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_view, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView simpleDraweeView;

        public MyViewHolder(View itemView) {
            super(itemView);
            simpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.fresco);
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        FrescoUtil.setControllerListener(holder.simpleDraweeView, data.get(position).getCoverUrl(), width);
        if (mListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mListener.ItemClickListener(holder.itemView, pos, data.get(pos));
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mListener.ItemLongClickListener(holder.itemView, pos);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}

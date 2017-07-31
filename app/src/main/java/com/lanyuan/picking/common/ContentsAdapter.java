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

public class ContentsAdapter extends RecyclerView.Adapter<ContentsAdapter.MyViewHolder> {
    private List<AlbumInfo> lists;
    private int width;
    private Context context;
    private OnItemClickListener itemClickListener;

    public ContentsAdapter(Context context, List<AlbumInfo> lists, int width) {
        this.context = context;
        this.lists = lists;
        this.width = width;
    }

    public void addMore(List<AlbumInfo> data) {
        this.lists.addAll(data);
        notifyDataSetChanged();
    }

    public void removeAll() {
        this.lists.removeAll(lists);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void ItemClickListener(View view, int position, AlbumInfo albumInfo);

        void ItemLongClickListener(View view, int position, AlbumInfo albumInfo);
    }

    public void setOnClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
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
        if (!lists.get(position).equals(holder.simpleDraweeView.getTag())) {
            FrescoUtil.setControllerListener(holder.simpleDraweeView, lists.get(position).getCoverUrl(), width);
            if (itemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = holder.getLayoutPosition();
                        itemClickListener.ItemClickListener(holder.itemView, pos, lists.get(pos));
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int pos = holder.getLayoutPosition();
                        itemClickListener.ItemLongClickListener(holder.itemView, pos, lists.get(pos));
                        return true;
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}

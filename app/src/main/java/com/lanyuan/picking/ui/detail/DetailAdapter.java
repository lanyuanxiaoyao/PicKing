package com.lanyuan.picking.ui.detail;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lanyuan.picking.R;
import com.lanyuan.picking.common.bean.PicInfo;
import com.lanyuan.picking.util.FrescoUtil;

import java.util.List;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.MyViewHolder> {
    private List<PicInfo> lists;
    private int width;
    private Context context;
    private OnItemClickListener itemClickListener;
    private OnLoveClickListener loveClickListener;

    public DetailAdapter(Context context, List<PicInfo> lists, int width) {
        this.context = context;
        this.lists = lists;
        this.width = width;
    }

    public void addMore(List<PicInfo> data) {
        lists.addAll(data);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void ItemClickListener(View view, int position, PicInfo picInfo);

        void ItemLongClickListener(View view, int position, PicInfo picInfo);
    }

    public void setOnClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public interface OnLoveClickListener {
        void LoveClickListener(View view, int position, PicInfo picInfo);
    }

    public void setOnLoveClickListener(OnLoveClickListener listener) {
        this.loveClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_detail_recycler_view, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView simpleDraweeView;
        public AppCompatImageButton imageButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            simpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.fresco);
            simpleDraweeView.getHierarchy().setProgressBarImage(new ProgressBarDrawable());
            imageButton = (AppCompatImageButton) itemView.findViewById(R.id.love_button);
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        PicInfo p = lists.get(position);
        if (!p.getPicUrl().equals(holder.simpleDraweeView.getTag())) {
            if (p.getWidth() != null && p.getHeight() != null) {
                holder.simpleDraweeView.setImageURI(p.getPicUrl());
                ViewGroup.LayoutParams l = holder.simpleDraweeView.getLayoutParams();
                l.width = p.getWidth();
                l.height = p.getHeight();
                holder.simpleDraweeView.setLayoutParams(l);
            } else
                FrescoUtil.setControllerListener(holder.simpleDraweeView, p, width, true);
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
            if (loveClickListener != null) {
                holder.imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = holder.getLayoutPosition();
                        loveClickListener.LoveClickListener(holder.itemView, pos, lists.get(pos));
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

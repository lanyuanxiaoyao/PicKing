package com.lanyuan.picking.ui.contents;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lanyuan.picking.R;
import com.lanyuan.picking.common.bean.AlbumInfo;
import com.lanyuan.picking.config.AppConfig;
import com.lanyuan.picking.util.FrescoUtil;
import com.lanyuan.picking.util.SPUtils;

import java.util.List;

public class ContentsAdapter extends RecyclerView.Adapter<ContentsAdapter.MyViewHolder> {
    private List<AlbumInfo> lists;
    private int width;
    private Context context;
    private OnItemClickListener itemClickListener;
    private boolean autoGifPlay = false;

    public ContentsAdapter(Context context, List<AlbumInfo> lists, int width) {
        this.context = context;
        this.lists = lists;
        this.width = width;
        autoGifPlay = (boolean) SPUtils.get(context, AppConfig.auto_gif_play, false);
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_contents_recycler_view, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView simpleDraweeView;
        public TextView gifTip;

        public MyViewHolder(View itemView) {
            super(itemView);
            simpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.fresco);
            gifTip = (TextView) itemView.findViewById(R.id.gif_tip);
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        AlbumInfo a = lists.get(position);
        if (!a.getPicUrl().equals(holder.simpleDraweeView.getTag())) {
            if (autoGifPlay)
                setFresco(holder.simpleDraweeView, a, width, true);
            else {
                if (a.getGifThumbUrl() != null && !"".equals(a.getGifThumbUrl())) {
                    holder.gifTip.setVisibility(View.VISIBLE);
                    setFresco(holder.simpleDraweeView, a, width, false);
                } else {
                    holder.gifTip.setVisibility(View.INVISIBLE);
                    setFresco(holder.simpleDraweeView, a, width, true);
                }
            }
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

    private void setFresco(SimpleDraweeView simpleDraweeView, AlbumInfo albumInfo, int width, boolean isGif) {
        if (albumInfo.getWidth() != null && albumInfo.getHeight() != null) {
            if (isGif)
                simpleDraweeView.setImageURI(albumInfo.getPicUrl());
            else
                simpleDraweeView.setImageURI(albumInfo.getGifThumbUrl());
            ViewGroup.LayoutParams l = simpleDraweeView.getLayoutParams();
            l.width = albumInfo.getWidth();
            l.height = albumInfo.getHeight();
            simpleDraweeView.setLayoutParams(l);
        } else
            FrescoUtil.setControllerListener(simpleDraweeView, albumInfo, width, isGif);

    }

    @Override
    public int getItemCount() {
        return lists.size();
    }
}

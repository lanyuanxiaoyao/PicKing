package com.lanyuan.picking.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanyuan.picking.R;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {

    private List<Menu> list;
    private Context context;
    private OnItemClickListener itemClickListener;

    public MenuAdapter(Context context, List<Menu> list) {
        this.context = context;
        this.list = list;
    }

    public interface OnItemClickListener {
        void ItemClickListener(View view, int position);
        void ItemLongClickListener(View view, int position);
    }

    public void setOnClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.menu_item_recycler_view, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.textView.setText(list.get(position).getName());
        if (itemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    itemClickListener.ItemClickListener(holder.itemView, pos);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    itemClickListener.ItemLongClickListener(holder.itemView, pos);
                    return true;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.menu_text);
        }
    }

}

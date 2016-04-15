package com.victor.polskavisa;


import android.app.ActionBar;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class PVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int HEADING_ITEM = 0;
    private static final int NOT_HEADING_ITEM = 1;

    private List<ListItemInterface> dataList;
    private ListItemInterface item;
    private PVViewHolder holder;

    private List<Typeface> fontList;

    private LinearLayout.LayoutParams params;
    private OnItemClickListener listener;


    public class PVViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView itemImage;
        public TextView itemTitle;
        public TextView itemTitleDetail;
        public ImageView actImage;

        public PVViewHolder(View itemView, int i) {
            super(itemView);
            itemView.setClickable(true);
            if (i == NOT_HEADING_ITEM) {
                itemImage = (ImageView) itemView.findViewById(R.id.four_p_list_image);
                itemTitle = (TextView) itemView.findViewById(R.id.list_item_title);
                itemTitleDetail = (TextView) itemView.findViewById(R.id.list_item_title_detail);
                actImage = (ImageView) itemView.findViewById(R.id.four_p_list_act_image);
                itemView.setOnClickListener(this);
            } else {
                itemTitle = (TextView) itemView.findViewById(R.id.rw_title);
            }
        }

        @Override
        public void onClick(View view) {
            listener.onItemClick(view, getPosition());
        }
    }

    public PVAdapter(List<ListItemInterface> dataList, List<Typeface> fontList) {
        this.dataList = dataList;
        this.fontList = fontList;
        this.params = new LinearLayout.LayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        params.setMargins(0, 15, 0, 20);
    }

    @Override
    public int getItemViewType(int position) {
        return dataList.get(position).isHeading() ? HEADING_ITEM : NOT_HEADING_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        int layoutId = i == NOT_HEADING_ITEM ? R.layout.four_possition_row_layout : R.layout.title_layout;
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
        return new PVViewHolder(view, i);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        holder = (PVViewHolder) viewHolder;
        item = dataList.get(i);
        holder.itemTitle.setText(item.getTitle());
        if (!item.isHeading()) {
            holder.itemImage.setImageResource(item.getPicture());
            holder.itemTitleDetail.setText(item.getTitleDetail());
            holder.itemTitle.setTypeface(fontList.get(1));
            holder.itemTitleDetail.setTypeface(fontList.get(1));
            holder.actImage.setImageResource(item.getActPicture());
        } else {
            holder.itemTitle.setTypeface(fontList.get(0));
            if (i == 0)
                holder.itemTitle.setLayoutParams(params);
        }
        holder = null;
        item = null;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}

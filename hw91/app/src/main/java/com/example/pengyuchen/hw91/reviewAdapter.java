package com.example.pengyuchen.hw91;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class reviewAdapter extends RecyclerView.Adapter<reviewAdapter.ViewHolder>{

    private ArrayList<entity2> mData;
    private reviewAdapter.OnItemClickListener onItemClickListener;


    public reviewAdapter(ArrayList<entity2> data) {
        this.mData = data;
    }

    public void updateData(ArrayList<entity2> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(reviewAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // 绑定数据
        final ViewHolder vh = (ViewHolder)  holder;
        final View itemView = vh.itemView;


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(onItemClickListener != null) {
                    int pos = vh.getLayoutPosition();
                    onItemClickListener.onItemClick(itemView, pos);
                }
            }
        });



        Picasso.get().load(mData.get(position).getItem1()).resize(110, 110).into(vh.getTv_Item1());
        vh.getTv_Item2().setText(mData.get(position).getItem2());
        vh.getTv_Item3().setRating(Float.parseFloat(mData.get(position).getItem3()));
        String milSecond = mData.get(position).getItem4();
        if(milSecond.length() == 10){
            Long time = Long.parseLong(milSecond) * 1000L;
            Date date = new Date(time);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            vh.getTv_Item4().setText(format.format(date));
        }else{
            vh.getTv_Item4().setText(milSecond);
        }


        vh.getTv_Item5().setText(mData.get(position).getItem5());
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView tv_sheetRow1;
        public final TextView tv_sheetRow2;
        public final RatingBar tv_sheetRow3;
        public final TextView tv_sheetRow4;
        public final TextView tv_sheetRow5;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            tv_sheetRow1 = (ImageView) itemView.findViewById(R.id.reviewcol1);
            tv_sheetRow2 = (TextView) itemView.findViewById(R.id.reviewcol2);
            tv_sheetRow3 = (RatingBar) itemView.findViewById(R.id.reviewcol3);
            tv_sheetRow4 = (TextView) itemView.findViewById(R.id.reviewcol4);
            tv_sheetRow5 = (TextView) itemView.findViewById(R.id.reviewcol5);


        }

        public ImageView getTv_Item1() {
            return tv_sheetRow1;
        }

        public TextView getTv_Item2() {
            return tv_sheetRow2;
        }

        public RatingBar getTv_Item3() {
            return tv_sheetRow3;
        }

        public TextView getTv_Item4() {
            return tv_sheetRow4;
        }

        public TextView getTv_Item5() {
            return tv_sheetRow5;
        }
    }




}



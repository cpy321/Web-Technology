package com.example.pengyuchen.hw91;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    private ArrayList<entity> mData;
    private MyAdapter.OnItemClickListener onItemClickListener;


    public MyAdapter(ArrayList<entity> data) {
        this.mData = data;
    }

    public void updateData(ArrayList<entity> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemLike(ImageView view, int position);
        void onItemDetail(View view, int position);
    }

    public void setOnItemClickListener(MyAdapter.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

        @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_rv_item, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // 绑定数据
        final ViewHolder vh = (ViewHolder)  holder;
        final ImageView heartView = vh.itemView.findViewById(R.id.resultcol4);
        final View detailView= (View)vh.itemView.findViewById(R.id.detailView);

        heartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(onItemClickListener != null) {
                    int pos = vh.getLayoutPosition();
                    onItemClickListener.onItemLike(heartView, pos);
                }
            }
        });

        detailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if(onItemClickListener != null) {
                    int pos = vh.getLayoutPosition();
                    onItemClickListener.onItemDetail(detailView, pos);
                }
            }
        });


        Picasso.get().load(mData.get(position).getItem1()).resize(110, 110).into(vh.getTv_Item1());
        vh.getTv_Item2().setText(mData.get(position).getItem2());
        vh.getTv_Item3().setText(mData.get(position).getItem3());
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView tv_sheetRow1;
        public final TextView tv_sheetRow2;
        public final TextView tv_sheetRow3;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            tv_sheetRow1 = (ImageView) itemView.findViewById(R.id.resultcol1);
            tv_sheetRow2 = (TextView) itemView.findViewById(R.id.resultcol2);
            tv_sheetRow3 = (TextView) itemView.findViewById(R.id.resultcol3);
        }

        public ImageView getTv_Item1() {
            return tv_sheetRow1;
        }

        public TextView getTv_Item2() {
            return tv_sheetRow2;
        }

        public TextView getTv_Item3() {
            return tv_sheetRow3;
        }
    }




}



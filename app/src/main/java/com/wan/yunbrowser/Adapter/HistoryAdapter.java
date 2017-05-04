package com.wan.yunbrowser.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wan.yunbrowser.Bean.Bookmark;
import com.wan.yunbrowser.R;

import java.util.List;

/**
 * Created by wan on 2017/4/25.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    MyclickListener mlistener;
    public Context ctx;
    public List<Bookmark> BookmarkList;

    public HistoryAdapter(List<Bookmark> list, MyclickListener myclickListener) {
        BookmarkList=list;
        mlistener=myclickListener;
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardview;
        ImageView image;
        TextView title;
        TextView url;

        public ViewHolder(View view){
            super(view);
            cardview= (CardView) view;
            image= (ImageView) view.findViewById(R.id.s_icon);
            title= (TextView) view.findViewById(R.id.s_title);
            url= (TextView) view.findViewById(R.id.s_url);
        }
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(ctx==null){
            ctx=parent.getContext();
        }
        View view= LayoutInflater.from(ctx).inflate(R.layout.simple_layout,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Bookmark bk=BookmarkList.get(position);
                mlistener.urlclick(bk.getUrl());
            }
        });
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Bookmark bk=BookmarkList.get(position);
                mlistener.titleclick(bk.getUrl());
            }
        });
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Bookmark bk=BookmarkList.get(position);
                mlistener.imageclick(bk.getTitle());
            }
        });
        return  holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Bookmark bookmark=BookmarkList.get(position);
        Glide.with(ctx).load(R.drawable.history).into(holder.image);
        holder.title.setText(bookmark.getTitle());
        holder.url.setText(bookmark.getUrl());
    }

    @Override
    public int getItemCount() {
        return BookmarkList.size();
    }

    public interface MyclickListener{
        void urlclick(String url);
        void titleclick(String url);
        void imageclick(String title);
    }
}

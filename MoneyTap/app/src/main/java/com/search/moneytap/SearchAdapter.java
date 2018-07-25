package com.search.moneytap;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    private List<SearchItem> permissionList;
    private Context cxt;
    private Activity act;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, genre;
        public ImageView icon;
        public CardView cardView;
        public SwitchCompat switchCompat;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.permission);
            genre = (TextView) view.findViewById(R.id.genre);
            icon = (ImageView) view.findViewById(R.id.iconPermission);
            cardView = (CardView) view.findViewById(R.id.cardView);
        }
    }


    public SearchAdapter(List<SearchItem> permissionList, Context cxt) {
        this.permissionList = permissionList;
        this.cxt = cxt;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        SearchItem permission = permissionList.get(position);
        holder.title.setText(permission.getTitle());
        holder.genre.setText(permission.getGenre());
        Glide.with(cxt).load(permission.getIcon()).into(holder.icon);


    }
    @Override
    public int getItemCount() {
        return permissionList.size();
    }
}

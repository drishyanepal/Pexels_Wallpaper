package com.example.mywallpaper.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mywallpaper.Interfaces.OnRecyclerViewClickedListener;
import com.example.mywallpaper.Models.Photo;
import com.example.mywallpaper.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WallpaperAdapter extends RecyclerView.Adapter<MyViewHolder> {
    Context context;
    List<Photo> list;
    OnRecyclerViewClickedListener listener;

    public WallpaperAdapter(Context context, List<Photo> list, OnRecyclerViewClickedListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MyViewHolder view = new MyViewHolder(LayoutInflater.from(context).
                inflate(R.layout.wallpaper_card, parent, false));
        return view;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(list.get(position).getSrc().getLarge()).
                placeholder(R.drawable.placeholder).
                into(holder.iv_wallpaperCardImage);
        holder.cv_wallpaperCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(list.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {
    CardView cv_wallpaperCard;
    ImageView iv_wallpaperCardImage;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        cv_wallpaperCard = itemView.findViewById(R.id.cv_wallpaperCard);
        iv_wallpaperCardImage = itemView.findViewById(R.id.iv_wallpaperCardImage);
    }
}
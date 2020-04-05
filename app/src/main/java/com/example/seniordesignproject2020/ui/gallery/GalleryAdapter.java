package com.example.seniordesignproject2020.ui.gallery;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.seniordesignproject2020.R;
import com.example.seniordesignproject2020.core.Scan;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView label;
        TextView date;
        ImageButton delete;
        ImageButton share;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            label = itemView.findViewById(R.id.label);
            date = itemView.findViewById(R.id.date);
            delete = itemView.findViewById(R.id.delete);
            share = itemView.findViewById(R.id.share);
        }
    }

    interface OnItemInteraction {
        void onDelete(int id);
        void onShare(String imgUri, String result);
    }

    private LayoutInflater inflater;
    private List<Scan> items;
    private OnItemInteraction onItemInteraction;

    public GalleryAdapter(Context context, List<Scan> items, @NonNull OnItemInteraction onItemInteraction) {
        this.inflater = LayoutInflater.from(context);
        this.items = items == null ? new ArrayList<Scan>() : items;
        this.onItemInteraction = onItemInteraction;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.gallery_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Scan item = items.get(position);
        holder.imageView.setImageURI(Uri.parse(item.image_location));
        holder.label.setText(item.scan_result);
        holder.date.setText(new Date(item.date * 1000).toString());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemInteraction.onDelete(item.id);
                items.remove(position);
                notifyDataSetChanged();
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemInteraction.onShare(item.image_location, item.scan_result);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

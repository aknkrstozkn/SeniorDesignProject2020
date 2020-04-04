package com.example.seniordesignproject2020.ui.gallery;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.seniordesignproject2020.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {
    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView label;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            label = itemView.findViewById(R.id.label);
        }
    }

    private LayoutInflater inflater;
    private List<GalleryItem> items;

    public GalleryAdapter(Context context, List<GalleryItem> items) {
        this.inflater = LayoutInflater.from(context);
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.gallery_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GalleryItem item = items.get(position);
        holder.imageView.setImageURI(Uri.parse(item.imageUri));
        holder.label.setText(item.label);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

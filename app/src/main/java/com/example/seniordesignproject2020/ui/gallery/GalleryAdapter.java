package com.example.seniordesignproject2020.ui.gallery;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            label = itemView.findViewById(R.id.label);
            date = itemView.findViewById(R.id.date);
        }
    }

    private LayoutInflater inflater;
    private List<Scan> items;

    public GalleryAdapter(Context context, List<Scan> items) {
        this.inflater = LayoutInflater.from(context);
        this.items = items == null ? new ArrayList<Scan>() : items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.gallery_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Scan item = items.get(position);
        holder.imageView.setImageURI(Uri.parse(item.image_location));
        holder.label.setText(item.scan_result);
        holder.date.setText(new Date(item.date).toString());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

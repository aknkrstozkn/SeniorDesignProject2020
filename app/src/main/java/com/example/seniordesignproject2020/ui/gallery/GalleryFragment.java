package com.example.seniordesignproject2020.ui.gallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.seniordesignproject2020.R;
import com.example.seniordesignproject2020.core.Scan;
import com.example.seniordesignproject2020.core.database.DataBase;
import com.example.seniordesignproject2020.util.ShareUtil;

import java.io.File;

public class GalleryFragment extends Fragment {

    private GalleryAdapter itemsAdapter = null;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if(itemsAdapter == null) {
            itemsAdapter = new GalleryAdapter(inflater.getContext(), DataBase.getInstance(getContext()).get_scans(), new GalleryAdapter.OnItemInteraction() {
                @Override
                public void onDelete(int id, String imgUri) {
                    DataBase.getInstance(getContext()).remove_scan(id);

                    File file = new File(Uri.parse(imgUri).getPath());
                    if(file.exists())
                        file.delete();
                }

                @Override
                public void onShare(String imgUri, String result) {
                    ShareUtil.shareScan(getContext(), imgUri, result);
                }
            });
        }

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(inflater.getContext(), LinearLayout.VERTICAL));

        return root;
    }
}

package com.example.seniordesignproject2020.ui.gallery;

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
import com.example.seniordesignproject2020.core.database.DataBase;

public class GalleryFragment extends Fragment {

    private GalleryAdapter itemsAdapter = null;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if(itemsAdapter == null) {
            DataBase db = new DataBase(getContext());
            itemsAdapter = new GalleryAdapter(inflater.getContext(), db.get_scans());
        }

        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(itemsAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(inflater.getContext(), LinearLayout.VERTICAL));

        return root;
    }
}

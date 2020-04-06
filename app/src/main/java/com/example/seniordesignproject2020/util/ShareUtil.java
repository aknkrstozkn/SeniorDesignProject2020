package com.example.seniordesignproject2020.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

import androidx.core.content.FileProvider;

public class ShareUtil {
    public static void shareScan(Context context, String imageUri, String result) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        Uri uri = FileProvider.getUriForFile(
                context, context.getApplicationContext()
                        .getPackageName() + ".provider", new File(Uri.parse(imageUri).getPath()));
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_TEXT, "Result: " + result);
        context.startActivity(intent);
    }
}

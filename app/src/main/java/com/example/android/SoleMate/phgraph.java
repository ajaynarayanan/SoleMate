package com.example.android.SoleMate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class phgraph extends AppCompatActivity {

    private StorageReference mStorageRef, mStorageRef1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phgraph);
        mStorageRef = FirebaseStorage.getInstance().getReference().child("yesterday.jpg");
        PhotoView im1 = (PhotoView) findViewById(R.id.him1);
        Glide.with(this)
                .load(mStorageRef)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(im1);
        mStorageRef1 = FirebaseStorage.getInstance().getReference().child("8.1_5khz.JPG");
        PhotoView im2 = (PhotoView) findViewById(R.id.him2);
        Glide.with(this)
                .load(mStorageRef1)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(im2);
    }
}

package com.example.android.SoleMate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PBG extends AppCompatActivity {

    private StorageReference mStorageRef;
    private ImageView im1;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pbg);

        mStorageRef = FirebaseStorage.getInstance().getReference().child("8.1_5khz.JPG");
        PhotoView im1 = (PhotoView) findViewById(R.id.im1);
            Glide.with(this)
                    .load(mStorageRef)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(im1);

        /*
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            final StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("8.1_5khz.JPG");
            Log.i("user!=null ", "singned");
            im1.setImageURI(null);
            Glide.with(this )
                    .load(storageRef)
                    .into(im1);

        } else {
            Log.i("onCreate: ", "singned");
            mAuth.signInAnonymously().addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {

                    final StorageReference storageRef1 = FirebaseStorage.getInstance().getReference().child("8.1_5khz.JPG");
                    im1.setImageURI(null);
                    Glide.with(getApplicationContext() )
                            .load(storageRef1)
                            .into(im1);
                }
            })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.e("MainActivity", "signFailed****** ", exception);
                        }
                    });
        }
*/

        //FirebaseAuth mAuth = FirebaseAuth.getInstance();

        //FirebaseUser user = mAuth.getCurrentUser();



        /*
        im1.setImageURI(Uri.parse("https://firebasestorage.googleapis.com/v0/b/solemate-123.appspot.com/o/8.1_5khz.JPG?alt=media&token=f81a3bda-1085-4136-9a7d-0152c7e9335b"));

        StorageReference riversRef = mStorageRef.child("8.1_5khz.JPG");

        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                im1.setImageURI(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });*/
    }

}


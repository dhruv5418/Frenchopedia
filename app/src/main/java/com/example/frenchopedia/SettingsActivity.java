package com.example.frenchopedia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseUser curUser;
    CircleImageView imageView;
    EditText edt_fName,edt_lName,edt_email;
    Toolbar toolbar;
    private FirebaseFirestore db;
    TextView txt_level;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        toolbar =findViewById(R.id.toolbar_set);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //toolbar.inflateMenu(R.menu.tool_settings);
        toolbar.setOnMenuItemClickListener(toolListener);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Back",Toast.LENGTH_LONG).show();
                onBackPressed();
            }
        });
        imageView=findViewById(R.id.img_p);
        edt_fName=findViewById(R.id.txt_fName);
        edt_lName=findViewById(R.id.txt_lName);
        edt_email=findViewById(R.id.txt_email);
        txt_level=findViewById(R.id.lbl_level);
        imageView.setOnClickListener(changeProfile);
        loadData();
    }
   void loadData(){
       curUser=auth.getCurrentUser();
       if(curUser!=null){
           if(curUser.getPhotoUrl()!= null){
               Glide.with(getApplicationContext())
                       .load(curUser.getPhotoUrl())
                       .into(imageView);
           }
       }
       readData(new FirestoreCallback() {
           @Override
           public void onClickback(DocumentSnapshot documentSnapshot) {
               edt_fName.setText((CharSequence) documentSnapshot.get("First Name"));
               edt_lName.setText((CharSequence) documentSnapshot.get("Last Name"));
               edt_email.setText((CharSequence) documentSnapshot.get("Email"));
             int a=(int)(long) documentSnapshot.get("Level");
               switch (a){
                   case 1: txt_level.setText("Level:- "+getString(R.string.lvl_1));
                   case 2: txt_level.setText("Level:- "+getString(R.string.lvl_2));
                   case 3: txt_level.setText("Level:- "+getString(R.string.lvl_3));
               }
           }
       });
    }
    private void readData(final SettingsActivity.FirestoreCallback firestoreCallback){
        DocumentReference docref=db.collection("Users").document(curUser.getUid());
        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    documentSnapshot.getData();
                    firestoreCallback.onClickback(documentSnapshot);
                }else{
                    Log.d("Else=","Doc not exist");
                }
            }
        });
    }

    private interface FirestoreCallback{
        void onClickback(DocumentSnapshot documentSnapshot);
    }


    CircleImageView.OnClickListener changeProfile=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
            AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
            builder.setTitle("Add Photo!");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if(options[item].equals("Take Photo")){
                        Intent openCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(openCamera, 1000);
                    }else if(options[item].equals("Choose from Gallery")){
                        Intent openGalary=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(openGalary,1001);
                    }else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            switch (resultCode){
                case RESULT_OK:
                    Bitmap bitmap=(Bitmap)data.getExtras().get("data");
                    imageView.setImageBitmap(bitmap);
                    handleUpload(bitmap);
            }
        }else if(requestCode==1001){

            switch (resultCode){
                case RESULT_OK:
                    Uri imageUri=data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                        imageView.setImageBitmap(bitmap);
                        handleUpload(bitmap);
                    } catch (IOException e) {
                        Log.d("Image Gallary","exception"+e.getMessage());
                        e.printStackTrace();
                    }
            }
        }
    }

    private void handleUpload(Bitmap bitmap) {
        ByteArrayOutputStream baos= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        curUser=auth.getCurrentUser();
        String uId=curUser.getUid();
        final StorageReference reference= FirebaseStorage.getInstance().getReference()
                .child("profileImages")
                .child(uId+".jpeg");
        reference.putBytes(baos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                getDownloadUrl(reference);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Profile Fragment","onFailure",e.getCause());
            }
        });
    }

    private void getDownloadUrl(StorageReference reference){
        reference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("Profile Fragment","onSuccess"+uri);
                        setUserProfile(uri);
                    }
                });
    }

    private void setUserProfile(Uri uri){
        UserProfileChangeRequest request=new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();
        curUser.updateProfile(request)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Updated Successfully",Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Profile Update Unsuccessful",Toast.LENGTH_LONG).show();
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tool_settings, menu);
        return true;
    }

    Toolbar.OnMenuItemClickListener toolListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.done:
                    Toast.makeText(getApplicationContext(),"DOne",Toast.LENGTH_LONG).show();
            }

            return true;
        }
    };

}
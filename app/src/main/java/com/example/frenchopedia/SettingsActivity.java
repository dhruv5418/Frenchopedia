package com.example.frenchopedia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
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
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth auth;
    private FirebaseUser curUser;
    CircleImageView imageView;
    EditText edt_fName,edt_lName;
    Toolbar toolbar;
    Button btn_updateEmail,btn_signOut,btn_resetPass,btn_feedBack,btn_updateLevel;
    private FirebaseFirestore db;
    TextView txt_level,txt_email;
    NavController navController;
    String fName,lName,nEmail,email;
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
        txt_email=findViewById(R.id.txt_email);
        txt_level=findViewById(R.id.lbl_level);
        btn_updateEmail=findViewById(R.id.update_email);
        btn_signOut=findViewById(R.id.btn_logOut);
        btn_resetPass=findViewById(R.id.btn_resetPass);
        btn_feedBack=findViewById(R.id.btn_feedBack);
        btn_updateLevel=findViewById(R.id.update_level);
        btn_updateLevel.setOnClickListener(this);
        btn_feedBack.setOnClickListener(this);
        btn_updateEmail.setOnClickListener(this);
        btn_signOut.setOnClickListener(this);
        btn_resetPass.setOnClickListener(this);
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
               txt_email.setText((CharSequence) documentSnapshot.get("Email"));
               email=txt_email.getText().toString();
             int a=(int)(long) documentSnapshot.get("Level");
               switch (a){
                   case 1: txt_level.setText("Level:- "+getString(R.string.lvl_1));
                            break;
                   case 2: txt_level.setText("Level:- "+getString(R.string.lvl_2));
                            break;
                   case 3: txt_level.setText("Level:- "+getString(R.string.lvl_3));
                            break;
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

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.update_email: updateEmail(v);
                                     break;
            case R.id.btn_logOut: auth.signOut();
                                      intent = new Intent(getApplicationContext(), MainActivity.class);
                                     startActivity(intent);
                                     break;
            case R.id.btn_resetPass: reset(email);
                                     break;
            case R.id.btn_feedBack: sendFeedback();
                                     break;
            case R.id.update_level: updateLevel(v);
                                     break;
        }
    }

    private void updateLevel(View v) {
        final View popupview=getLayoutInflater().inflate(R.layout.popupview1,null);
        final PopupWindow popupWindow=new PopupWindow(popupview, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        if(Build.VERSION.SDK_INT>=21){
            popupWindow.setElevation(5.0f);
        }
        final Button btn_next;
        final RadioGroup selectLvl;
        //final RadioButton radio_button;

        //Log.d("Setting","email="+nEmail);
         btn_next=popupview.findViewById(R.id.btn_lvl);
        selectLvl=popupview.findViewById(R.id.selectLevel);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.orenge_button)));
        popupWindow.showAtLocation(v, Gravity.CENTER,0,0);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = selectLvl.getCheckedRadioButtonId();
                int set;
                //radio_button=popupview.findViewById(selectedId);
                Map<String,Object> usermap=new HashMap<>();
                if(selectedId==R.id.lvl_1){
                    set=1;
                }else if(selectedId==R.id.lvl_2){
                    set=2;
                }else{
                    set=3;
                }
                usermap.put("Level",set);
                db.collection("Users").document(curUser.getUid()).update(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Level updated successfully",Toast.LENGTH_LONG);
                            popupWindow.dismiss();
                            loadData();
                           /* navController= Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
                            auth.signOut();
                            navController.navigate(R.id.loginfragment);*/
                        }else{
                            Log.d("Select Level Fragment","onFailure: Level Selection"+task.getException().getMessage());
                        }
                    }
                });
            }
        });

    }

    private void sendFeedback() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL  , new String[] { "dhruvj5418@gmail.com" });
        intent.putExtra(Intent.EXTRA_SUBJECT, "Frenchopedia Feedback");

        startActivity(Intent.createChooser(intent, "Email via..."));
    }

    private void reset(String email) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Email sent successfully!",Toast.LENGTH_LONG).show();
                    auth.signOut();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void updateEmail(View v) {
        View popupview=getLayoutInflater().inflate(R.layout.popupview,null);
        final PopupWindow popupWindow=new PopupWindow(popupview, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        if(Build.VERSION.SDK_INT>=21){
            popupWindow.setElevation(5.0f);
        }
        final EditText edtPEmail=popupview.findViewById(R.id.edt_popemail);
        final EditText edtPPass=popupview.findViewById(R.id.edt_poppassword);
        final EditText edtNEmail=popupview.findViewById(R.id.edt_newEmail);

        //Log.d("Setting","email="+nEmail);
        Button btnSubmit=popupview.findViewById(R.id.btn_poplogin);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.orenge_button)));
        popupWindow.showAtLocation(v, Gravity.CENTER,0,0);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(edtPEmail.getText().toString())){
                    edtPEmail.setError("Email cannot be blank!");
                    edtPEmail.requestFocus();
                }else if(TextUtils.isEmpty(edtPPass.getText().toString())) {
                    edtPPass.setError("Password cannot be blank!");
                    edtPPass.requestFocus();
                }else if(TextUtils.isEmpty(edtNEmail.getText().toString())) {
                    edtPPass.setError("New Email cannot be blank!");
                    edtPPass.requestFocus();
                }else{
                    if(edtPPass.getText().toString().length()<6){
                        edtPPass.setError("Invalid password,Should be at least 6 characters");
                        edtPPass.requestFocus();
                    } else {
                        AuthCredential credential= EmailAuthProvider.getCredential(edtPEmail.getText().toString(),edtPPass.getText().toString());
                        curUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    nEmail=edtNEmail.getText().toString();
                                    Log.d("Setting","email="+nEmail);
                                    curUser.updateEmail(nEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Log.d("Exception", "log Succ: ");
                                                curUser=auth.getCurrentUser();
                                                Map<String,Object> usermap=new HashMap<>();
                                                usermap.put("Email",nEmail);
                                                //int a=0;
                                                //usermap.put("Level",a);
                                                db.collection("Users").document(curUser.getUid()).update(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(getApplicationContext(),"Email Updated Successfully!",Toast.LENGTH_LONG).show();
                                                            popupWindow.dismiss();
                                                            loadData();
                                                            //Toast.makeText(getActivity().getApplicationContext(),"Please verify your Email address!",Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                            }else{
                                                try{
                                                throw task.getException();
                                            } catch (FirebaseAuthUserCollisionException already) {
                                                Toast.makeText(getApplicationContext(),"Email Already Exist!",Toast.LENGTH_LONG).show();
                                                edtNEmail.getText().clear();
                                                edtNEmail.requestFocus();
                                                edtNEmail.setError("Enter Other Emil");
                                                edtPEmail.getText().clear();
                                                edtPPass.getText().clear();

                                            }
                                            catch (Exception e){
                                                Log.d("Exception", "onComplete: " + e.getMessage());
                                                Toast.makeText(getApplicationContext(),"Register Failed!",Toast.LENGTH_LONG).show();
                                            }

                                            }
                                        }
                                    });
                                }else {
                                    Log.d("Setting Activity","onFailure: Authentication"+task.getException().getMessage());
                                    edtPEmail.getText().clear();
                                    edtPPass.getText().clear();
                                    edtPEmail.setError("Please enter correct details");
                                }
                            }
                        });
                    }
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
                                fName=edt_fName.getText().toString();
                                lName=edt_lName.getText().toString();
                                //email=edt_email.getText().toString();

                             if(!checkEmptyField()){
                                updateData();
                             }

                    Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_LONG).show();
            }

            return true;
        }
    };
    public  boolean checkEmptyField(){
       if(TextUtils.isEmpty(fName)){
            edt_fName.setError("Name cannot be blank!");
            edt_fName.requestFocus();
            return true;
        }else if(TextUtils.isEmpty(lName)){
            edt_lName.setError("Name cannot be blank!");
            edt_lName.requestFocus();
            return true;
        } /*else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edt_email.setError("Wrong Email Format!");
            edt_email.getText().clear();
            edt_email.requestFocus();
            return true;
        }*/
        else{
            return false;
        }
    }

    public void updateData(){
        curUser=auth.getCurrentUser();
        Map<String,Object> usermap=new HashMap<>();
        usermap.put("First Name",fName);
        usermap.put("Last Name",lName);
        //usermap.put("Email",email);
        //int a=0;
        //usermap.put("Level",a);
        db.collection("Users").document(curUser.getUid()).update(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"Updated Successfully!",Toast.LENGTH_LONG).show();
                    loadData();
                    //Toast.makeText(getActivity().getApplicationContext(),"Please verify your Email address!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
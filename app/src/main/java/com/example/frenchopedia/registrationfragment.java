package com.example.frenchopedia;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class registrationfragment extends Fragment implements View.OnClickListener {

    EditText edt_fName,edt_lName,edt_email,edt_pass,edt_cPass;
    Button btn_reg;
    TextView txt_login;
    NavController navController;
    String fName,lName,email,pass,cPass;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    public registrationfragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registrationfragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_reg=view.findViewById(R.id.btn_registration);
        txt_login=view.findViewById(R.id.txt_regLogin);
        edt_fName=view.findViewById(R.id.reg_fname);
        edt_lName=view.findViewById(R.id.reg_lname);
        edt_email=view.findViewById(R.id.reg_email);
        edt_pass=view.findViewById(R.id.reg_pass);
        edt_cPass=view.findViewById(R.id.reg_cPass);
        btn_reg.setOnClickListener(this);
        txt_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.btn_registration){
        fName=edt_fName.getText().toString();
        lName=edt_lName.getText().toString();
        email=edt_email.getText().toString();
        pass=edt_pass.getText().toString();
        cPass=edt_cPass.getText().toString();
            if(!checkEmptyField()){
                if(pass.length()<6){
                    edt_pass.setError("Invalid Password,minimum 6 characters");
                    edt_pass.requestFocus();
                    return;
                }else{
                    if(!pass.equals(cPass)){
                        edt_cPass.setError("Password not match!");
                        edt_cPass.requestFocus();
                        return;
                    }else{
                        register();
                    }
                }
            }else{
                return;
            }
        }else if(id==R.id.txt_regLogin){
            navController= Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
            navController.navigate(R.id.loginfragment);

        }
    }


    public  boolean checkEmptyField(){
        if(TextUtils.isEmpty(email)){
            edt_email.setError("Email cannot be blank!");
            edt_email.requestFocus();
            return true;
        }else if(TextUtils.isEmpty(pass)){
            edt_pass.setError("Password cannot be blank!");
            edt_pass.requestFocus();
            return true;
        }else if(TextUtils.isEmpty(cPass)){
            edt_cPass.setError("Confirm Password cannot be blank!");
            edt_cPass.requestFocus();
            return true;
        }else if(TextUtils.isEmpty(fName)){
            edt_fName.setError("Name cannot be blank!");
            edt_fName.requestFocus();
            return true;
        }else if(TextUtils.isEmpty(lName)){
            edt_lName.setError("Name cannot be blank!");
            edt_lName.requestFocus();
            return true;
        } else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            edt_email.setError("Wrong Email Format!");
            edt_email.getText().clear();
            edt_email.requestFocus();
            return true;
        }
        else{
            return false;
        }
    }

    public void register(){

        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    try{
                        throw task.getException();
                    } catch (FirebaseAuthUserCollisionException already) {
                        Toast.makeText(getActivity().getApplicationContext(),"User Already Exist!",Toast.LENGTH_LONG).show();
                        navController= Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
                        navController.navigate(R.id.loginfragment);
                    }
                    catch (Exception e){
                        Log.d("Exception", "onComplete: " + e.getMessage());
                        Toast.makeText(getActivity().getApplicationContext(),"Register Failed!",Toast.LENGTH_LONG).show();
                    }
                    edt_email.getText().clear();
                    edt_pass.getText().clear();
                    edt_cPass.getText().clear();
                    edt_fName.getText().clear();
                    edt_lName.getText().clear();
                    edt_email.requestFocus();
                    return;
                }else{
                    FirebaseUser user=auth.getCurrentUser();

                    Map<String,Object> usermap=new HashMap<>();
                    usermap.put("First Name",fName);
                    usermap.put("Last Name",lName);
                    usermap.put("Email",email);
                    int a=0;
                    usermap.put("Level",a);
                    db.collection("Users").document(user.getUid()).set(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getActivity().getApplicationContext(),"Register Success!",Toast.LENGTH_LONG).show();
                                //Toast.makeText(getActivity().getApplicationContext(),"Please verify your Email address!",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                  /*  FirebaseAuth.getInstance().signOut();
                    //NavController navController= Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
                    navController.navigate(R.id.loginfragment);*/
                    navController= Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
                  navController.navigate(R.id.selectlevelFragment);
                }
            }
        });

    }



}

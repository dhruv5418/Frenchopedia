package com.example.frenchopedia;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class emailverifyfragment extends Fragment implements View.OnClickListener {

   private FirebaseAuth auth;
   private Button btn_send;
   private TextView txt_frgtLogin;
   private EditText edt_email;
   NavController navController;
   String email;

    public emailverifyfragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_emailverifyfragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_send=view.findViewById(R.id.btn_frgtPass);
        txt_frgtLogin=view.findViewById(R.id.txt_frgtLogin);
        edt_email=view.findViewById(R.id.edit_restPassEmail);
        btn_send.setOnClickListener(this);
        txt_frgtLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.btn_frgtPass){
            email=edt_email.getText().toString();
            if(TextUtils.isEmpty(email)){
                edt_email.setError("Please enter email!");
                edt_email.requestFocus();
            }else{
                reset(email);
            }
        }else if(id==R.id.txt_frgtLogin){
            navController= Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
            navController.navigate(R.id.loginfragment);
        }

    }

    private void reset(String email) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getActivity().getApplicationContext(),"Email sent successfully!",Toast.LENGTH_LONG).show();
                    navController= Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
                    navController.navigate(R.id.loginfragment);
                }
            }
        });
    }
}

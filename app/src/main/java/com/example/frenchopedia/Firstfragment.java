package com.example.frenchopedia;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.login.LoginFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Firstfragment extends Fragment {
    NavController navController;
    Button btn_getStarted,btn_login;
    FirebaseAuth auth;
    FirebaseUser curUser;
    public Firstfragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_firstfragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
        btn_getStarted=view.findViewById(R.id.btn_getStarted);
        btn_login=view.findViewById(R.id.btn_firstLogin);
        btn_getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.registrationfragment);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.loginfragment);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        curUser=auth.getCurrentUser();
        if(curUser!=null){
            updateUI(curUser);
            Toast.makeText(getActivity().getApplicationContext(),"User Already Login",Toast.LENGTH_LONG).show();
        }
    }

    public void updateUI(FirebaseUser fUser){
        //NavController nv=Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
        // navController= Navigation.findNavController(getActivity(),R.id.nav_host_fragment);
        Intent intent = new Intent(getActivity(), Dashboard.class);
        intent.putExtra("User",fUser);
        startActivity(intent);
       /* Bundle bundle=new Bundle();
        bundle.putParcelable("User",fUser);
        //navController.navigate(R.id.dashboardfragment,bundle);*/
    }
}

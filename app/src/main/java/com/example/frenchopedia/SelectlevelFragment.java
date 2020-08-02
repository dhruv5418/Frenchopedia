package com.example.frenchopedia;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class SelectlevelFragment extends Fragment {

    Button btn_next;
    RadioGroup selectLvl;
    RadioButton radio_button;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private FirebaseUser curUser;
    NavController navController;

    public SelectlevelFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        curUser=auth.getCurrentUser();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selectlevel, container, false);
    }

    @Override
    public void onStop() {
        super.onStop();
        //auth.signOut();
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_next=view.findViewById(R.id.btn_lvl);
        selectLvl=view.findViewById(R.id.selectLevel);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = selectLvl.getCheckedRadioButtonId();
                int set;
                radio_button=view.findViewById(selectedId);
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
                    Toast.makeText(getActivity().getApplicationContext(),"Level selected successfully",Toast.LENGTH_LONG);
                    Intent intent = new Intent(getActivity(), Dashboard.class);
                    intent.putExtra("User",curUser);
                    startActivity(intent);
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
}

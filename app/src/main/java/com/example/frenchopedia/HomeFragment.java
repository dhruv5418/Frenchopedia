package com.example.frenchopedia;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class HomeFragment extends Fragment implements View.OnClickListener {

    String data="<div id=\"widget\";\"><iframe src=\"https://www.innovativelanguage.com/widgets/wotd/embed.php?language=French&type=small&bg=url%28/widgets/wotd/skin/images/small/Cherry_Blossom.png%29%20no-repeat%200%200&content=%23000&header=%23DBB9D4&highlight=%23D973BE&opacity=.15&scrollbg=%23D991C6&sound=%23D973BE&text=%23BF43A5&quiz=N\" width=\"360\" height=\"190\" frameborder=\"0\" scrolling=\"no\"></iframe></div>";
    WebView webView;
    private FirebaseAuth auth;
    private FirebaseUser curUser;
    private FirebaseFirestore db;
    NavController navController;
    Toolbar toolbar;
    TextView txt_lvl1,txt_lvl2;
    LinearLayout layoutPractice,layoutQuiz;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        setHasOptionsMenu(true);
    }

    private void loadData() {
        readData(new FirestoreCallback() {
            @Override
            public void onClickback(DocumentSnapshot documentSnapshot) {
                int a=(int)(long) documentSnapshot.get("Level");
                switch (a){
                    case 1: txt_lvl1.setText("Level:- "+getString(R.string.lvl_1));
                            txt_lvl2.setText("Level:- "+getString(R.string.lvl_1));
                        break;
                    case 2: txt_lvl1.setText("Level:- "+getString(R.string.lvl_2));
                            txt_lvl2.setText("Level:- "+getString(R.string.lvl_2));
                        break;
                    case 3: txt_lvl1.setText("Level:- "+getString(R.string.lvl_3));
                            txt_lvl2.setText("Level:- "+getString(R.string.lvl_3));
                        break;
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView=view.findViewById(R.id.webView);
        toolbar=view.findViewById(R.id.toolbar_home);
        toolbar.inflateMenu(R.menu.tool_home);
        toolbar.setOnMenuItemClickListener(toolListner);
        layoutPractice=view.findViewById(R.id.layout_practice);
        layoutQuiz=view.findViewById(R.id.layout_quiz);
        layoutQuiz.setOnClickListener(this);
        layoutPractice.setOnClickListener(this);
        txt_lvl1=view.findViewById(R.id.txt_lvl1);
        txt_lvl2=view.findViewById(R.id.txt_lvl2);
        loadData();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadData(data , "text/html" , null);

    }

    private Toolbar.OnMenuItemClickListener toolListner=new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            Intent intent;
            switch (item.getItemId()){
                case(R.id.translator):   intent= new Intent(getActivity(),TranslationActivity.class);
                                        startActivity(intent);
                                        break;
                case(R.id.noteBook):     intent= new Intent(getActivity(),NotesActivity.class);
                                        startActivity(intent);
                                        break;
            }
            return true;
        }
    };


    private void readData(final FirestoreCallback firestoreCallback){
        curUser=auth.getCurrentUser();
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
            case R.id.layout_practice:
                                        intent=new Intent(getActivity(),PracticeActivity.class);
                                        startActivity(intent);
                                        break;
            case R.id.layout_quiz:
                                        intent=new Intent(getActivity(),QuizActivity.class);
                                        startActivity(intent);
                                        break;

        }

    }

    private interface FirestoreCallback{
        void onClickback(DocumentSnapshot documentSnapshot);
    }



}

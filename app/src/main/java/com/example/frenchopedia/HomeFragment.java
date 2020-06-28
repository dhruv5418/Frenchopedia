package com.example.frenchopedia;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class HomeFragment extends Fragment {

    String data="<div id=\"widget\";\"><iframe src=\"https://www.innovativelanguage.com/widgets/wotd/embed.php?language=French&type=small&bg=url%28/widgets/wotd/skin/images/small/Cherry_Blossom.png%29%20no-repeat%200%200&content=%23000&header=%23DBB9D4&highlight=%23D973BE&opacity=.15&scrollbg=%23D991C6&sound=%23D973BE&text=%23BF43A5&quiz=N\" width=\"360\" height=\"190\" frameborder=\"0\" scrolling=\"no\"></iframe></div>";
    WebView webView;
    private FirebaseAuth auth;
    private FirebaseUser curUser;
    NavController navController;
    Toolbar toolbar;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
        setHasOptionsMenu(true);
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
        Button b=view.findViewById(R.id.logout);
        toolbar=view.findViewById(R.id.toolbar_home);
        toolbar.inflateMenu(R.menu.tool_home);
        toolbar.setOnMenuItemClickListener(toolListner);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadData(data , "text/html" , null);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
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

}

package com.example.frenchopedia;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class HomeFragment extends Fragment {

    String data="<div id=\"widget\" style=\"width:460px;height:190px;margin:0 auto;\"><iframe src=\"https://www.innovativelanguage.com/widgets/wotd/embed.php?language=French&type=small&bg=url%28/widgets/wotd/skin/images/small/Cherry_Blossom.png%29%20no-repeat%200%200&content=%23000&header=%23DBB9D4&highlight=%23D973BE&opacity=.15&scrollbg=%23D991C6&sound=%23D973BE&text=%23BF43A5&quiz=N\" width=\"160\" height=\"190\" frameborder=\"0\" scrolling=\"no\"></iframe><div style=\"font:bold 9px/16px Verdana; padding:0; height:16px;\"><div style=\"float:left; margin:0;\"><a href=\"https://www.frenchpod101.com/french-phrases/\" target=\"_parent\" title=\"Get Widget\" style=\"font-family: Helvetica, Arial, sans-serif;font-size: 11px;color: #00ACED;\" rel=\"nofollow\">Get Widget</a></div><div style=\"float:right; margin:0;\"><a href=\"https://www.frenchpod101.com\" target=\"_parent\" title=\"Learn French\" style=\"font-family: Helvetica, Arial, sans-serif;font-size: 11px;color: #00ACED;\" rel=\"nofollow\">Learn French</a></div></div></div>";
    WebView webView;

    private FirebaseAuth auth;
    private FirebaseUser curUser;
    NavController navController;
    public HomeFragment() {
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView=view.findViewById(R.id.webView);
        Button b=view.findViewById(R.id.logout);
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
}

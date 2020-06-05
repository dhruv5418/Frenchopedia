package com.example.frenchopedia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
/*import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;*/

import java.io.IOException;
import java.io.InputStream;

public class TranslationActivity extends AppCompatActivity implements View.OnClickListener {
    EditText edt_textToTranslate,edt_translatedText;
    Button swap,b_translate;
    Toolbar toolbar;
    String originalText;
    String translatedText;
    String language="fr";
    private boolean connected;
   // Translate translate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translation);
        toolbar= findViewById(R.id.tool_trans);
        edt_textToTranslate=findViewById(R.id.textToTranslate);
        edt_translatedText=findViewById(R.id.translatedText);
        b_translate=findViewById(R.id.btn_translate);
        swap=findViewById(R.id.btn_swap);
        b_translate.setOnClickListener(this);
        swap.setOnClickListener(this);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.orenge_button));
        getSupportActionBar().setTitle("Translation");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(intent);
                /*NavController navController= Navigation.findNavController(Dashboard.this,R.id.nav_dashboard);
                navController.navigate(R.id.homeFragment);*/
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.btn_swap:
                        swap();
                break;
            case R.id.btn_translate:
                  //  translate();
                break;
        }
    }

    private void swap() {

        if(language.equals("fr")){
            language="en";
            edt_textToTranslate.setHint("Enter Text (French)");
            edt_translatedText.setHint("Translated Text (English)");
        }else {
            language="fr";
            edt_textToTranslate.setHint("Enter Text (English)");
            edt_translatedText.setHint("Translated Text (French)");
        }
        Toast.makeText(getApplicationContext(),"Language swapped successfully",Toast.LENGTH_LONG).show();
    }

   /* public void translate(){
        if (checkInternetConnection()) {

            //If there is internet connection, get translate service and start translation:
            getTranslateService();
            getTranslatedText();

        } else {
            final Snackbar snackbar=Snackbar.make(findViewById(R.id.translatorView),"No Internet Connection!",Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("RETRY!", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!checkInternetConnection()){
                        snackbar.dismiss();
                    }
                }
            });
            snackbar.show();
        }
    }


    public void getTranslateService() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try (InputStream is = getResources().openRawResource(R.raw.credentials)) {

            //Get credentials:
            final GoogleCredentials myCredentials = GoogleCredentials.fromStream(is);

            //Set credentials and get translate service:
            TranslateOptions translateOptions = TranslateOptions.newBuilder().setCredentials(myCredentials).build();
            translate = translateOptions.getService();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    public void getTranslatedText() {

        //Get input text to be translated:
        originalText = edt_textToTranslate.getText().toString();
        Translation translation = translate.translate(originalText, Translate.TranslateOption.targetLanguage(language), Translate.TranslateOption.model("base"));
        translatedText = translation.getTranslatedText();

        //Translated text and original text are set to TextViews:
        edt_translatedText.setText(translatedText);

    }

    public boolean checkInternetConnection() {

        //Check internet connection:
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //Means that we are connected to a network (mobile or wi-fi)
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        return connected;
    }*/
}

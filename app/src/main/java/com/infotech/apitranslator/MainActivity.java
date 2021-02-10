package com.infotech.apitranslator;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.IOException;
import java.io.InputStream;
import java.security.Permission;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements bottomsheetfragment.ItemClickListener, MyDialogFragment.DialogListener,AsBottomFrag.ExampleDialogListener{


    SharedPreferences sharedpreferences;
    private EditText startdate,titleleave,endate1,remarks,shifting1;
    private String originalText;
    private String translatedText;
    private boolean connected;
    Translate translate;
    Button anotheractivity,voiceChangerSetting;
    public static final String MyPREFERENCES = "MyPrefs" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        startdate = findViewById(R.id.startdate);
        titleleave=findViewById(R.id.titleleave);
        endate1=findViewById(R.id.endate);
        remarks=findViewById(R.id.remarks);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        shifting1=findViewById(R.id.shifting);
        voiceChangerSetting=findViewById(R.id.voiceChangerSetting);
        ImageButton translateButton = findViewById(R.id.translateButton);
        anotheractivity=findViewById(R.id.anotheractivity);
        anotheractivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ab=new Intent(MainActivity.this,NotesActivity.class);
                startActivity(ab);
                finish();

            }
        });
        voiceChangerSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String gender = sharedpreferences.getString("gender", "male");
                String seekbar = sharedpreferences.getString("seekvalue", "1.0");


                MyDialogFragment   dialogFragment = new MyDialogFragment();

                Bundle bundle = new Bundle();

                bundle.putString("gender",gender);
                bundle.putString("seekbar",seekbar);
                dialogFragment.setArguments(bundle);

                FragmentTransaction     ft = getSupportFragmentManager().beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);


                dialogFragment.show(ft, "dialog");
            }
        });

        translateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(MainActivity.this).withPermissions(Manifest.permission.RECORD_AUDIO)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                String gender = sharedpreferences.getString("gender", "male");
                                String seekbar = sharedpreferences.getString("seekvalue", "1.0");
                                Bundle bundle=new Bundle();
                                bundle.putString("gender",gender);
                                bundle.putString("seekbar",seekbar);
                                AsBottomFrag addPhotoBottomDialogFragment =
                                        AsBottomFrag.newInstance();
                                addPhotoBottomDialogFragment.setArguments(bundle);
                                addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                                        AsBottomFrag.TAG);
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                            }
                        }).check();

//                if (checkInternetConnection()) {
//
//                    //If there is internet connection, get translate service and start translation:
//                    getTranslateService();
//                    translate();
//
//                } else {
//
//                    //If not, display "no connection" warning:
//                    translatedTv.setText("No connection ");
//                }





            }
        });
    }

    public void getTranslateService() {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try (InputStream is = getResources().openRawResource(R.raw.creditinals)) {

            //Get credentials:
            final GoogleCredentials myCredentials = GoogleCredentials.fromStream(is);

            //Set credentials and get translate service:
            TranslateOptions translateOptions = TranslateOptions.newBuilder().setCredentials(myCredentials).build();
            translate = translateOptions.getService();

        } catch (IOException ioe) {
            ioe.printStackTrace();

        }

    }

    public void translate(String originalText) {

        //Get input text to be translated:

      // --- Translation translation = translate.translate(originalText, Translate.TranslateOption.targetLanguage("en"), Translate.TranslateOption.model("base"));
    //---    translatedText = translation.getTranslatedText();

        //Translated text and original text are set to TextViews:
//        String input=translatedText;
//        String regex="\\d{2} \\w{0,3} \\d{4}";
//        Pattern m= Pattern.compile(regex);
//        Matcher matcher=m.matcher(input);
//        if(matcher.find())
//        {
//            String dataString=matcher.group(1);
//            translatedTv.setText(dataString);
//        }
//        else
//        {
//
//            Toast.makeText(this, "could not find", Toast.LENGTH_SHORT).show();
//        }
        startdate.setText(originalText);



    }

    public boolean checkInternetConnection() {

        //Check internet connection:
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        //Means that we are connected to a network (mobile or wi-fi)
        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        return connected;
    }

    @Override
    public void onItemClick(String item) {
        Toast.makeText(this, ""+item, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startdate(String username) {
        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(MainActivity.this,R.style.Theme_Design_BottomSheetDialog);
        bottomSheetDialog.dismiss();
        if (checkInternetConnection()) {

            //If there is internet connection, get translate service and start translation:
            getTranslateService();
            translate(username);

        } else {

            //If not, display "no connection" warning:
            startdate.setText("No connection ");
        }
    }

    @Override
    public void sendtitle(String title) {
        titleleave.setText(title);

    }

    @Override
    public void endate(String endate) {

        endate1.setText(endate);

    }

    @Override
    public void reason(String message) {
        remarks.setText(message);
    }

    @Override
    public void shifting(String shifting) {
        if(shifting.toLowerCase().contains("morning"))
        shifting1.setText("9.00-1.00PM");
        else
            shifting1.setText("1.00-6.00PM");
    }


    @Override
    public void seekbarvalue(String inputText) {
        String split[]=inputText.split(" ");
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString("seekvalue", split[1]);
        editor.putString("gender", split[0]);

        editor.apply();
        Toast.makeText(this, split[0]+"=="+split[1]+"----tanku", Toast.LENGTH_SHORT).show();
    }
}

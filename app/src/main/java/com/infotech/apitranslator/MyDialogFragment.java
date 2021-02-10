package com.infotech.apitranslator;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;


public class MyDialogFragment extends DialogFragment {
    private DialogListener dialogListener;
    static TextToSpeech t1;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        String gender=getArguments().getString("gender");
        Toast.makeText(getContext(), "---"+gender, Toast.LENGTH_SHORT).show();
        return inflater.inflate(R.layout.fragment_my_dialog, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Float[] progress1 = new Float[1];
        SeekBar seekBar;
        final RadioButton[] genderradioButton = new RadioButton[1];
        final RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        seekBar = view.findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {
                    float progressid=(float) progress/5;
                    Toast.makeText(getContext(), "seekbar progress: " + progressid+"--", Toast.LENGTH_SHORT).show();

                    progress1[0] =progressid;
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

        Button btnDone = view.findViewById(R.id.btnDone);
        ImageButton testmale=view.findViewById(R.id.malevoice);
        ImageButton testfemale=view.findViewById(R.id.femalevoice);
        testfemale.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              t1 = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
                                                  @Override
                                                  public void onInit(int status) {
                                                      if (status == TextToSpeech.SUCCESS) {
                                                          Set<String> a = new HashSet<>();
                                                          a.add("female");//here you can give male if you want to select male voice.
                                                          //Voice v=new Voice("en-us-x-sfg#female_2-local",new Locale("en","US"),400,200,true,a);
                                                          Voice v = new Voice("en-us-x-sfg#female-local", new Locale("en", "US"), 400, 200, true, a);
                                                          t1.setVoice(v);
                                                          t1.setSpeechRate(progress1[0]);

                                                          // int result = T2S.setLanguage(Locale.US);
                                                          int result = t1.setVoice(v);

                                                          if (result == TextToSpeech.LANG_MISSING_DATA
                                                                  || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                                              Log.e("TTS", "This Language is not supported");
                                                          }
                                                          else
                                                              t1.speak("text", TextToSpeech.QUEUE_FLUSH, null, "eiy");

                                                      } else {
                                                          Log.e("TTS", "Initilization Failed!");
                                                      }
                                                  }
                                              });
                                          }
                                      }
        );
        testmale.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            t1 = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
                                                @Override
                                                public void onInit(int status) {
                                                    if (status == TextToSpeech.SUCCESS) {
                                                        Set<String> a = new HashSet<>();
                                                        a.add("male");//here you can give male if you want to select male voice.
                                                        //Voice v=new Voice("en-us-x-sfg#female_2-local",new Locale("en","US"),400,200,true,a);
                                                        Voice v = new Voice("en-us-x-sfg#male_2-local", new Locale("en", "US"), 400, 200, true, a);
                                                        t1.setVoice(v);
                                                        t1.setSpeechRate(progress1[0]);

                                                        // int result = T2S.setLanguage(Locale.US);
                                                        int result = t1.setVoice(v);

                                                        if (result == TextToSpeech.LANG_MISSING_DATA
                                                                || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                                            Log.e("TTS", "This Language is not supported");
                                                        }
                                                        else {
                                                            t1.speak("text", TextToSpeech.QUEUE_FLUSH, null, "eiy");
                                                        }

                                                    } else {
                                                        Log.e("TTS", "Initilization Failed!");
                                                    }
                                                }
                                            });
                                        }
                                    });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int selectedId = radioGroup.getCheckedRadioButtonId();

                if(selectedId==2131230897)
                {
                    DialogListener dialogListener = (DialogListener) getActivity();
                    dialogListener.seekbarvalue("male"+" "+progress1[0]);
                    dismiss();

                }
                else
                {
                    DialogListener dialogListener = (DialogListener) getActivity();
                    dialogListener.seekbarvalue("female"+" "+progress1[0]);
                    dismiss();
                }


            //    dismiss();
            }
        });
        }







    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("API123", "onCreate");

        boolean setFullScreen = false;
        if (getArguments() != null) {
            setFullScreen = getArguments().getBoolean("fullScreen");
        }

        if (setFullScreen)
            setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public interface DialogListener {
         void seekbarvalue(String inputText);
    }


}
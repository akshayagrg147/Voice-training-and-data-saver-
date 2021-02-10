package com.infotech.apitranslator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.dnkilic.waveform.WaveView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class noteFragment extends BottomSheetDialogFragment implements RecognitionListener {
    private WaveView mWaveView;



    @Override
    public void onError(int i) {
        String errorMessage = getErrorText(i);
        Log.d(LOG_TAG, "FAILED " + errorMessage);


        mWaveView.speechPaused();
        //    mWaveView.stop();

        //    progressBar.setIndeterminate(false);
        //    progressBar.setVisibility(View.INVISIBLE);

        speech.destroy();

        t1.speak("Oops,I dont get it", TextToSpeech.QUEUE_FLUSH, null, "eiy");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                t1.speak("Lets try again", TextToSpeech.QUEUE_FLUSH, null, "eiy");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fn();
                    }
                }, 1500);
            }
        }, 1500);


    }


    private NoteDialohListener listener;
    TextToSpeech t1;
    static int x = 0;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "VoiceRecognitionActivity";
    public static String TAG = "bottom";
    String pardtdone = "prartdone";

    public noteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_note, container, false);
        mWaveView = view.findViewById(R.id.vw21);

        t1 = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.getDefault());
                }
            }
        });
        //    progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);


        fn();


        return view;
    }

    private void fn() {
        //    progressBar.setVisibility(View.INVISIBLE);
        speech = SpeechRecognizer.createSpeechRecognizer(this.getContext());
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, Locale.getDefault());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getActivity().getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getActivity().getPackageName());

        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);


//                    progressBar.setVisibility(View.VISIBLE);
//                    progressBar.setIndeterminate(true);
        t1 = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i != TextToSpeech.ERROR)
                    t1.setLanguage(Locale.ENGLISH);
                if (!pardtdone.equals("complete1"))
                    t1.speak("Hi,What you want to store?", TextToSpeech.QUEUE_FLUSH, null, "eiy");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        speech.startListening(recognizerIntent);
                    }
                }, 1500);


            }
        });


    }

    public static noteFragment newInstance() {
        return new noteFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (NoteDialohListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (speech != null) {


            speech.destroy();
            Log.i(LOG_TAG, "destroy");
        }
    }

    //    @Override
//    protected void onPause() {
//        super.onPause();
//        if (speech != null) {
//            speech.destroy();
//            Log.i(LOG_TAG, "destroy");
//        }
//
//    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
        DisplayMetrics dm = new DisplayMetrics();
        if (getActivity().getWindowManager() != null)
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);


        mWaveView.initialize(dm);
        mWaveView.speechStarted();


        //   progressBar.setIndeterminate(false);
        //    progressBar.setMax(10);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
        //     mWaveView.speechEnded();
        //    progressBar.setIndeterminate(true);

        //     progressBar.setIndeterminate(false);
        //     progressBar.setVisibility(View.INVISIBLE);
        speech.stopListening();
        //  speech.cancel();

        mWaveView.speechPaused();
    }


    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(LOG_TAG, "onEvent");
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        Log.i(LOG_TAG, "onPartialResults");
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");


    }

    @Override
    public void onResults(Bundle results) {
        speech.destroy();
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        for (String result : matches) {

        }
        listener.sendnote(matches.get(0));
    }


    //  dismiss();

    //  mWaveView.stop();


    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
        //     progressBar.setProgress((int) rmsdB);
    }

    public String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "InfoTech Can't get your voice";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }


        return message;

    }

    public interface NoteDialohListener {
        void sendnote(String sendnote);


    }
}
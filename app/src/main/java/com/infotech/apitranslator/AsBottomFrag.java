package com.infotech.apitranslator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.dnkilic.waveform.WaveView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class AsBottomFrag extends BottomSheetDialogFragment implements RecognitionListener {
    private WaveView mWaveView;
    private TextView returnedText;
    String TotalMessages = "";
    private TextView textView4;
    String taskcompleted = "pp";

    String collectedLeave = "null";
    String SearchComonWord = "";

    @Override
    public void onError(int i) {
        String errorMessage = getErrorText(i);
        Log.d(LOG_TAG, "FAILED " + errorMessage);
        returnedText.setText("Oops,I dont get it");

        mWaveView.speechPaused();
        //    mWaveView.stop();

        //    progressBar.setIndeterminate(false);
        //    progressBar.setVisibility(View.INVISIBLE);

        speech.destroy();


        speakText("Oops,I dont get it");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                speakText("Lets try again");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fn();
                    }
                }, 1500);
            }
        }, 1500);


    }

    private TextView textView5;
    private ExampleDialogListener listener;
    static TextToSpeech t1;
    static int x = 0;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "VoiceRecognitionActivity";
    public static String TAG = "bottom";
    String pardtdone = "prartdone";
    String getActualLeave = "";
    public static final int VOICE_DATA_CHECK = 1;
    String gender,seekbar;

    public AsBottomFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_as_bottom, container, false);
        mWaveView = view.findViewById(R.id.vw2);
        returnedText = (TextView) view.findViewById(R.id.textView1);
        textView5 = view.findViewById(R.id.textView5);
        textView4 = view.findViewById(R.id.textView4);
         gender=getArguments().getString("gender");
         seekbar=getArguments().getString("seekbar");
//        Intent installIntent = new Intent();
//        installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);//Change system voice
//        startActivity(installIntent);
        if(gender.equals("male")) {
            t1 = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        Set<String> a = new HashSet<>();
                        a.add("male");//here you can give male if you want to select male voice.
                        //Voice v=new Voice("en-us-x-sfg#female_2-local",new Locale("en","US"),400,200,true,a);
                        Voice v = new Voice("en-us-x-sfg#male_2-local", new Locale("en", "US"), 400, 200, true, a);
                        t1.setVoice(v);
                        t1.setSpeechRate(Float.parseFloat(seekbar));

                        // int result = T2S.setLanguage(Locale.US);
                        int result = t1.setVoice(v);

                        if (result == TextToSpeech.LANG_MISSING_DATA
                                || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                            Log.e("TTS", "This Language is not supported");
                        }

                    } else {
                        Log.e("TTS", "Initilization Failed!");
                    }
                }
            });
        }
        else
        {
            t1 = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        Set<String> a = new HashSet<>();
                        a.add("female");//here you can give male if you want to select male voice.
                        //Voice v=new Voice("en-us-x-sfg#female_2-local",new Locale("en","US"),400,200,true,a);
                        Voice v = new Voice("en-us-x-sfg#female_2-local", new Locale("en", "US"), 400, 200, true, a);
                        t1.setVoice(v);
                        t1.setSpeechRate(Float.parseFloat(seekbar));

                        // int result = T2S.setLanguage(Locale.US);
                        int result = t1.setVoice(v);

                        if (result == TextToSpeech.LANG_MISSING_DATA
                                || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                            Log.e("TTS", "This Language is not supported");
                        }

                    } else {
                        Log.e("TTS", "Initilization Failed!");
                    }
                }
            });
        }


        fn();
        return view;
    }

    private void fn() {
        if (this.getContext() != null) {
            speech = SpeechRecognizer.createSpeechRecognizer(this.getContext());
            speech.setRecognitionListener(this);
            recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getActivity().getPackageName());
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getActivity().getPackageName());

            recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
            if(gender.equals("male")) {
                t1 = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int status) {
                        if (status == TextToSpeech.SUCCESS) {
                            Set<String> a = new HashSet<>();
                            a.add("male");//here you can give male if you want to select male voice.
                            //Voice v=new Voice("en-us-x-sfg#female_2-local",new Locale("en","US"),400,200,true,a);
                            Voice v = new Voice("en-us-x-sfg#male_2-local", new Locale("en", "US"), 400, 200, true, a);
                            t1.setVoice(v);
                            t1.setSpeechRate(Float.parseFloat(seekbar));

                            // int result = T2S.setLanguage(Locale.US);
                            int result = t1.setVoice(v);

                            if (result == TextToSpeech.LANG_MISSING_DATA
                                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                Log.e("TTS", "This Language is not supported");
                            }

                        } else {
                            Log.e("TTS", "Initilization Failed!");
                        }


                        if (!pardtdone.equals("complete1"))
                            speakText("Hi,How can I help");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                speech.startListening(recognizerIntent);
                            }
                        }, 1500);
                    }
                });
            }
            else
            {t1 = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status == TextToSpeech.SUCCESS) {
                        Set<String> a = new HashSet<>();
                        a.add("female");//here you can give male if you want to select male voice.
                        //Voice v=new Voice("en-us-x-sfg#female_2-local",new Locale("en","US"),400,200,true,a);
                        Voice v = new Voice("en-us-x-sfg#female_2-local", new Locale("en", "US"), 400, 200, true, a);
                        t1.setVoice(v);
                        t1.setSpeechRate(Float.parseFloat(seekbar));

                        // int result = T2S.setLanguage(Locale.US);
                        int result = t1.setVoice(v);

                        if (result == TextToSpeech.LANG_MISSING_DATA
                                || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                            Log.e("TTS", "This Language is not supported");
                        }

                    } else {
                        Log.e("TTS", "Initilization Failed!");
                    }


                    if (!pardtdone.equals("complete1"))
                        speakText("Hi,How can I help");

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            speech.startListening(recognizerIntent);
                        }
                    }, 1500);
                }
            });

            }

        }


    }

    public static AsBottomFrag newInstance() {
        return new AsBottomFrag();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (ExampleDialogListener) context;
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


    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
        DisplayMetrics dm = new DisplayMetrics();
        if (getActivity().getWindowManager() != null)
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        mWaveView.initialize(dm);
        mWaveView.speechStarted();

    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {

        speech.stopListening();
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
        Matcher m = null;
        Matcher m1 = null;

        Log.i(LOG_TAG, "onResults");
        ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        String regex = "(\\d{1,2}(th|rd|st|nd|)\\s\\w{1,10})";
        int j = 0;
        Log.d("date_mentioned", matches.get(0));
        for (String result : matches) {
            if (j == matches.size() - 1)
                break;
            text += result + "\n";
            m = Pattern.compile(regex).matcher(matches.get(j));
            if (result.toLowerCase().contains("morning shift"))
                listener.shifting("morning");
            if (result.toLowerCase().contains("evening shift"))
                listener.shifting("evening");
            if ((result.toLowerCase().startsWith("because") || result.toLowerCase().startsWith("since") || result.toLowerCase().startsWith("as")) && collectedLeave.equals("annual")) {

                listener.reason(result.toLowerCase());

                speakText("Ok ,Which Sift you want leave? ");
                fn();
            }


            boolean b = isAnyMonth(result);

            if (x == 10 && (!result.contains("00")) && b && m.find()) {
                x = 0;
                taskcompleted = "task";
                Log.d("hel", "called1");


                if (Objects.requireNonNull(m.group(1)).contains("th")) {
                    listener.startdate(Objects.requireNonNull(m.group(1)).replace("th", ""));

                    AfterDateGetValue(result, (m.group(1)));
                    break;
                }
                if (Objects.requireNonNull(m.group(1)).contains("rd")) {
                    Log.d("hel", "called2");
                    listener.startdate(Objects.requireNonNull(m.group(1)).replace("rd", ""));
                    AfterDateGetValue(result, (m.group(1)));
                    break;
                }
                if (Objects.requireNonNull(m.group(1)).contains("st")) {
                    String[] split = Objects.requireNonNull(m.group(1)).split(" ");
                    listener.startdate(Objects.requireNonNull(split[0].replace("st", " ") + " " + split[1]));//this function is not able to replace "st" to ""
                    Log.d("hel", "called2" + m.group(1).replace("st", ""));
                    AfterDateGetValue(result, (m.group(1)));
                    break;
                }
                if (Objects.requireNonNull(m.group(1)).contains("nd")) {
                    listener.startdate(Objects.requireNonNull(m.group(1)).replace("nd", ""));
                    AfterDateGetValue(result, (m.group(1)));
                    break;

                } else {
                    listener.startdate(m.group(1));
                    AfterDateGetValue(result, (m.group(1)));
                    break;

                }


            }

            ++j;
        }
        returnedText.setText(matches.get(0));
        String s1 = "Apply for leave";
        if (s1.toLowerCase().equals((matches.get(0)))) {
            speakText("Which type of leave is that");
            textView4.setText("Which type of leave is that?");
            textView5.setVisibility(View.VISIBLE);

            fn();
            pardtdone = "complete1";
        } else {
            int k = 0;
            String result = matches.get(0);

            boolean b = result.contains("because") || result.contains("as") || result.contains("cause") || result.contains("since") || result.contains("that");
            String[] ListOfLeaves = {"first half annual leave", "second half annual leave", "compassionate leave", "hospitalization leave", "medical leave", "medical hourly leave"};
            for (int pp = 0; pp < ListOfLeaves.length; pp++) {
                if (result.toLowerCase().contains(ListOfLeaves[pp])) {
                    taskcompleted = "";


                    Log.d("timetaken", "first part");
                    m1 = Pattern.compile(regex).matcher(matches.get(k));
                    listener.sendtitle(ListOfLeaves[pp]);
                    if (m1.find()) {


                        Log.d("timetaken", "Second part");
                        x = 0;

                        removeLastCharacter(result, Objects.requireNonNull(m1.group(1)), b);
                        break;
                    } else {

                        speakText("Ok got it,On which date you want leave");
                        x = 10;
                        pardtdone = "complete1";
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                fn();
                            }
                        }, 1500);

                    }
                    break;
                } else {
                    unmatchedFunctionCalled(pp, ListOfLeaves, result);

                }


            }
            ++k;

        }
    }

    private void removeLastCharacter(String result, String group, boolean b) {
        if (group.contains("th")) {
            listener.startdate(Objects.requireNonNull(group).replace("th", ""));

            ContainsTh(group, result, b);
        }
        if (group.contains("rd")) {
            Log.d("timetaken", "3rd part");
            listener.startdate(Objects.requireNonNull(group).replace("rd", ""));
            ContainsTh(group, result, b);


        }
        if (group.contains("st")) {
            Log.d("timetaken", "3rd part");
            listener.startdate(Objects.requireNonNull(group).replace("st", ""));
            ContainsTh(group, result, b);

        }
        if (group.contains("nd")) {
            Log.d("timetaken", "3rd part");
            listener.startdate(Objects.requireNonNull(group).replace("nd", ""));
            ContainsTh(group, result, b);


        } else {
            Log.d("timetaken", "3rd part");
            listener.startdate(Objects.requireNonNull(group));
            ContainsTh(group, result, b);

        }
    }

    private void speakText(String text) {
        if (t1.isSpeaking()) {
            return;
        } else {
            t1.speak(text, TextToSpeech.QUEUE_FLUSH, null, "eiy");
        }
    }

    private void unmatchedFunctionCalled(int pp, String[] ListOfLeaves, String result) {
        if ((pp == ListOfLeaves.length - 1) && (!result.toLowerCase().contains(ListOfLeaves[ListOfLeaves.length - 1]))) {
            if (taskcompleted.equals("pp")) {
                Log.d("hel", pp + "----" + ListOfLeaves.length);
                String[] splitword = result.split(" ");
                for (int zz = 0; zz < splitword.length; zz++) {
                    Log.d("checking", "first" + zz);
                    if (splitword[zz].toLowerCase().equals("leave")) {
                        if (zz > 0) {
                            SearchComonWord = splitword[zz - 1];
                            for (int mm = 0; mm < ListOfLeaves.length; mm++) {
                                Log.d("checking", "second" + SearchComonWord);
                                String[] getCommonWord = ListOfLeaves[mm].split(" ");
                                for (int xx = 0; xx < getCommonWord.length; xx++) {
                                    if (getCommonWord[xx].toLowerCase().equals(SearchComonWord)) {
                                        Log.d("checking", "3rd");
                                        Log.d("ListOfLeaves", ListOfLeaves[mm]);
                                        TotalMessages = TotalMessages + "\n" + ListOfLeaves[mm];


                                    }
                                }

                            }

                            break;
                        }
                    }
                }

                returnedText.setText(TotalMessages);
                pardtdone = "complete1";


                speakText("ok,Which type of leave is that?");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fn();
                    }
                }, 1500);


            }
        }
    }

    private boolean isAnyMonth(String result) {
        return (result.toLowerCase().contains("jan")
                || result.toLowerCase().contains("feb")
                || result.toLowerCase().contains("mar")
                || result.toLowerCase().contains("apr")
                || result.toLowerCase().contains("april")
                || result.toLowerCase().contains("may")
                || result.toLowerCase().contains("june")
                || result.toLowerCase().contains("july")
                || result.toLowerCase().contains("aug")
                || result.toLowerCase().contains("august")
                || result.toLowerCase().contains("sept")
                || result.toLowerCase().contains("september")
                || result.toLowerCase().contains("oct")
                || result.toLowerCase().contains("october")
                || result.toLowerCase().contains("nov")
                || result.toLowerCase().contains("november")
                || result.toLowerCase().contains("dec")
                || result.toLowerCase().contains("december"));
    }

    private String removelastchar(String group) {
        String returnValue = null;
        Pattern p = Pattern.compile("([a-zA-Z])");
        String[] split1 = group.split(" ");
        if (Objects.requireNonNull(group).contains("th")) {
            returnValue = Objects.requireNonNull(group).replace("th", "");

        }
        if (Objects.requireNonNull(group).contains("rd")) {
            returnValue = Objects.requireNonNull(group).replace("rd", "");
        }
        if (Objects.requireNonNull(group).contains("st")) {
            String[] split = Objects.requireNonNull(group.split(" "));

            returnValue = Objects.requireNonNull(Objects.requireNonNull(split1[0].replace("st", " ") + " " + split1[1]));
            Log.d("hel", returnValue + "oooo");

        }
        if (Objects.requireNonNull(group).contains("nd")) {
            returnValue = Objects.requireNonNull(group).replace("nd", "");

        }
        Matcher m = p.matcher(group);
        if (!m.find())
            returnValue = m.group(1);
        else
            Log.d("hel","error occured");


        return returnValue;

    }

    private void AfterDateGetValue(String result, String group) {
        Log.d("hel", "1111111111111");
        if (getNextWord(result, group) != null) {
            if (Objects.requireNonNull(getNextWord(result, group)).toLowerCase().equals("to")) {
                if (getNextWord(result, getNextWord(result, getNextWord(result, getNextWord(result, "to")))) != null) {

                    String firstpart = getNextWord(result, getNextWord(result, getNextWord(result, getNextWord(result, "to"))));
                    assert firstpart != null;
                    Log.d("hel", firstpart);
                    listener.endate(removelastchar(firstpart)+ " " + getNextWord(result, getNextWord(result, "to")));
                    Log.d("hel","===="+removelastchar(firstpart)+ " " + getNextWord(result, getNextWord(result, "to")));
                    speakText("Ok ,Why you want leave ?");
                    collectedLeave = "annual";
                    pardtdone = "complete1";
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fn();
                        }
                    }, 1500);

                } else {
                    listener.endate(group.replace("th", ""));

                    speakText("Ok ,Why you want leave ?");
                    collectedLeave = "annual";
                    pardtdone = "complete1";
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fn();
                        }
                    }, 1500);
                }
            }
            else    if (Objects.requireNonNull(getNextWord(result, group)).toLowerCase().equals("2")) {
                if (getNextWord(result, getNextWord(result, getNextWord(result, getNextWord(result, "2")))) != null) {

                    String firstpart =  getNextWord(result, getNextWord(result, getNextWord(result, getNextWord(result, "2"))));
                    assert firstpart != null;
                    Log.d("hel", firstpart);
                    listener.endate(removelastchar(firstpart)+ " " + getNextWord(result, getNextWord(result, "2")));
                    Log.d("hel","===="+removelastchar(firstpart)+ " " + getNextWord(result, getNextWord(result, "2")));
                    speakText("Ok ,Why you want leave ?");
                    collectedLeave = "annual";
                    pardtdone = "complete1";
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fn();
                        }
                    }, 1500);

                } else {
                    listener.endate(group.replace("th", ""));

                    speakText("Ok ,Why you want leave ?");
                    collectedLeave = "annual";
                    pardtdone = "complete1";
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fn();
                        }
                    }, 1500);
                }
            }


        } else {


            listener.endate(removelastchar(group));
            Log.d("hel", "=========="+removelastchar(group));


            speakText("Ok ,Why you want leave ?");
            collectedLeave = "annual";
            pardtdone = "complete1";
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fn();
                }
            }, 1500);//this fn() function is not immediately active
        }
    }

    private void ContainsTh(String group, String result, boolean b) {
        if (getNextWord(result, group) != null) {

            if (Objects.requireNonNull(getNextWord(result, group)).toLowerCase().equals("to")) {

                if (getNextWord(result, getNextWord(result, getNextWord(result, getNextWord(result, "to")))) != null) {
                    listener.endate(getNextWord(result, getNextWord(result, getNextWord(result, getNextWord(result, "to")))) + getNextWord(result, getNextWord(result, "to")));
                    Pattern word = null;
                    Matcher match = null;
                    String getMax = "False";
                    String[] ReasonwordToFind = {"because", "as", "cause", "since", "that"};
                    for (int ss = 0; ss < ReasonwordToFind.length; ss++) {
                        if ((ss == ReasonwordToFind.length - 1) && (getMax.equals("False"))) {

                            speakText("Ok ,Why you want leave?");
                            collectedLeave = "annual";
                            pardtdone = "complete1";
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    fn();
                                }
                            }, 1500);
                        }
                        word = Pattern.compile(ReasonwordToFind[ss]);

                        match = word.matcher(result.toLowerCase());
                        while (match.find()) {
                            listener.reason(result.substring(match.start(), result.length()));
                            if (result.toLowerCase().contains("morning shift")) {
                                listener.shifting("morning");

                                speakText("Ok ,got it");
                            } else if (result.toLowerCase().contains("evening shift")) {
                                listener.shifting("evening");
                                speakText("Ok ,got it");

                            } else {

                                speakText("Ok ,Which Sift you want leave? ");
                                pardtdone = "complete1";
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        fn();
                                    }
                                }, 1500);

                            }
                            getMax = "True";

                        }
                    }


                }


            } else if (Objects.requireNonNull(getNextWord(result, group)).toLowerCase().equals("2")) {

                if (group.contains("th")) {
                    listener.startdate(Objects.requireNonNull(group).replace("th", ""));

                } else if (group.contains("rd")) {
                    listener.startdate(Objects.requireNonNull(group).replace("rd", ""));


                } else if (group.contains("nd")) {
                    listener.startdate(Objects.requireNonNull(group).replace("nd", ""));


                } else if (group.contains("st")) {
                    listener.startdate(Objects.requireNonNull(group).replace("st", ""));

                } else {
                    listener.startdate(Objects.requireNonNull(group));

                }
                listener.endate(getNextWord(result, getNextWord(result, getNextWord(result, getNextWord(result, "to")))) + " " + getNextWord(result, getNextWord(result, "to")));


            } else {
                String output = "False";
                Pattern word = null;
                Matcher match = null;
                String[] ReasonwordToFind = {"because", "as", "cause", "since", "that"};
                for (int ss = 0; ss < ReasonwordToFind.length; ss++) {
                    if ((ss == ReasonwordToFind.length - 1) && (output.equals("False"))) {

                        speakText("Ok ,Why you want leave");
                        collectedLeave = "annual";
                        pardtdone = "complete1";
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                fn();
                            }
                        }, 1500);
                    }

                    word = Pattern.compile(ReasonwordToFind[ss]);
                    match = word.matcher(result.toLowerCase());
                    while (match.find()) {
                        listener.reason(result.substring(match.start(), result.length()));
                        listener.endate(Objects.requireNonNull(group).replace("th", ""));
                        output = "tuue";
                        if (result.toLowerCase().contains("morning shift")) {
                            listener.shifting("morning");

                            speakText("Ok ,got it");
                        } else if (result.toLowerCase().contains("evening shift")) {
                            listener.shifting("evening");
                            speakText("Ok ,got it");

                        } else {

                            speakText("Ok ,Which Sift you want leave?");
                            pardtdone = "complete1";
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    fn();
                                }
                            }, 1500);
                        }
                    }
                }
            }

        } else {
            if (group.contains("th")) {
                listener.startdate(Objects.requireNonNull(group).replace("th", ""));
                listener.endate(Objects.requireNonNull(group).replace("th", ""));
            } else if (group.contains("rd")) {
                listener.startdate(Objects.requireNonNull(group).replace("rd", ""));
                listener.endate(Objects.requireNonNull(group).replace("rd", ""));

            } else if (group.contains("nd")) {
                listener.startdate(Objects.requireNonNull(group).replace("nd", ""));
                listener.endate(Objects.requireNonNull(group).replace("nd", ""));

            } else if (group.contains("st")) {
                listener.startdate(Objects.requireNonNull(group).replace("st", ""));
                listener.endate(Objects.requireNonNull(group).replace("st", ""));
            } else {
                listener.startdate(Objects.requireNonNull(group));
                listener.endate(Objects.requireNonNull(group));
            }
            if (b) {

                Log.d("timetaken", "4th part");
                Pattern word = null;
                Matcher match = null;
                String[] ReasonwordToFind = {"because", "as", "cause", "since", "that"};
                for (int ss = 0; ss < ReasonwordToFind.length; ss++) {
                    if (ss == ReasonwordToFind.length - 1) {
                        Log.d("timetaken", "5th part");

                        speakText("Ok ,got it");

                    }
                    word = Pattern.compile(ReasonwordToFind[ss]);
                    match = word.matcher(result.toLowerCase());
                    while (match.find()) {
                        listener.reason(result.substring(match.start(), result.length()));
                        if (result.toLowerCase().contains("morning shift")) {
                            listener.shifting("morning");

                            speakText("Ok ,got it");
                        } else if (result.toLowerCase().contains("evening shift")) {
                            listener.shifting("evening");
                            speakText("Ok ,got it");

                        } else {

                            speakText("Ok ,Which Sift you want leave?");
                            pardtdone = "complete1";
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    fn();
                                }
                            }, 1500);
                        }

                    }
                }
            } else {


                speakText("Ok ,Why you want leave?");
                collectedLeave = "annual";
                pardtdone = "complete1";
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fn();
                    }
                }, 1500);
            }

        }
    }

    public static String getNextWord(String str, String word) {
        try {
            List<String> text = Arrays.asList(str.split(" "));
            List<String> list = Arrays.asList(word.split(" "));
            int index_of = text.indexOf(list.get(list.size() - 1));
            return (index_of == -1) ? null : text.get(index_of + 1);
        } catch (Exception e) {
            return null;
        }
    }

    static int binarySearch(String[] arr, String x) {
        int l = 0, r = arr.length - 1;
        while (l <= r) {
            int m = l + (r - l) / 2;

            int res = x.compareTo(arr[m]);

            // Check if x is present at mid
            if (res == 0)
                return m;

            // If x greater, ignore left half
            if (res > 0)
                l = m + 1;

                // If x is smaller, ignore right half
            else
                r = m - 1;
        }

        return -1;
    }


    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
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

    public interface ExampleDialogListener {
        void startdate(String startdate);

        void sendtitle(String title);

        void endate(String endate);

        void reason(String message);

        void shifting(String shifting);
    }
}
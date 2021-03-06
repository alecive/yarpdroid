package com.alecive.yarpdroid;

/**
 * Created by alecive on 19/06/15.
 * This is the fragment for the TTS capabilities.
 */

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A placeholder fragment containing a simple view.
 */
public class STTFragment extends Fragment {

    private TextView txtSpeechInput;
    private Button btnSpeak;
    private Button btnInitNative;
    private Button btnFiniNative;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    private static final String TAG = "STTFragment";

    private long STTPortHandle;

    private static String applicationName = "/yarpdroid";

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static STTFragment newInstance(int sectionNumber, String _applicationName) {
        STTFragment fragment = new STTFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        fragment.setApplicationName(_applicationName);
        return fragment;
    }

    public void setApplicationName(String _applicationName) {
        applicationName = _applicationName;
    }

    public STTFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        finiNative();
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.stt_fragment, container, false);

        txtSpeechInput = (TextView) rootView.findViewById(R.id.txtSpeechInput);

        btnSpeak = (Button) rootView.findViewById(R.id.btnSpeak);
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        btnInitNative = (Button) rootView.findViewById(R.id.btnInitNative);
        btnInitNative.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                initNative();
            }
        });
        btnFiniNative = (Button) rootView.findViewById(R.id.btnFiniNative);
        btnFiniNative.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finiNative();
            }
        });

        STTPortHandle=0;

        register();
        return rootView;
    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        initNative();
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
//                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            String s=getString(R.string.speech_not_supported);
            Snackbar.make(getView(), "WARN: "+s, Snackbar.LENGTH_LONG).show();
            Log.w(TAG, s);
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == Activity.RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    writeOntoBufferedPort(result.get(0));
                } else {
                    Log.w("MainActivity", "No valid speech output has ben received");
                }
                break;
            }

        }
    }

    private void initNative() {
        if (STTPortHandle!=0) {
            String s="Native port has been already opened!";
            Snackbar.make(getView(), "WARN: "+s, Snackbar.LENGTH_LONG).show();
            Log.w(TAG,s);
            return;
        }

        Log.d(TAG, "I'm opening the native port");
        if(!createBufferedPort(applicationName)) {
            createBufferedPort(applicationName);
        }

        if (STTPortHandle!=0) {
            String s="Native port has been successfully opened!";
            Snackbar.make(getView(), s, Snackbar.LENGTH_LONG).show();
            Log.i(TAG, s);
        }
    }

    private void finiNative() {
        Log.d(TAG,"I'm closing the native port");
        if (STTPortHandle!=0) {
            if (destroyBufferedPort()) {
                STTPortHandle=0;
            }
        }
        else {
            String s="The native port is not open or has been already closed";
            Snackbar.make(getView(), "WARN: "+s, Snackbar.LENGTH_LONG).show();
            Log.w(TAG, s);
        }
    }

    private static void staticTestMethod(String str)
    {
        Log.i(TAG, str);
    }

    private void nonStaticTestMethod(String str)
    {
        Log.i(TAG, str);
    }

    private        native boolean register();
    private static native void    testCallbackStatic();
    private        native boolean createBufferedPort(String _applicationName);
    private        native void    writeOntoBufferedPort(String textToSend);
    private        native void    getDataReceivedonPort(String textToSend);
    private        native boolean destroyBufferedPort();
}

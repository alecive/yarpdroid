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

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static STTFragment newInstance(int sectionNumber) {
        STTFragment fragment = new STTFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
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

        register();
        return rootView;
    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity().getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
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
        Log.i(TAG,"I'm opening the native port");
        createBufferedPort();
    }

    private void finiNative() {
        Log.i(TAG,"I'm closing the native port");
        destroyBufferedPort();
    }

    private static void staticTestMethod(String str)
    {
        Log.i(TAG, str);
    }

    private void nonStaticTestMethod(String str)
    {
//        txtSpeechInput.setText(str);
        Log.i(TAG, str);
    }

    private        native boolean register();
    private        native void    getDataReceivedonPort(String textToSend);
    private static native void    testCallbackStatic();
    private        native void    createBufferedPort();
    private        native void    writeOntoBufferedPort(String textToSend);
    private        native void    destroyBufferedPort();
}

package com.alecive.yarpdroid;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by alecive on 29/06/15. This is the third tab
 */
public class demoCTPFragment extends Fragment {

    private static final String TAG = "demoCTPFragment";
    private static final String ARG_SECTION_NUMBER = "section_number";

    private static final String COMMUNICATION = "RPC";
    // private static final String COMMUNICATION = "BUFFERED_PORT";

    private Button btnAction1;
    private Button btnAction2;
    private Button btnAction3;
    private Button btnInitNative;
    private Button btnFiniNative;

    private long demoCTPPortHandle;
    private long demoCTPRPCHandle;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static demoCTPFragment newInstance(int sectionNumber) {
        demoCTPFragment fragment = new demoCTPFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.joints_fragment, container, false);

        btnAction1 = (Button) rootView.findViewById(R.id.btnAction1);
        btnAction1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (COMMUNICATION=="RPC") {
                    sendRPCAction("start");
                }
                else if (COMMUNICATION=="BUFFERED_PORT") {
                    sendAction(1);
                }
            }
        });

        btnAction2 = (Button) rootView.findViewById(R.id.btnAction2);
        btnAction2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (COMMUNICATION=="RPC") {
                    sendRPCAction("stop");
                }
                else if (COMMUNICATION=="BUFFERED_PORT") {
                    sendAction(2);
                }
            }
        });

        btnAction3 = (Button) rootView.findViewById(R.id.btnAction3);
        btnAction3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (COMMUNICATION=="RPC") {
                    sendRPCAction("home");
                }
                else if (COMMUNICATION=="BUFFERED_PORT") {
                    sendAction(3);
                }
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

        demoCTPPortHandle=0;
        demoCTPRPCHandle=0;

        register();
        return rootView;
    }

    private void initNative() {
        if (COMMUNICATION=="RPC") {
            if (demoCTPRPCHandle!=0) {
                String s="RPC port has been already opened!";
                Snackbar.make(getView(), "WARN: "+s, Snackbar.LENGTH_LONG).show();
                Log.w(TAG,s);
                return;
            }

            Log.d(TAG, "I'm opening the native port");
            if(!createRPCPort()) {
                createRPCPort();
            }

            if (demoCTPRPCHandle!=0) {
                String s="RPC port has been successfully opened!";
                Snackbar.make(getView(), s, Snackbar.LENGTH_LONG).show();
                Log.i(TAG, s);
            }
        }
        else if (COMMUNICATION=="BUFFERED_PORT") {
            if (demoCTPPortHandle!=0) {
                String s="Native port has been already opened!";
                Snackbar.make(getView(), "WARN: "+s, Snackbar.LENGTH_LONG).show();
                Log.w(TAG,s);
                return;
            }

            Log.d(TAG, "I'm opening the native port");
            if(!createBufferedPort()) {
                createBufferedPort();
            }

            if (demoCTPPortHandle!=0) {
                String s="Native port has been successfully opened!";
                Snackbar.make(getView(), s, Snackbar.LENGTH_LONG).show();
                Log.i(TAG, s);
            }
        }
    }

    private void finiNative() {
        if (COMMUNICATION=="RPC") {
            Log.d(TAG, "I'm closing the rpc port");
            if (demoCTPRPCHandle!=0) {
                if (destroyRPCPort()) {
                    demoCTPRPCHandle=0;
                }
            }
            else {
                String s="The rpc port is not open or has been already closed";
                Snackbar.make(getView(), "WARN: "+s, Snackbar.LENGTH_LONG).show();
                Log.w(TAG, s);
            }
        }
        else if (COMMUNICATION=="BUFFERED_PORT") {
            Log.d(TAG, "I'm closing the native port");
            if (demoCTPPortHandle!=0) {
                if (destroyBufferedPort()) {
                    demoCTPPortHandle=0;
                }
            }
            else {
                String s="The native port is not open or has been already closed";
                Snackbar.make(getView(), "WARN: "+s, Snackbar.LENGTH_LONG).show();
                Log.w(TAG, s);
            }
        }
    }

    private native boolean register();

    private native boolean createBufferedPort();
    private native boolean sendAction(int action);
    private native boolean destroyBufferedPort();

    private native boolean createRPCPort();
    private native boolean sendRPCAction(String message);
    private native boolean destroyRPCPort();
}

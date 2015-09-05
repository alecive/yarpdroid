package com.alecive.yarpdroid;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;
import java.util.Timer;
import java.util.TimerTask;

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

    private AllSensorManager allSensorManager;
    private Timer timer;
    private TimerTask timerTask;
    private Button btnStartSensor;
    private Button btnStopSensor;
    private ToggleButton toggleButtonMovementType;

    private long mobileSensorPortHandle;
    private static String applicationName = "/yarpdroid";
    private boolean requiresOrientationCalibrationOffset;
    private float[] orientationOffsets;
    private int sensorCount;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static demoCTPFragment newInstance(int sectionNumber, String _applicationName) {
        demoCTPFragment fragment = new demoCTPFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        fragment.setApplicationName(_applicationName);
        return fragment;
    }

    public void setApplicationName(String _applicationName) {
        applicationName = _applicationName;
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
        mobileSensorPortHandle = 0;

        requiresOrientationCalibrationOffset = true;
        orientationOffsets = new float[3];

        register();
        allSensorManager = new AllSensorManager(getActivity(), 10);

        btnStartSensor = (Button) rootView.findViewById(R.id.btnStartSensor);
        btnStartSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sensorCount = 0;

                timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        sensorCount++;
                        float[] data = allSensorManager.getOrientationDataAsFloats();

//                        data[6] = (float)Math.toRadians(data[6]);
//                        data[7] = (float)Math.toRadians(data[7]);
//                        data[8] = (float)Math.toRadians(data[8]);

                        // subtracts away the orientation offset to align with the robot to (0,0,0)
                        if (requiresOrientationCalibrationOffset) {
                            requiresOrientationCalibrationOffset = false;
                            orientationOffsets[0] = data[6];
                            orientationOffsets[1] = data[7];
                            orientationOffsets[2] = data[8];
                            Log.d("demoCTPFragmentmobile", "Offset: "+data[6] + " " + data[7] + " " + data[8]);

                        }
                        data[6] = data[6] - orientationOffsets[0];
                        data[7] = data[7] - orientationOffsets[1];
                        data[8] = data[8] - orientationOffsets[2];

                        String movementType;
                        if (toggleButtonMovementType.isChecked()) {
                            movementType = "position";
                        } else {
                            movementType = "orientation";
                        }
                        Log.d("demoCTPFragmentmobile", data[6] + " " + data[7] + " " + data[8]);
//                        writeOntoBufferedMobilePort(data[0], data[1], data[2], data[3], data[4], data[5], data[7], data[6], data[8], movementType);
                }
                };

                timer = new Timer();
                timer.scheduleAtFixedRate(timerTask, 1, 100);
                Toast.makeText(getActivity(), "Phone sensor started", Toast.LENGTH_SHORT).show();
            }
        });

        btnStopSensor = (Button) rootView.findViewById(R.id.btnStopSensor);
        btnStopSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timer.cancel();
                timerTask.cancel();
                requiresOrientationCalibrationOffset = true;
                Toast.makeText(getActivity(), "Phone sensor stopped", Toast.LENGTH_SHORT).show();
            }
        });

        toggleButtonMovementType = (ToggleButton) rootView.findViewById(R.id.toggleButtonMovementType);
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
            if(!createRPCPort(applicationName)) {
                createRPCPort(applicationName);
            }

            if (!createBufferedMobileSensorDataPort(applicationName)) {
                createBufferedMobileSensorDataPort(applicationName);
            }

            if (demoCTPRPCHandle!=0 && mobileSensorPortHandle != 0) {
                String s="RPC port has been successfully opened!";
                Snackbar.make(getView(), s, Snackbar.LENGTH_LONG).show();
                Log.i(TAG, s);
            }
            allSensorManager.registerOrientationSensors();
        }
        else if (COMMUNICATION=="BUFFERED_PORT") {
            if (demoCTPPortHandle!=0) {
                String s="Native port has been already opened!";
                Snackbar.make(getView(), "WARN: "+s, Snackbar.LENGTH_LONG).show();
                Log.w(TAG,s);
                return;
            }

            Log.d(TAG, "I'm opening the native port");
            if(!createBufferedPort(applicationName)) {
                createBufferedPort(applicationName);
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
            if (demoCTPRPCHandle!=0 | mobileSensorPortHandle != 0) {
                if (destroyRPCPort()) {
                    demoCTPRPCHandle=0;
                }

                if (destroyBufferedMobileSensorDataPort()) {
                    mobileSensorPortHandle =0;
                }
            }
            else {
                String s="The rpc port is not open or has been already closed";
                Snackbar.make(getView(), "WARN: "+s, Snackbar.LENGTH_LONG).show();
                Log.w(TAG, s);
            }
            allSensorManager.unregisterOrientationSensors();
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

    private native boolean createBufferedPort(String _applicationName);
    private native boolean sendAction(int action);
    private native boolean destroyBufferedPort();

    private native boolean createRPCPort(String _applicationName);
    private native boolean sendRPCAction(String message);
    private native boolean destroyRPCPort();
    private native boolean createBufferedMobileSensorDataPort(String _applicationName);
    private native boolean destroyBufferedMobileSensorDataPort();
    private native void    writeOntoBufferedMobilePort(double accelerometer1, double accelerometer2, double accelerometer3,
                                                       double gyroscope1, double gyroscope2, double gyroscope3,
                                                       double orientation1, double orientation2, double orientation3,
                                                       String movementType);
}

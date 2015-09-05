package com.alecive.yarpdroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.Semaphore;

import example.yarpcommon.ApplicationID;
import example.yarpcommon.ImageUtil;
import example.yarpcommon.bluetooth.BluetoothChannel;
import example.yarpcommon.bluetooth.BluetoothCommunication;
import example.yarpcommon.bluetooth.BluetoothException;
import example.yarpcommon.bluetooth.BluetoothListener;
import example.yarpcommon.bluetooth.BluetoothListenerExceptionHandler;
import example.yarpcommon.bluetooth.BluetoothWorker;

/**
 * Created by alecive on 22/06/15. Blah
 */
public class yarpviewFragment extends Fragment {

    private static final String TAG = "yarpviewFragment";
    private ImageView imgLeft;

    private Button btnInitNative;
    private Button btnFiniNative;

    private long viewLeftHandle;
    private long monoLeftHandle;

    private int screenHeight;
    private int screenWidth;

    private static String applicationName = "/yarpdroid";

    private BluetoothCommunication bluetoothCommunication;
    private BluetoothChannel channel1;
    private BluetoothChannel channel2;
    private BluetoothListener bluetoothListener;
    private BluetoothWorker bluetoothWorker;
    private BluetoothListenerExceptionHandler exceptionHandler;
    private AsyncTask<Void, Integer, Void> bluetoothTask;

    private Semaphore available;

    private long glassSensorPortHandle;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static yarpviewFragment newInstance(int sectionNumber, String _applicationName) {
        yarpviewFragment fragment = new yarpviewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        fragment.setApplicationName(_applicationName);
        return fragment;
    }

    public void setApplicationName(String _applicationName) {
        applicationName = _applicationName;
    }

    public yarpviewFragment() {  }

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
        View rootView = inflater.inflate(R.layout.yarpview_fragment, container, false);

        imgLeft  = (ImageView) rootView.findViewById(R.id.imgLeft);
        imgLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.e(TAG, "Sending touch coordinates to iKinGazeCtrl : " +
                            String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()) + " " +
                            (((int)event.getX()*320)/screenWidth) + "x" +
                            (((int)event.getY()*240)/540));
                    sendTouchEventsonMonoIPort("left",(((int)event.getX()*320)/screenWidth),
                                                      (((int)event.getY()*240)/540), 1.0);
                }
                return true;
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

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth  = displaymetrics.widthPixels;

        viewLeftHandle=0;
        monoLeftHandle=0;
        glassSensorPortHandle = 0;

        register();

        available = new Semaphore(1);

        prepareBlueTooth();
        startBluetooth();
        return rootView;
    }

    private void initNative() {
        if (viewLeftHandle!=0 | monoLeftHandle!=0) {
            String s="Native ports have been already opened!";
            Snackbar.make(getView(), "WARN: "+s, Snackbar.LENGTH_LONG).show();
            Log.w(TAG,s);
            return;
        }

        Log.d(TAG,"I'm opening the native ports");
        if (!createBufferedImgPortL(applicationName)) {
            createBufferedImgPortL(applicationName);
        }

        if (!createBufferedMonoIPort(applicationName)) {
            createBufferedMonoIPort(applicationName);
        }

        if (!createBufferedGlassSensorDataPort(applicationName)) {
            createBufferedGlassSensorDataPort(applicationName);
        }

        if (viewLeftHandle!=0 && monoLeftHandle!=0 && glassSensorPortHandle !=0)
        {
            String s="Native ports have been successfully opened!";
            Snackbar.make(getView(), s, Snackbar.LENGTH_LONG).show();
            Log.i(TAG,s);
        }
    }

    private void finiNative() {
        Log.d(TAG,"I'm fining the native ports");
        boolean isViewLeftOn=(viewLeftHandle!=0);
        boolean isMonoLeftOn=(monoLeftHandle!=0);

        if (!isViewLeftOn | !isMonoLeftOn) {
            String s="One of the native ports (or more than one) is not open or has been already closed";
            Snackbar.make(getView(), "WARN: " + s, Snackbar.LENGTH_LONG).show();
            Log.w(TAG,s);
        }

        if (isViewLeftOn) {
            if (destroyBufferedImgPortL()) {
                viewLeftHandle=0;
            }
        }

        if (isMonoLeftOn) {
            if (destroyBufferedMonoIPort()) {
                monoLeftHandle=0;
            }
        }

        if (destroyBufferedGlassSensorDataPort()) {
            glassSensorPortHandle =0;
        }

        stopBluetoothListener();
        closeBluetooth();
    }

    public static String getHexString(byte[] b) throws Exception {
        final StringBuilder result = new StringBuilder( 2 * b.length );
        for (int i=0; i < b.length; i++) {
            result.append(Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 ));
        }
        return result.toString();
    }

    public void setImageViewWithByteArray(final ImageView view, final byte[] data) {
        long threadId = Thread.currentThread().getId();
        Log.i(TAG,"Thread #" + threadId + " is doing this task");
        if (data==null) {
            Log.e(TAG,"input image is NULL!");
        }
        Log.i(TAG, "I am in setImageViewWithByteArray. Size of the byte array to display: "+data.length);

        int[] intArray = new int[data.length/3];

        for (int i=0; i<data.length/3; i++) {
            intArray[i] = 0xff000000                      | (((int)data[i*3] & 255) << 16) |
                          (((int)data[i*3+1] & 255) << 8) | ((int)data[i*3+2] & 255);
        }

        final Bitmap bitmap = Bitmap.createBitmap(320,240, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(intArray,0,320,0,0,320,240);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (bitmap==null) {
                    Log.e(TAG,"Bitmap is NULL!");
                }
                else {
                    view.setImageBitmap(Bitmap.createScaledBitmap(bitmap,screenWidth,
                                        (int)((screenWidth*240)/320),false));
                }
            }
        });

        if (available.tryAcquire()) {
            Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 32, 48, 256, 144);
            byte[] resizedData = ImageUtil.convertImage(resizedBitmap, 70);
            try {
                channel2.send(resizedData);
            } catch (IOException e) {
                e.printStackTrace();
            }

            available.release();
        }
        Log.i(TAG, "setImageViewWithByteArray finished!" + screenWidth + " " + screenHeight);
    }

    public void setImgLeft(byte[] data) {
        setImageViewWithByteArray(imgLeft, data);
    }

    public void setImageViewWithString(final ImageView view, String str) {
        long threadId = Thread.currentThread().getId();
        Log.i(TAG,"Thread #" + threadId + " is doing this task");

        byte[] data = str.getBytes();

        if (data==null) {
            Log.e(TAG,"input image is NULL!");
        }
        Log.i(TAG, "I am in setImageViewWithString. Size of the byte array to display: "+data.length);

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        opt.outWidth           = 320;
        opt.outHeight          = 240;
        opt.inDensity          = 3;
        final Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,opt);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (bitmap==null) {
                    Log.e(TAG,"Bitmap is NULL!");
                    view.setImageResource(R.drawable.abc_btn_radio_material);
                }
                else {
                    view.setImageBitmap(bitmap);
                }
            }
        });
    }

    public void setImgLeft(String data) {
        setImageViewWithString(imgLeft, data);
    }

    private native boolean register();

    private native boolean createBufferedImgPortL(String _applicationName);
    private native boolean destroyBufferedImgPortL();

    private native boolean createBufferedMonoIPort(String _applicationName);
    private native boolean destroyBufferedMonoIPort();
    private native boolean sendTouchEventsonMonoIPort(String cam, int u, int v, double z);
    private native boolean createBufferedGlassSensorDataPort(String _applicationName);
    private native boolean destroyBufferedGlassSensorDataPort();
    private native void    writeOntoBufferedGlassPort(double accelerometer1, double accelerometer2, double accelerometer3, double gyroscope1, double gyroscope2, double gyroscope3);

    private native boolean createBufferedMobileSensorDataPort(String _applicationName);
    private native boolean destroyBufferedMobileSensorDataPort();
    private native void    writeOntoBufferedMobilePort(double accelerometer1, double accelerometer2, double accelerometer3,
                                                       double gyroscope1, double gyroscope2, double gyroscope3,
                                                       double orientation1, double orientation2, double orientation3,
                                                       String movementType);


    private void stopBluetoothListener() {
        if (bluetoothListener != null) {
            bluetoothListener.stop();
        }
    }

    private void prepareBlueTooth() {
        prepareBluetoothWorker();
        prepareBluetoothExceptionhandler();
    }

    private void prepareBluetoothExceptionhandler() {
        exceptionHandler = new BluetoothListenerExceptionHandler() {
            @Override
            public void handleException(Exception e) {
                onExceptionEncountered(e);
            }
        };
    }

    private void onExceptionEncountered(Exception e) {
        stopBluetoothListener();
        closeBluetooth();
        startBluetooth();
    }

    private void startBluetooth() {
        bluetoothTask = new AsyncTask<Void, Integer, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                bluetoothCommunication = new BluetoothCommunication(ApplicationID.ICUB);
                try {
                    channel1 = bluetoothCommunication.getNewChannelAsListener();
                    channel2 = bluetoothCommunication.getNewChannelAsInitiator();

                    bluetoothListener = new BluetoothListener(channel1, bluetoothWorker, exceptionHandler);
                    bluetoothListener.start();

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Connected to Glass", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (final Exception e) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Error connecting to bluetooth "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                return null;
            }
        };
        bluetoothTask.execute();
    }

    private void prepareBluetoothWorker() {
        bluetoothWorker = new BluetoothWorker() {

            @Override
            public void doWork(BluetoothChannel bluetoothChannel) throws IOException, BluetoothException {
                float[] data = bluetoothChannel.receiveFloatArray(null);
                //Log.d("glasssensors", "data: "+data[0]+" "+data[1]+" "+data[2]+" "+data[3]+" "+data[4]+" "+data[5]);
                writeOntoBufferedGlassPort((double)data[0], (double)data[1], (double)data[2], (double)data[3], (double)data[4], (double)data[5]);
            }
        };
    }

    private void closeBluetooth() {
        if (bluetoothCommunication != null) {
            bluetoothCommunication.close();
        }
    }
}

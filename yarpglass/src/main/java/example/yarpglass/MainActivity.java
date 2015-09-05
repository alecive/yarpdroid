package example.yarpglass;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.glass.touchpad.GestureDetector;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import example.yarpcommon.ApplicationID;
import example.yarpcommon.FileSystemUtil;
import example.yarpcommon.ImageUtil;
import example.yarpcommon.UIUtil;
import example.yarpcommon.bluetooth.BluetoothChannel;
import example.yarpcommon.bluetooth.BluetoothCommunication;
import example.yarpcommon.bluetooth.BluetoothException;
import example.yarpcommon.bluetooth.BluetoothListener;
import example.yarpcommon.bluetooth.BluetoothListenerExceptionHandler;
import example.yarpcommon.bluetooth.BluetoothReceivingCallback;
import example.yarpcommon.bluetooth.BluetoothWorker;


public class MainActivity extends Activity {

    private ImageView imageView;
    private TextView textView;

    private BluetoothCommunication bluetoothCommunication;
    private BluetoothReceivingCallback receivingCallback;
    private BluetoothChannel channel1;
    private BluetoothChannel channel2;
    private BluetoothListener bluetoothListener;
    private BluetoothWorker bluetoothWorker;
    private BluetoothListenerExceptionHandler exceptionHandler;
    private AsyncTask<Void, Integer, Void> bluetoothTask;

    private String folderName;

    private AllSensorManager allSensorManager;
    private Timer timer;
    private TimerTask timerTask;

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UIUtil.keepScreenOn(this);
        setContentView(R.layout.activity_main);

        imageView = (ImageView)findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textview);

        folderName = FileSystemUtil.getCommonFolder("yarp");

        allSensorManager = new AllSensorManager(this, 10);

        gestureDetector = new GlassGestureDetectorAdapter(this) {
            @Override
            protected void onTap() {
                openOptionsMenu();
            }

            @Override
            protected void onSwipeDown() {
                finish();
                System.exit(0);
            }

        };

        prepareBlueTooth();

        startBluetooth();
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        if (gestureDetector != null) {
            return gestureDetector.onMotionEvent(event);
        }
        return super.onGenericMotionEvent(event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_start_sensor) {
            startSensor();
            return true;
        }

        if (id == R.id.action_stop_sensor) {
            stopSensor();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void closeBluetooth() {
        if (bluetoothCommunication != null) {
            bluetoothCommunication.close();
        }
    }

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
        showOnUIThread(textView, e.getMessage());
    }

    private void startBluetooth() {
        bluetoothTask = new AsyncTask<Void, Integer, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                bluetoothCommunication = new BluetoothCommunication(ApplicationID.ICUB);
                try {
                    showOnUIThread(textView, "Waiting for new connection");
                    channel1 = bluetoothCommunication.getNewChannelAsInitiator();
                    channel2 = bluetoothCommunication.getNewChannelAsListener();
                    showOnUIThread(textView, "Connected to: " + channel1.getConnectedTargetName());

                    bluetoothListener = new BluetoothListener(channel2, bluetoothWorker, exceptionHandler);
                    bluetoothListener.start();
                } catch (BluetoothException e) {
                    onExceptionEncountered(e);
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
                byte[] data = bluetoothChannel.receive(null);
                Bitmap bitmap = ImageUtil.convertImage(data);
                showOnUIThread(imageView, bitmap);
            }
        };
    }

    private void showOnUIThread(final TextView textview, final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textview.setText(text);
            }
        });
    }

    private void showOnUIThread(final ImageView imageView, final Bitmap bitmap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(bitmap);
            }
        });
    }

    private void startSensor() {
        allSensorManager.registerOrientationSensors();
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    channel1.sendFloatArray(allSensorManager.getOrientationDataAsFloats());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 1, 100);
    }

    private void stopSensor() {
        allSensorManager.unregisterOrientationSensors();
        timer.cancel();
    }
}

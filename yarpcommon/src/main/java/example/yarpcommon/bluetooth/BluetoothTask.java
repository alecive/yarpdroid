package example.yarpcommon.bluetooth;

import android.os.AsyncTask;

/**
 * Created by Shue Ching on 10/3/2015.
 */
public abstract class BluetoothTask< X, Y, Z> extends AsyncTask<X, Y, Z>{
    private boolean started;

    public BluetoothTask() {
        started = false;
    }

    public void setStarted() {
        started = true;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStopped() {
        started = false;
    }

    protected abstract Z doWork(X... params);

    @Override
    protected Z doInBackground(X... params) {
        Z result = null;
        if (started && !isCancelled()) {
            result = doWork(params);
        }
        return result;
    }
}

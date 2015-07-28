package com.alecive.yarpdroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import java.lang.Thread;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    TabsPagerAdapter mTabsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    TabLayout mTabLayout;

    DrawerLayout mDrawerLayout;

    String serverName = "/yarpdroid";
    String host       = "192.168.1.9";
    int    port       = 10000;

    private static final String TAG = "MainActivity";

    boolean initNetworkResult;

    private long stopPortHandle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mTabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mTabsPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);

        initToolbar();
        setupDrawerLayout();
        Log.i(TAG,"AbsolutePath for creating files:"+getFilesDir().getAbsolutePath());
//        long threadId = Thread.currentThread().getId();
//        Log.i(TAG,"Thread # " + threadId + " is doing this task");

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.statusbar_background));
        }

        stopPortHandle =0;
        register();
        initNetworkResult = false;

//        This command hides the ActionBar
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();
    }

    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_action_menu);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    private void setupDrawerLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Snackbar.make(mViewPager, menuItem.getTitle() + " pressed ", Snackbar.LENGTH_LONG).show();
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
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
        switch (item.getItemId()) {
            case R.id.action_preferences:
                Intent i = new Intent(this, PreferencesActivity.class);
                startActivity(i);
                return true;
            case R.id.action_search:
                setUserSettings();
                long threadId = Thread.currentThread().getId();
                Log.d(TAG, "Thread # " + threadId + " is doing this task");

                networkInitializer netIni = new networkInitializer();
                Thread iniNet = new Thread (netIni);
                iniNet.start();

                try {
                    iniNet.join(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (iniNet.isAlive()) {
                    // If the thread is still alive, it is still blocked on the method call
                    // So let's try stopping it
                    iniNet.interrupt();
                } else {
                    // The thread is finished, get the result
                    initNetworkResult = netIni.getIniNetRes();
                }

                String s = serverName + "@" + host + ":" + port +
                        " " + initNetworkResult;
                Snackbar.make(mViewPager, s, Snackbar.LENGTH_LONG).show();
                return true;
            case R.id.action_stop_now:
                String portToConnectTo = "/iolStateMachineHandler/motor_stop:i";
                boolean result = actionStopNow(portToConnectTo);
                Snackbar.make(mViewPager, "Emergency command on " +
                        portToConnectTo + "; result: " + result, Snackbar.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    public class networkInitializer implements Runnable {
        private boolean iniNetRes;

        public networkInitializer() { iniNetRes = false;}
        public void run() {
            long threadId = Thread.currentThread().getId();
            Log.d(TAG, "Thread # " + threadId + " is doing this task");
            try {
                iniNetRes = initNetwork(serverName,host,port);
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Log.e(TAG,"initNetwork probably got stuck. Interrupting corresponding thread.");
            }
        }
        public boolean getIniNetRes() {
            return iniNetRes;
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        mDrawerLayout.openDrawer(GravityCompat.START);
        return true;
    }

    private String setUserSettings() {
        SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        StringBuilder builder = new StringBuilder();

        serverName = sharedPrefs.getString("pref_yarp_namespace", "NULL");
        host       = sharedPrefs.getString("pref_yarp_server", "NULL");
        port       = Integer.parseInt(sharedPrefs.getString("pref_yarp_server_port", "NULL"));

        builder.append("Yarp Namespace Server: " + serverName);
        builder.append("\t Yarp Server IP:" + host);
        builder.append("\t Yarp Server Port: " + port);

        return builder.toString();
    }

    private boolean actionStopNow(String portToConnectTo) {
        boolean res = true;
        initNative();
        res = res & connectStopPort(portToConnectTo);
        res = res & writeStopMsg();
        finiNative();
        return res;
    }

    private void initNative() {
        if (stopPortHandle!=0) {
            String s="Native port has been already opened!";
            Snackbar.make(mViewPager, "WARN: "+s, Snackbar.LENGTH_LONG).show();
            Log.w(TAG,s);
            return;
        }

        Log.d(TAG,"I'm opening the native port");
        if(!createBufferedPort()) {
            createBufferedPort();
        }

        if (stopPortHandle!=0) {
            String s="Native port has been successfully opened!";
            Snackbar.make(mViewPager, s, Snackbar.LENGTH_LONG).show();
            Log.i(TAG, s);
        }
    }

    private void finiNative() {
        Log.d(TAG,"I'm closing the native port");
        if (stopPortHandle!=0) {
            if (destroyBufferedPort()) {
                stopPortHandle=0;
            }
        }
        else {
            String s="The native port is not open or has been already closed";
            Snackbar.make(mViewPager, "WARN: "+s, Snackbar.LENGTH_LONG).show();
            Log.w(TAG, s);
        }
    }

    public class TabsPagerAdapter extends FragmentPagerAdapter {

        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a STTFragment (defined as a static inner class below).
            switch (position) {
                case 0: // Fragment # 0 - This will show STTFragment
                    return STTFragment.newInstance(position + 1);
                case 1: // Fragment # 1 - This will show YarpviewFragment
                    return yarpviewFragment.newInstance(position + 1);
                case 2: // Fragment # 2 - This will show the CameraFragment
                    return cameraIntentFragment.newInstance(position + 1);
                case 3: // Fragment # 3 - This will show the demoCTPFragment
                    return demoCTPFragment.newInstance(position + 1);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // get item count - equal to number of tabs
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    String s1 = getString(R.string.title_section1);
                    return s1.toUpperCase(l);
                case 1:
                    String s2 = getString(R.string.title_section2);
                    return s2.toUpperCase(l);
                case 2:
                    String s3 = getString(R.string.title_section3);
                    return s3.toUpperCase(l);
                case 3:
                    String s4 = getString(R.string.title_section4);
                    return s4.toUpperCase(l);
            }
            return null;
        }
    }

    static{
        System.loadLibrary("yarpdroid");
        Log.i(TAG, "yarpdroid C++ library loaded successfully");
    }

    private native boolean register();
    public  native boolean initNetwork(String serverName, String host, int port);

    private native boolean createBufferedPort();
    private native boolean connectStopPort(String portToConnectTo);
    private native boolean writeStopMsg();
    private native boolean destroyBufferedPort();
}

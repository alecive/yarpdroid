package com.alecive.yarpdroid;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
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

    private static final String TAG = "MainActivity";

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
        Log.e(TAG,getFilesDir().getAbsolutePath());


        ClassLoader cl = ClassLoader.getSystemClassLoader();

        try {
            Class stringClass = ClassLoader.getSystemClassLoader().loadClass("java/lang/String");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            Class civeCLass = this.getClass().getClassLoader().loadClass("com/alecive/yarpdroid/MainActivity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.abc_ic_search_api_mtrl_alpha);
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
                Snackbar.make(mViewPager, menuItem.getTitle() + " pressed " + yarpdroid(), Snackbar.LENGTH_LONG).show();
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
            case R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class yarpSTTFragment extends Fragment {

        private TextView txtSpeechInput;
        private ImageButton btnSpeak;
        private final int REQ_CODE_SPEECH_INPUT = 100;

        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private static void staticTestMethod(String str)
        {
            Log.i(TAG,"ciao come stai "+str);
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static yarpSTTFragment newInstance(int sectionNumber) {
            yarpSTTFragment fragment = new yarpSTTFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public yarpSTTFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            txtSpeechInput = (TextView) rootView.findViewById(R.id.txtSpeechInput);
            btnSpeak = (ImageButton) rootView.findViewById(R.id.btnSpeak);

            btnSpeak.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    promptSpeechInput();
                }
            });

            return rootView;
        }

        /**
         * Showing google speech input dialog
         */
        private void promptSpeechInput() {
//            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                    RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//            intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
//                    getString(R.string.speech_prompt));
//            try {
//                startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
//            } catch (ActivityNotFoundException a) {
//                Toast.makeText(getActivity().getApplicationContext(),
//                        getString(R.string.speech_not_supported),
//                        Toast.LENGTH_SHORT).show();
//            }
            testCallback();
        }

        /**
         * Receiving speech input
         */
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            switch (requestCode) {
                case REQ_CODE_SPEECH_INPUT: {
                    if (resultCode == RESULT_OK && null != data) {

                        ArrayList<String> result = data
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        txtSpeechInput.setText(result.get(0));
                    } else {
                        Log.w("MainActivity", "No valid speech output has ben received");
                    }
                    break;
                }

            }
        }
    }

    public static class TabsPagerAdapter extends FragmentPagerAdapter {

        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a yarpSTTFragment (defined as a static inner class below).
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return yarpSTTFragment.newInstance(position + 1);
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return yarpSTTFragment.newInstance(position + 1);
                case 2: // Fragment # 1 - This will show SecondFragment
                    return yarpSTTFragment.newInstance(position + 1);
                default:
                    return null;
            }
        }
        @Override
        public int getCount() {
            // get item count - equal to number of tabs
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "STT".toUpperCase(l);
                case 1:
                    return "view".toUpperCase(l);
                case 2:
                    return "boh".toUpperCase(l);
            }
            return null;
        }
    }

    static{
        System.loadLibrary("yarpdroid");
        Log.i(TAG, "yarpdroid loaded successfully");
    }

    public native String yarpdroid();
    public static native void testCallback();
}

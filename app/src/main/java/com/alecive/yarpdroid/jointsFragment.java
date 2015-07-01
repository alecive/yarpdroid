package com.alecive.yarpdroid;

import android.support.v4.app.Fragment;
import android.os.Bundle;

/**
 * Created by alecive on 29/06/15. This is the third tab
 */
public class jointsFragment extends Fragment {

    private static final String TAG = "jointsFragment";
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static jointsFragment newInstance(int sectionNumber) {
        jointsFragment fragment = new jointsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }
}

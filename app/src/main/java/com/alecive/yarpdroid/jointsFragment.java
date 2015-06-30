package com.alecive.yarpdroid;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by alecive on 29/06/15. This is the third tab
 */
public class jointsFragment extends Fragment {

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
}

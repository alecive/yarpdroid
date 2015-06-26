package com.alecive.yarpdroid;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by alecive on 22/06/15. Blah
 */
public class yarpviewFragment extends Fragment {

    private static final String TAG = "yarpviewFragment";
    private ImageView imgLeft;
    private ImageView imgRight;

    private long viewLeftHandle;
    private long viewRightHandle;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static yarpviewFragment newInstance(int sectionNumber) {
        yarpviewFragment fragment = new yarpviewFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public yarpviewFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.yarpview_fragment, container, false);

        imgLeft  = (ImageView) rootView.findViewById(R.id.imgLeft);
        imgRight = (ImageView) rootView.findViewById(R.id.imgRight);

        return rootView;
    }

    private        native void    createBufferedImgPortL();
    private        native void    createBufferedImgPortR();
//    private native void getImageRight();
//    private native void getImageLeft();
}

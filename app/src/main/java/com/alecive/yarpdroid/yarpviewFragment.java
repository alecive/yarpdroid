package com.alecive.yarpdroid;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;

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
    public void onDestroy() {
//        destroyBufferedImgPortL();
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.yarpview_fragment, container, false);

        imgLeft  = (ImageView) rootView.findViewById(R.id.imgLeft);
        imgRight = (ImageView) rootView.findViewById(R.id.imgRight);

        register();
//        createBufferedImgPortL();
//        destroyBufferedImgPortL();
//        createBufferedImgPortL();

        return rootView;
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

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        final int height = displaymetrics.heightPixels;
        final int width = displaymetrics.widthPixels;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (bitmap==null) {
                    Log.e(TAG,"Bitmap is NULL!");
                }
                else {
                    view.setImageBitmap(Bitmap.createScaledBitmap(bitmap,720,540,false));
                }
            }
        });

        Log.i(TAG, "setImageViewWithByteArray finished!" + width + " " + height);

//        File photo = null;
//
//        File docsFolder = new File(Environment.getExternalStorageDirectory() + "/CiveTest");
//        boolean isPresent = true;
//        if (!docsFolder.exists()) {
//            isPresent = docsFolder.mkdir();
//        }
//        if (isPresent) {
//            photo = new File(docsFolder.getAbsolutePath(),"imgOrig.bmp");
//        }
//
//        if(photo.exists()) {
//            final Bitmap myBitmap = BitmapFactory.decodeFile(photo.getAbsolutePath());
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (myBitmap==null) {
//                        Log.e(TAG,"Bitmap from FILE is NULL!");
//                    }
//                    else {
//                        view.setImageBitmap(myBitmap);
//                    }
//                }
//            });
//        }

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

    private        native boolean register();
    private        native void    getImgReceivedonPort(byte[] img);
    private        native void    getImgReceivedonPortStr(String imgStr);
    private        native void    createBufferedImgPortL();
    private        native void    destroyBufferedImgPortL();
}

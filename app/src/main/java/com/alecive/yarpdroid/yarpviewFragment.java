package com.alecive.yarpdroid;

import android.os.Environment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;

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
        destroyBufferedImgPortL();
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
        createBufferedImgPortL();
        destroyBufferedImgPortL();
        createBufferedImgPortL();

        return rootView;
    }

    public void setImageViewWithByteArray(ImageView view, byte[] data) {
        long threadId = Thread.currentThread().getId();
        Log.i(TAG,"Thread #" + threadId + " is doing this task");
        if (data==null) {
            Log.e(TAG,"input image is NULL!");
        }
        Log.i(TAG, "I am in setImageViewWithByteArray. Size of the byte array to display: "+data.length);

//        String string = "";
//        for(byte c : data) {
//            string = string + " " + c;
//        }
//        Log.w(TAG,string);

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inJustDecodeBounds = true;
        opt.outWidth           = 320;
        opt.outHeight          = 240;
        opt.inDensity          = 3;
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,opt);

        if (bitmap==null) {
            Log.e(TAG,"Bitmap is NULL!");
            view.setImageResource(R.drawable.abc_btn_radio_material);
        }
        else {
            view.setImageBitmap(bitmap);
        }

//        File photo=new File(getActivity().getFilesDir().getAbsolutePath(), "img.bmp");
//
//        if (photo.exists()) {
//            photo.delete();
//        }
//
//        try {
//            FileOutputStream fos=new FileOutputStream(photo.getPath());
//
//            fos.write(data[0]);
//            fos.close();
//        }
//        catch (java.io.IOException e) {
//            Log.e("PictureDemo", "Exception in photoCallback", e);
//        }
//
//        File imgFile = new File(getActivity().getFilesDir().getAbsolutePath(), "img.bmp");
//
//        if(imgFile.exists()){
//             Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//            if (myBitmap==null) {
//                Log.e(TAG,"Bitmap is NULL!");
//            }
//             view.setImageBitmap(myBitmap);
//        }

    }

    public void setImgLeft(final byte[] data) {
        long threadId = Thread.currentThread().getId();
        Log.i(TAG,"Thread #" + threadId + " is doing this task");
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setImageViewWithByteArray(imgLeft, data);
            }
        });
    }

    private        native boolean register();
    private        native void    getImgReceivedonPort(byte[] img);
    private        native void    createBufferedImgPortL();
    private        native void    destroyBufferedImgPortL();
}

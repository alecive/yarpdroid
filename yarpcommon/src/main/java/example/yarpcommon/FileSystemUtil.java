package example.yarpcommon;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by ivs4nsc on 8/5/2015.
 */
public class FileSystemUtil {

    private static String TAG = "sg.edu.astar.i2r.vc.commonlibrary.util.FileSystemUtil";

    public static String getFilenameWithoutExtension(String filename) {
        return filename.replaceFirst("[.][^.]+$", "");
    }

    public static byte[] readRawResource(int resourceId, Context ctx) {
        byte[] b = null;
        try {
            Resources res = ctx.getResources();
            InputStream inS = res.openRawResource(resourceId);

            b = new byte[inS.available()];
            inS.read(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
    }

    public static File getApplicationBaseFolder(Context context) {
        return context.getFilesDir().getAbsoluteFile();
    }

    public static String getCommonFolder(String applicationName) {
        String commonFolderString = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + applicationName + "/";
        File folder = new File(commonFolderString);
        if (!folder.exists()) {
            folder.mkdir();
        }
        return commonFolderString;
    }

    public static void refreshFilesVisibilityOnDevice(Context context, String[] filenames) {
        for (String filename : filenames) {
            if (filename.length() > 0) {
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(filename))));
            }
        }
    }

    public static void refreshFilesVisibilityOnDevice(Context context, String filename) {
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(filename))));
    }

    public static boolean isImageFilename(String filename) {
        String[] tokens = filename.split("\\.");
        if (tokens.length == 2 && (tokens[1].equals("jpg") || tokens[1].equals("png") || tokens[1].equals("bmp"))) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isVideoFilename(String filename) {
        String[] tokens = filename.split("\\.");
        if (tokens.length == 2 && (tokens[1].equals("mp4") || tokens[1].equals("3pg") || tokens[1].equals("avi"))) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean copyFileFromAssetToDevice(Context context, String assetFilename, String fullDestinationFilename) {
        boolean success;

        try {
            InputStream inputStream = context.getAssets().open(assetFilename);
            OutputStream outputStream = new FileOutputStream(fullDestinationFilename);

            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            //Close the streams
            outputStream.flush();
            outputStream.close();
            inputStream.close();

            success = true;
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
            success = false;
        }

        return success;
    }

    public static boolean copyFileFromRawToDevice(Context context, int rawResourceId, String fullDestinationFilename) {
        boolean success = false;

        byte[] bytes;
        bytes = readRawResource(rawResourceId, context);

        try {
            OutputStream outputStream = new FileOutputStream(fullDestinationFilename);
            outputStream.write(bytes, 0, bytes.length);

            //Close the streams
            outputStream.flush();
            outputStream.close();

            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return success;
    }

    public static void copyAllFileOrDir(Context context, String assetFolderName, String targetFolderName) {
        if (assetFolderName.startsWith("images") || assetFolderName.startsWith("sounds") || assetFolderName.startsWith("webkit")) {
            return;
        }

        AssetManager assetManager = context.getAssets();
        String assets[] = null;
        try {
            assets = assetManager.list(assetFolderName);
            String targetFullPath = targetFolderName + "/" + assetFolderName;
            if (assets.length == 0) {
                boolean success = copyFileFromAssetToDevice(context, assetFolderName, targetFullPath);
                Log.i(TAG, success + " copy file from " + assetFolderName + " to " + targetFullPath);

            } else {
                File dir = new File(targetFullPath);
                if (!dir.exists()) {
                    if (dir.mkdirs()) {
                        Log.i(TAG, "created new folder " + targetFullPath);
                    }
                }

                for (int i = 0; i < assets.length; ++i) {
                    String p;
                    if (assetFolderName.equals("")) {
                        p = "";
                    } else {
                        p = assetFolderName + "/";
                    }

                    p = p + assets[i];
                    copyAllFileOrDir(context, p, targetFolderName);
                }
            }
        } catch (IOException ex) {
            Log.e(TAG, "I/O Exception", ex);
        }
    }

    public static void deleteFile(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            file.delete();
        }
    }
}

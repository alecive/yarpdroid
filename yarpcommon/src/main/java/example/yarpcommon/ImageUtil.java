package example.yarpcommon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class ImageUtil {

	public static byte[] resizeImageData(byte[] data, int resizeWidth,
			int resizeHeight) {
		Bitmap original = convertImage(data);
		Bitmap resized = Bitmap.createScaledBitmap(original, resizeWidth,
				resizeHeight, true);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		resized.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		return baos.toByteArray();
	}

	public static Bitmap convertImage(byte[] data) {
		return BitmapFactory.decodeByteArray(data, 0, data.length);
	}

	public static byte[] convertImage(Bitmap bitmap) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
		return byteArrayOutputStream .toByteArray();
	}

	public static byte[] convertImage(Bitmap bitmap, int quality) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream);
		return byteArrayOutputStream .toByteArray();
	}

	public static byte[] compressImage(byte[] data, int imageFormat, int width,
			int height, int quality) {
		YuvImage image = new YuvImage(data, imageFormat, width, height, null);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compressToJpeg(new Rect(0, 0, width, height), quality, baos);
		return baos.toByteArray();
	}

	public static void saveImageAsJPEG(Bitmap bitmap, String filename,
			int quality) {
		try {
			OutputStream fOut = new FileOutputStream(filename);
			bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fOut);
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
		}
	}

	public static Bitmap convertToGrayScale(Bitmap bitmap) {
		int width, height;
		height = bitmap.getHeight();
		width = bitmap.getWidth();

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bitmap, 0, 0, paint);
		return bmpGrayscale;
	}

    public static Bitmap convertStringToBitmap(String data) {
        Bitmap bitmap = null;
        try {
            byte[] decodedString = Base64.decode(data, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

	public static String convertBitmapToString(Bitmap bitmap, int quality) {
		byte[] byteArray = convertImage(bitmap, quality);
		return Base64.encodeToString(byteArray, Base64.DEFAULT);
	}

    public static Bitmap scaledUpImageToMeetOrExceedTargetImageDimensions(Bitmap inputImage, int targetWidth, int targetHeight) {
        double widthRatio = (double)inputImage.getWidth()/targetWidth;
        double heightRatio = (double)inputImage.getHeight()/targetHeight;
        double minRatio = (widthRatio < heightRatio ? widthRatio : heightRatio);
        int scaledUpWidth = (int)((double)inputImage.getWidth() / minRatio);
        int scaledUpHeight = (int)((double)inputImage.getHeight() / minRatio);
        return Bitmap.createScaledBitmap(inputImage, scaledUpWidth,
                scaledUpHeight, true);
        //return rescaleMat(inputImage, scaledUpWidth, scaledUpHeight);
    }
}

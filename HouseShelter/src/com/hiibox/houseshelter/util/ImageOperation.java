package com.hiibox.houseshelter.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Environment;

    
  
  
    
  
public class ImageOperation {

	public static boolean isSave(String path, String imageUrl) {
		File dir = new File(Environment.getExternalStorageDirectory() + path);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		File file = new File(Environment.getExternalStorageDirectory()
				+ path
				+ imageUrl.substring(imageUrl.lastIndexOf("/") + 1,
						imageUrl.length()));
		return file.isFile();
	}

	public static void saveFile(String path, String imageUrl) {
		File file = new File(path + "/" + imageUrl.hashCode());
		try {
			InputStream is = new FileInputStream(file);

			FileOutputStream fos = new FileOutputStream(path
					+ imageUrl.substring(imageUrl.lastIndexOf("/") + 1,
							imageUrl.length()));
			int data = is.read();
			while (data != -1) {
				fos.write(data);
				data = is.read();
			}
			fos.close();
			is.close();
		} catch (Exception e) {
			                                   
			e.printStackTrace();
			System.out.println("±£´æÍ¼Æ¬Òì³£");
		}

	}

	    
  
  
  
  
  
  
	public static boolean copy(String fromFile, String toPath, String newName) {
		File root = new File(fromFile);
		                       
		                   
		if (!root.exists()) {
			return false;
		}
		        
		File targetDir = new File(toPath);
		        
		if (!targetDir.exists()) {
			targetDir.mkdirs();
		}
		String toFile = toPath + "/" + newName;
		try {
			InputStream fosfrom = new FileInputStream(fromFile);
			OutputStream fosto = new FileOutputStream(toFile);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0) {
				fosto.write(bt, 0, c);
			}
			fosfrom.close();
			fosto.close();
			return true;

		} catch (Exception ex) {
			return false;
		}
	}

	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}

	public static Bitmap readNoImageBitmap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		Bitmap bitmap = Bitmap
				.createBitmap(
						drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(),
						drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
								: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight());
		drawable.draw(canvas);
		return bitmap;
	}

	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	public static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			                                                            
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	    
  
  
  
  
  
  
	public static boolean hasFileExits(String fileName) {
		File file = new File(fileName);
		if (file.exists()) {
			return true;
		} else {
			return false;
		}
	}

	    
  
  
  
  
  
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {

		 Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
		 bitmap.getHeight(), Config.ARGB_8888);
		                                                   

		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = bitmap.getWidth() / 2;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	    
  
  
  
  
	public static Bitmap createRepeater(int width, Bitmap src) {
		int count = (width + src.getWidth() - 1) / src.getWidth();

		Bitmap bitmap = Bitmap.createBitmap(width, src.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);

		for (int idx = 0; idx < count; ++idx) {

			canvas.drawBitmap(src, idx * src.getWidth(), 0, null);
		}

		return bitmap;
	}

	    
  
  
  
  
  
  
  
  
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		          
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	    
  
  
  
  
  
	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis);                   

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	    
  
  
	public static Bitmap ImageCrop(Bitmap bitmap) {
		int w = bitmap.getWidth();             
		int h = bitmap.getHeight();

		int wh = w > h ? h : w;                 

		int retX = w > h ? (w - h) / 2 : 0;                   
		int retY = w > h ? 0 : (h - w) / 2;

		           
		return Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null, false);
	}

}
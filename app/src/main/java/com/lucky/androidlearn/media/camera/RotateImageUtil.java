package com.lucky.androidlearn.media.camera;

import java.io.IOException;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

/**旋转图片*/
public class RotateImageUtil {

	/**
	 * 获取要旋转的角度
	 * @param path
	 * @return
	 */
	public static int readPictureDegree(String path) {
        int degree  = 0;
        try {
                ExifInterface exifInterface = new ExifInterface(path);
//              int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
                System.out.println("filepath..."+path+"...orientation..."+orientation);
                switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                        degree = 90;
                        break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                        degree = 180;
                        break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                        degree = 270;
                        break;
                }
        } catch (IOException e) {
                e.printStackTrace();
        }
        return degree;
    }
  
	
	/**
    * 旋转图片 
    * @param angle 
    * @param bitmap 
    * @return Bitmap 
    */ 
   public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {  
       //旋转图片 动作   
	   //在这块可以做�?个黑名单
	   //遇见此种机型可以处理
//	   String model=android.os.Build.MODEL;
	   Bitmap resizedBitmap=null;
//	   if(MyApplication.initInstance().getPhonesList().contains(model)){
//	       Matrix matrix = new Matrix();  
//	       matrix.postRotate(90);  
//	       // 创建新的图片   
//	       resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,  
//	       bitmap.getWidth(), bitmap.getHeight(), matrix, true); 
//	   }else{
//		   Matrix matrix = new Matrix();  
//		   matrix.postRotate(angle);  
//		   // 创建新的图片   
//		   resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,  
//				   bitmap.getWidth(), bitmap.getHeight(), matrix, true);  
//	   }
	   Matrix matrix = new Matrix();  
	   matrix.postRotate(angle);  
	   // 创建新的图片   
	   resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,  
			   bitmap.getWidth(), bitmap.getHeight(), matrix, true);  

       return resizedBitmap;  
   }
	
}

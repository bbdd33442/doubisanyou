package com.doubisanyou.baseproject.utilsResource;

import android.graphics.BitmapFactory;

/** 
 * <p>图片压缩处理</p> 
 */
public class BitmapCompressUtil {
	/**
	 * <p>功能描述</p>
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return 
	 */
	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
	    int initialSize = computeInitialSampleSize(options.outWidth,options.outHeight, minSideLength, maxNumOfPixels);

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

	private static int computeInitialSampleSize(double w,double h, int minSideLength, int maxNumOfPixels) {

	    int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
	    int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

	    if (upperBound < lowerBound) {
	        // return the larger one when there is no overlapping zone.
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
	
	public static int computeSampleSize( double w,double h,int minSideLength, int maxNumOfPixels){
		int initialSize = computeInitialSampleSize(w,h, minSideLength, maxNumOfPixels);
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
}

package com.doubisanyou.appcenter.widget;



import com.doubisanyou.baseproject.utilsResource.ImageLoader;
import com.doubisanyou.baseproject.utilsResource.ImageLoader.Type;

import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;




public class ViewHolder {

    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
    

	/**
	 * 为TextView设置字符串
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public static ViewHolder setText(View view,int viewId, String text)
	{
		TextView textView = get(view,viewId);
		textView.setText(text);
		return null;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public static ViewHolder setImageResource(View view,int viewId, int drawableId)
	{
		ImageView imageView = get(view,viewId);
		imageView.setImageResource(drawableId);

		return null;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public static ViewHolder setImageBitmap(View view,int viewId, Bitmap bm)
	{
		ImageView imageView = get(view,viewId);
		imageView.setImageBitmap(bm);
		return null;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 * @param drawableId
	 * @return
	 */
	public static ViewHolder setImageByUrl(View view,int viewId, String url)
	{
		ImageLoader.getInstance(3,Type.LIFO).loadImage(url, (ImageView) get(view,viewId));
		return null;
	}

}



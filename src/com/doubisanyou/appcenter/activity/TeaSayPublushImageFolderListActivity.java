package com.doubisanyou.appcenter.activity;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.doubisanyou.appcenter.R;
import com.doubisanyou.appcenter.adapter.ImageFolderListAdapter;
import com.doubisanyou.appcenter.bean.ImageFloder;

public class TeaSayPublushImageFolderListActivity extends Activity implements OnItemClickListener{
	
	private ImageFolderListAdapter ifl;
	
	private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();
	
	private HashSet<String> mDirPaths = new HashSet<String>();
	
	private ArrayList<String> selcetedImageName = new ArrayList<String>();
	
	ProgressDialog mProgressDialog;
	
	private int mPicsSize;
	
	int totalCount = 0;
	
	ListView folderDirList ;
	
	public static final String TEASYPUBLISH = "teasaypublish";
	
	public static final String USERAVATARS = "useravatars";
	
	public static final String ACTIVITYTYPE = "activtytype";
	
	String type;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tea_say_image_list_dir);
		Intent i = getIntent();
		selcetedImageName = i.getStringArrayListExtra(TeaSayImageSelectedViewActivity.SELECTED_IMAGE_PATH);
		type = i.getStringExtra(ACTIVITYTYPE);
		folderDirList = (ListView) findViewById(R.id.tea_say_image_dir_list);
		folderDirList.setOnItemClickListener(this);
		getImages();
	}
	
	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				mProgressDialog.dismiss();
				ifl= new ImageFolderListAdapter(mImageFloders,getApplicationContext());
				folderDirList.setAdapter(ifl);
				break;

			default:
				break;
			}
		}
		
	};
	
	private void getImages()
	{
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
		{
			Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
			return;
		}
		// 显示进度条
		mProgressDialog = ProgressDialog.show(this, null, "正在加载...");

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{

				String firstImage = null;

				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = TeaSayPublushImageFolderListActivity.this
						.getContentResolver();

				// 只查询jpeg和png的图片
				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png" },
						MediaStore.Images.Media.DATE_MODIFIED);

				Log.e("TAG", mCursor.getCount() + "");
				while (mCursor.moveToNext())
				{
					// 获取图片的路径
					String path = mCursor.getString(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATA));

					Log.e("TAG", path);
					// 拿到第一张图片的路径
					if (firstImage == null)
						firstImage = path;
					// 获取该图片的父路径名
					File parentFile = new File(path).getParentFile();
					if (parentFile == null)
						continue;
					String dirPath = parentFile.getAbsolutePath();
					ImageFloder imageFloder = null;
					// 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
					if (mDirPaths.contains(dirPath))
					{
						continue;
					} else
					{
						mDirPaths.add(dirPath);
						// 初始化imageFloder
						imageFloder = new ImageFloder();
						imageFloder.setDir(dirPath);
						imageFloder.setFirstImagePath(path);
					}

					int picSize = parentFile.list(new FilenameFilter()
					{
						@Override
						public boolean accept(File dir, String filename)
						{
							if (filename.endsWith(".jpg")
									|| filename.endsWith(".png")
									|| filename.endsWith(".jpeg")
									||filename.endsWith(".JPEG"))
								return true;
							return false;
						}
					}).length;
					totalCount += picSize;

					imageFloder.setCount(picSize);
					mImageFloders.add(imageFloder);

					if (picSize > mPicsSize)
					{
						mPicsSize = picSize;
						/*mImgDir = parentFile;*/
					}
				}
				mCursor.close();

				// 扫描完成，辅助的HashSet也就可以释放内存了
				mDirPaths = null;

				// 通知Handler扫描图片完成
				mHandler.sendEmptyMessage(1);

			}
		}).start();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		File parentFile = new File(mImageFloders.get(position).getDir());
		List<String>imageName = Arrays.asList(parentFile.list(new FilenameFilter()
		{
			@Override
			public boolean accept(File dir, String filename)
			{
				if (filename.endsWith(".jpg") || filename.endsWith(".png")
						|| filename.endsWith(".jpeg")|| filename.endsWith(".JPEG"))
					return true;
				return false;
			}
		}));
		Intent i = new Intent(TeaSayPublushImageFolderListActivity.this,TeaSayImageSelectedViewActivity.class);
		i.putExtra(TeaSayImageSelectedViewActivity.FOLDER_DIR,mImageFloders.get(position).getDir());
		i.putStringArrayListExtra(TeaSayImageSelectedViewActivity.FOLDER_IMAGE_NAME, new ArrayList<String>(imageName));
		i.putStringArrayListExtra(TeaSayImageSelectedViewActivity.SELECTED_IMAGE_PATH, selcetedImageName);
		i.putExtra(ACTIVITYTYPE, type);
		startActivityForResult(i, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==0){
			selcetedImageName = data.getStringArrayListExtra(TeaSayImageSelectedViewActivity.SELECTED_IMAGE_PATH);
		}else if(resultCode==1){
			finish();
		}
		
	}
}

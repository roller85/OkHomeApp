package id.co.okhome.okhomeapp.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import id.co.okhome.okhomeapp.R;
import id.co.okhome.okhomeapp.lib.OkHomeActivityParent;

public class CropImageActivity extends OkHomeActivityParent implements OnClickListener{

	CropImageView mCropImageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_cropimage);
		findViewById(R.id.actCropImage_cancel).setOnClickListener(this);
		findViewById(R.id.actCropImage_rotate).setOnClickListener(this);
		findViewById(R.id.actCropImage_done).setOnClickListener(this);

		mCropImageView = (CropImageView) findViewById(R.id.actCropImage_cropImageView);
		mCropImageView.setAspectRatio(1, 1);
		mCropImageView.setFixedAspectRatio(true);

		Uri dataUri = Uri.parse(getIntent().getStringExtra("data-uri"));
		
		String filePath = dataUri.getPath();
		
		if (dataUri.toString().contains("file://")) {
			Options opts = new Options();
			opts.inJustDecodeBounds = true;
			opts.inPreferredConfig = Config.RGB_565;
			BitmapFactory.decodeFile(filePath, opts);
			
			try {
				int sampleSize = calculateBitmapSampleSize(opts);
				opts.inSampleSize = sampleSize;
				opts.inJustDecodeBounds = false;
				
				Bitmap bm = BitmapFactory.decodeFile(filePath, opts);
				mCropImageView.setImageBitmap(bm);
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if (dataUri.toString().contains("content://")) {
			try {
				
				InputStream is = null;
				Options options = new Options();
				options.inJustDecodeBounds = true;
				try {
					is = getContentResolver().openInputStream(dataUri);
					BitmapFactory.decodeStream(is, null, options); // Just get image size
				} finally {
					is.close();
				}
				int sampleSize = calculateBitmapSampleSize(options);
//				InputStream is = getContentResolver().openInputStream(dataUri);
				is = getContentResolver().openInputStream(dataUri);
				Options option = new Options();
				option.inSampleSize = sampleSize;
				Bitmap bm = BitmapFactory.decodeStream(is, null, option);
				mCropImageView.setImageBitmap(bm);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private class BitmapCompressTask extends AsyncTask<String, Integer, String> {

		ProgressDialog p;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			p = ProgressDialog.show(CropImageActivity.this, null, "Loading");
		}

		@Override
		protected String doInBackground(String... params) {

			String filePath = null;

			try {
				filePath = compressImage();
			} catch (IOException e) {
				Log.d(CropImageActivity.class.getName(), e.getMessage());
			}
			return filePath;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			p.dismiss();

			if (result != null) {
				Intent i = new Intent();
				i.putExtra("cropped-path", result);
				setResult(RESULT_OK, i);
				finish();
			}
		}
	}

	private int calculateBitmapSampleSize(Options options) throws IOException {
		
		int maxSize = 2048;
		int sampleSize = 1;
		while (options.outHeight / sampleSize > maxSize || options.outWidth / sampleSize > maxSize) {
			sampleSize = sampleSize << 1;
		}
		return sampleSize;
	}

	private String compressImage() throws IOException {
		Bitmap croppedBitmap = mCropImageView.getCroppedImage();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");
		String timestamp = dateFormat.format(new Date(System.currentTimeMillis()));

		File cropFile = new File(getCacheDir(), "crop_"+ timestamp + ".jpg");

		OutputStream outputStream = null;

		outputStream = new FileOutputStream(cropFile);
		croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
		outputStream.close();
		String filePath = cropFile.getPath();
		return filePath;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.actCropImage_cancel:
			setResult(RESULT_CANCELED);
			finish();
			break;

		case R.id.actCropImage_done:
			new BitmapCompressTask().execute("");
			break;

		case R.id.actCropImage_rotate:
			mCropImageView.rotateImage(90);
			break;
		}
	}
}

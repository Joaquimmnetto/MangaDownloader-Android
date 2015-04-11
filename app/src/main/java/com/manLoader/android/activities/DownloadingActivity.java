package com.manLoader.android.activities;

import com.manLoader.android.services.DownloadingService;
import com.manLoader.pojo.Manga;
import com.manLoader.utils.MyLog;
import com.manLoader.utils.ViewUtils;
import com.mangoDownload.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class DownloadingActivity extends Activity {

	private Manga downloadingManga;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_downloading);
		init();
		build();
	}

	private void init() {
		downloadingManga = (Manga) getIntent().getSerializableExtra(
				DetailsActivity.BUNDLE_MANGA);
	}

	private void build() {
		// set title
		ViewUtils.setTextinTextView(this, downloadingManga.getName(),
				R.id.downloading_mangaTitle);
		// set image
		((ImageView) findViewById(R.id.downloading_mangaImage))
				.setImageBitmap(BitmapFactory.decodeByteArray(
						downloadingManga.getCoverImage(), 0,
						downloadingManga.getCoverImage().length));
		// set listener
		((Button) findViewById(R.id.downloading_stopDownload))
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						onCancelDownload();
					}
				});
	}

	// cancel listener action
	private void onCancelDownload() {
		ViewUtils.setTextinTextView(this, "Download stopped",
				R.id.downloading_genStatus);
		Intent serviceIntent = new Intent(getApplicationContext(),
				DownloadingService.class);
		boolean isStopped = stopService(serviceIntent);
		MyLog.getInstance().debug(DownloadingActivity.class, "Is service stopped:"+isStopped);
		
	}

}


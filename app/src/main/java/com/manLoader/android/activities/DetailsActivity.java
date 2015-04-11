package com.manLoader.android.activities;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ExecutionException;

import com.manLoader.android.services.DownloadingService;
import com.manLoader.core.buscaDown.controller.MangaControler;
import com.manLoader.exceptions.ChapterNonExcizteException;
import com.manLoader.pojo.Manga;
import com.manLoader.pojo.SearchResult;
import com.manLoader.utils.MyLog;
import com.manLoader.utils.ViewUtils;
import com.mangoDownload.R;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class DetailsActivity extends Activity {

	public static final String BUNDLE_MANGA = "Manga";
	private SearchResult result;
	private Manga manga;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.screen_details);
		try {
			init();
            build();
		} catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            e.printStackTrace();

            finish();
        }

	}

	private void init() throws IOException, InterruptedException,
			ExecutionException {
		result = (SearchResult) getIntent().getSerializableExtra(
				SearchActivity.BUNDLE_SEARCH_RESULT);
		manga = MangaControler.buildMangaFromSearchResult(result);
	}

	private void build() {
		ViewUtils.setTextinTextView(this, manga.getName(),
				R.id.downloading_mangaTitle);
		ViewUtils.setTextinTextView(this, manga.getAuthor(),
				R.id.details_mangaAuthor);
		ViewUtils.setTextinTextView(this, manga.getNumChapters() + " chapters",
				R.id.details_mangaDetails);
		ViewUtils.setTextinTextView(this, manga.getSinopse(),
				R.id.downloading_chapStatus);

		ImageView mangaImage = (ImageView) findViewById(R.id.downloading_mangaImage);
		mangaImage.setImageBitmap(BitmapFactory.decodeByteArray(
				manga.getCoverImage(), 0, manga.getCoverImage().length));

		((Button) findViewById(R.id.downloading_stopDownload))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							onDownloadClick();
						} catch (ChapterNonExcizteException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
				});
	}

	private void onDownloadClick() throws IOException,
			ChapterNonExcizteException {
		
		Intent serviceIntent = new Intent(getApplicationContext(),
				DownloadingService.class);
		serviceIntent.putExtra(BUNDLE_MANGA, manga);
		startService(serviceIntent);
		//createDownloadNotification();
		
		Intent actyIntent = new Intent(getApplicationContext(),DownloadingActivity.class);
		actyIntent.putExtra(BUNDLE_MANGA, manga);
		startActivity(actyIntent);
	}



}

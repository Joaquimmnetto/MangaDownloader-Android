package com.manLoader.android.services;

import java.io.IOException;

import com.manLoader.android.activities.DetailsActivity;
import com.manLoader.android.activities.DownloadingActivity;
import com.manLoader.core.buscaDown.controller.MangaControler;
import com.manLoader.core.buscaDown.controller.MangaDownloadControler;
import com.manLoader.exceptions.ChapterNonExcizteException;
import com.manLoader.pojo.Manga;
import com.manLoader.utils.DownloadHelper;
import com.manLoader.utils.MyLog;
import com.manLoader.utils.ThreadQueue;
import com.mangoDownload.R;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class DownloadingService extends IntentService {

	public static final int NOTIFICATION_ID = 1;
	private Manga downloadingManga;

	public DownloadingService() {
		super("DownloadingService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			downloadingManga = (Manga) intent
					.getSerializableExtra(DetailsActivity.BUNDLE_MANGA);

			Intent notifyIntent = new Intent(this, DownloadingActivity.class);
			notifyIntent.putExtra(DetailsActivity.BUNDLE_MANGA,
					downloadingManga);
			notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent pIntent = PendingIntent.getActivity(this, 0,
					notifyIntent, 0);

            createDownloadNotification();
//			Notification noti = new NotificationCompat.Builder(this)
//					.setContentTitle(
//							"Now downloading " + downloadingManga.getName())
//					.setContentText("Click here to see status")
//					.setSmallIcon(R.drawable.notification_icon)
//					.setContentIntent(pIntent)// TODO: Make(HAHAHA, get one from
//												// Internet) icon
//					.build();
//			startForeground(NOTIFICATION_ID, noti);

			MyLog.getInstance().debug(
					DownloadingService.class,
					"Starting downloading service for:"
							+ downloadingManga.getName());
			MangaDownloadControler controler = new MangaDownloadControler();
			controler.addMangaToLibrary(downloadingManga);

		} catch (ChapterNonExcizteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        MyLog.getInstance().debug(DownloadingService.class,
				"Finishing download service for:" + downloadingManga.getName());

	}

    private Notification createDownloadNotification() {
        Intent intent = new Intent(this, DownloadingActivity.class);
        intent.putExtra(DetailsActivity.BUNDLE_MANGA, downloadingManga);

        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification noti = new NotificationCompat.Builder(this)
                .setContentTitle("Now downloading " + downloadingManga.getName())
                .setContentText("Click here to see status")
                .setSmallIcon(R.drawable.notification_icon)
                .setContentIntent(pIntent)
                .build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        noti.flags |= Notification.FLAG_FOREGROUND_SERVICE;

        notificationManager.notify(0, noti);

        return noti;

    }

	@Override
	public void onDestroy() {
		super.onDestroy();

		stopForeground(true);
        DownloadHelper.getInstance().restartLooping();
		stopSelf();
	}

}

package com.manLoader.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import android.os.AsyncTask;

public class FileDownloadTask extends AsyncTask<Object, Long, byte[][]> {

	static interface DownloadProgressRunnable extends Runnable {

		public void setProgress(Long... progress);

	}

    private static final int BUFFFER_SIZE = 16*1024; //64K

	private File[] file = null;

	private boolean isErrorManagement = false;
	private boolean isIOError = false;
	private int currentTries = 0;

	private long transferedBytesCount;
	private DownloadProgressRunnable progressUpdate = null;

	public FileDownloadTask(File... file) {
		this.file = file;
	}

	public FileDownloadTask(int maxTries, boolean isErrorManagement, File... file) {
		this.file = file;
		this.currentTries = maxTries;
		this.isErrorManagement = isErrorManagement;
	}

	public FileDownloadTask(int maxTries, boolean isErrorManagement,
                            DownloadProgressRunnable progress, File... file) {
		this.file = file;
		this.currentTries = maxTries;
		this.isErrorManagement = isErrorManagement;
		this.progressUpdate = progress;

	}

	@Override
	protected byte[][] doInBackground(Object... params) {

		for (int i = 0; i < params.length; i++) {
			try {
				URL url = new URL((String)params[i]);
				BufferedInputStream in = new BufferedInputStream(url.openStream());
				OutputStream out = null;

				out = new BufferedOutputStream(new FileOutputStream(file[i]));


				Timer updateProgressTimer = null;
				if (progressUpdate != null) {
					updateProgressTimer = new Timer();
					updateProgressTimer.schedule(new TimerTask() {
						@Override
						public void run() {
							publishProgress(transferedBytesCount);
						}
					}, 0, 500);
				}

                MyLog.getInstance().debug(getClass(),"Starting:"+ url);

                int byteCount;
                byte[] buffer = new byte[BUFFFER_SIZE];
                do{
                    byteCount = in.read(buffer,0,BUFFFER_SIZE);
					out.write(buffer);
					if (progressUpdate != null) {
						transferedBytesCount+=byteCount;
					}
				}while(byteCount > -1);

                MyLog.getInstance().debug(getClass(),"Finishing:"+ url);

				in.close();
				out.close();

				if (progressUpdate != null) {
					transferedBytesCount = 0;
					updateProgressTimer.cancel();
				}


			} catch (IOException e) {
				MyLog.getInstance().debug(FileDownloadTask.class.getSimpleName(),
						"IO Error, tries left:" + currentTries);
				if (isErrorManagement && currentTries > 0) {
					currentTries--;
					i--;
				} else {
					isIOError = true;
					return new byte[][] { new byte[] { Byte.MIN_VALUE },
							e.getClass().getName().getBytes(),
							e.getMessage().getBytes() };

				}
				e.printStackTrace();
			}

        }
        return null;
		//return result.toArray(new byte[result.size()][]);
	}

	@Override
	protected void onProgressUpdate(Long... values) {
		if (progressUpdate != null) {
			progressUpdate.setProgress(values);
			progressUpdate.run();
		}
	}

	public void verifyException(byte[][] sourceBytes) throws IOException {
		if (isIOError) {
			throw new IOException(new String(sourceBytes[2]));
		}
	}

}

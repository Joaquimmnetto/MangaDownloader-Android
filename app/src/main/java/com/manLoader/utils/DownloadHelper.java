package com.manLoader.utils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.os.Environment;

public class DownloadHelper {

    private static final int MAX_SIMULTANEOUS_EXEC = (int) Math.ceil(Runtime.getRuntime().availableProcessors() * 1.5);
    private static Executor sourceExecutor;
    public ThreadQueue queue;
    private static DownloadHelper instance;


    public static DownloadHelper getInstance() {
        if(instance == null) {
            instance = new DownloadHelper();
        }
        return instance;
    }

    private DownloadHelper() {
        queue = new ThreadQueue(MAX_SIMULTANEOUS_EXEC);

    }

    /**
     * Downloads the source code of a page. Doesn't use the ThreadQueue, instead, executes and gets a DownloadTask to simulate a non-paralel execution. (Android doesn't allow downloads on main thread).
     * @param URL - Page url
     * @return Source code of the page
     * @throws IOException
     */
	public static CppString downloadSource(String URL)
			throws IOException {
        try {
            if(sourceExecutor == null){
                sourceExecutor = Executors.newSingleThreadExecutor();
            }
			StringDownloadTask task = new StringDownloadTask();
			task.executeOnExecutor(sourceExecutor,URL);
            CppString result = task.get();
            //throw exceptions if any happened on task
            task.verifyException(result);

            return result;

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

		return null;
	}



    public void downloadToFile(String link, String filename)
			throws IOException {
		downloadToFile(link, null, filename);
	}

	public void downloadToFile(String link, String directory,
			String filename) throws IOException {
            File file = createPageFile(link,directory,filename);
			FileDownloadTask task = new FileDownloadTask(file);

            queue.enqueue(task,link);

    }

    public void startLooping(){
        queue.executeLooping();
    }

    public void stopLooping(){
        queue.stopLooping();
    }

    private File createPageFile(String link,String directory,String filename) throws IOException {

        File dir = null;
        if (directory == null) {
            dir = Environment.getExternalStorageDirectory();

        } else {
            dir = new File(Environment.getExternalStorageDirectory(), directory);

        }
        dir.mkdirs();
        File file = new File(dir, filename);
        if (!file.exists() || file.length() < 10) {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

        }
        return file;
    }

    /**
     *  Stops and runs again ThreadQueue, canceling any threads still in queue. Threads already started will end their execution.
     *  Note that this method sleeps for a while, to give time for the looping thread to finish.
     */
    public void restartLooping() {
        stopLooping();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        startLooping();

    }


    // private long logBitRate(Long btRateCounter, double timeAntMillis) {
	// long presentDayPresentTime/* MUAHAHAHAHAHAHA */= System
	// .currentTimeMillis();
	// if (Math.abs(presentDayPresentTime - timeAntMillis) > 1000) {
	// // Main.statusTransferRate(btRateCounter / 1024);
	// btRateCounter = 0l;
	// }
	// return presentDayPresentTime;
	// }

}

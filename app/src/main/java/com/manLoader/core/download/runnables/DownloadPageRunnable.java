package com.manLoader.core.download.runnables;

import java.io.IOException;
import java.io.InterruptedIOException;

import com.manLoader.pojo.Page;
import com.manLoader.utils.DownloadHelper;
import com.manLoader.utils.MyLog;

public class DownloadPageRunnable implements Runnable {

    private Page page;


    public DownloadPageRunnable(Page page) {
        this.page = page;

    }

    @Override
    public void run() {
        try {
            MyLog.getInstance().debug(DownloadPageRunnable.class,
                    "Queuing donwload, page:" + page.getNumero());
         //   MyLog.getInstance().page(page);

            String result = downloadPageToFile(page);
            page.setExternalFile(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String downloadPageToFile(Page page) throws InterruptedIOException,
            IOException {
        if (page.getImageLink() != null) {
            String chapterNumber = treatNumber(page.getCapitulo().getNumero());
            String pageNumber = treatNumber(page.getNumero());

            String directory = "mangas/"
                    + page.getCapitulo().getManga().getName() + "/"
                    + chapterNumber + "/";
            directory = directory.replace(':', '-').replace('?', ' ')
                    .replace('*', ' ').replace('<', '(').replace('>', ')');
            String filename = pageNumber + ".jpg";
            MyLog.getInstance().debug(DownloadPageRunnable.class,
                    "Downloading to:" + directory + "/" + filename);

            DownloadHelper.getInstance().downloadToFile(page.getImageLink(), directory,
                    filename);

            return filename;


        }
        return null;
    }

    private String treatNumber(int numero) {
        String zeros = "";
        zeros = numero < 9 ? "00" : "0";
        zeros = numero < 99 ? "0" : "";

        return zeros + numero;
    }

}

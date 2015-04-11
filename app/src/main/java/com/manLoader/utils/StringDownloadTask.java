package com.manLoader.utils;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Created by KithLenovo on 02/04/2015.
 */
public class StringDownloadTask extends AsyncTask<String,Void,CppString> {

    private boolean ioError = false;
    private static final int BUFFER_SIZE = 1024;

    private int currentTries = 2;

    protected CppString doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            BufferedInputStream in = new BufferedInputStream(url.openStream());
            CppString out = new CppString();

            int baite;
            do{
                baite = in.read();
                out.append((char)baite);
            }while(baite > -1);

            in.close();


            return out;
        } catch (IOException e) {
            MyLog.getInstance().debug(FileDownloadTask.class.getSimpleName(),
                    "IO Error, tries left:" + currentTries);
            if (currentTries > 0) {
                currentTries--;
            } else {
                ioError = true;
                return new CppString(e.getMessage());
            }
            e.printStackTrace();
        }
        return null;
    }

        public void verifyException(CppString sourceBytes) throws IOException {
            if(ioError) {
                throw new IOException(sourceBytes.toString());
            }

        }
}

package com.manLoader.utils;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * Created by KithLenovo on 02/04/2015.
 */
public class ByteDownloadTask extends AsyncTask<String,Void,byte[]> {

    private boolean ioError;
    private static final int BUFFER_SIZE = 1024 * 2; //2KB;
    private int currentTries = 3;

    @Override
    protected byte[] doInBackground(String... params) {

        for (int i = 0; i < params.length; i++) {
            try {
                URL url = new URL((String)params[i]);
                BufferedInputStream in = new BufferedInputStream(url.openStream());
                ByteArrayOutputStream out = null;

                out = new ByteArrayOutputStream();

                MyLog.getInstance().debug(getClass(),"Starting:"+ url);

                int byteCount;

                byte[] buffer = new byte[BUFFER_SIZE];
                do{
                    byteCount = in.read(buffer,0, BUFFER_SIZE);
                    if(byteCount == buffer.length) {
                        out.write(buffer);
                    }
                    else if(byteCount > 0){
                        out.write(buffer,0,byteCount);
                    }
                }while(byteCount > -1);

                MyLog.getInstance().debug(getClass(),"Finishing:"+ url);

                in.close();
                out.close();

                return out.toByteArray();



            } catch (IOException e) {
                MyLog.getInstance().debug(FileDownloadTask.class.getSimpleName(),
                        "IO Error, tries left:" + currentTries);
                if (currentTries > 0) {
                    currentTries--;
                    i--;
                } else {
                    ioError = true;
                    return e.getMessage().getBytes();

                }
                e.printStackTrace();
            }

        }
        return null;
        //return result.toArray(new byte[result.size()][]);
    }


    public void verifyException(byte[] sourceBytes) throws IOException {
        if (ioError) {
            throw new IOException(new String(sourceBytes));
        }
    }

}

package com.ebabu.ach.Beans;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Sahitya on 2/15/2017.
 */

public class DataHandler {
    private static DataHandler dataHandler = null;
    private File imageFile = null;

    public static DataHandler getInstance() {
        if (dataHandler == null) {
            dataHandler = new DataHandler();
        }
        return dataHandler;
    }


    public File getFile() {
        imageFile = new File(Environment.getExternalStorageDirectory() + File.separator + "temp11.jpeg");

        if (imageFile.exists())
            imageFile.delete();

        try {
            imageFile.createNewFile();
            FileOutputStream fo = new FileOutputStream(imageFile);
            fo.write(byteArray);
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageFile;
    }

    private byte[] byteArray;

    public byte[] getByteArray() {
        return byteArray;
    }

    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }

    public static void reset() {
        dataHandler = null;
    }
}

package com.merann.smamonov.meraimageviewer.model;

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by sergeym on 18.11.2015.
 */
public class ImageItem implements Serializable {
    private String path;
    private String fileName;
    private long fileSize;
    private transient Bitmap bitmap;
    private transient Bitmap icon;

    public String toString() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            oos.close();
        } catch (Throwable throwable) {
            System.err.println("Unable to convert ImageItem to String");
        }
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }


    public static ImageItem fromString(String input) {
        ImageItem result = null;
        byte[] data = Base64.decode(input, Base64.DEFAULT);
        try {
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
            result = (ImageItem) ois.readObject();
            ois.close();
        }
        catch (Throwable throwable)
        {
            System.err.println("Unable to convert String to ImageItem");
        }
        return result;
    }

    public ImageItem(String path, String fileName) {
        this.path = path;
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }
}

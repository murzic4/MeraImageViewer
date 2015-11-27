package com.merann.smamonov.meraimageviewer.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.merann.smamonov.meraimageviewer.model.ImageItem;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

interface ImageLoaderListener {
    void onLoadComplete();
    void imageLoadDelegate();
}

class ImageLoader extends AsyncTask<Void, Void, Void> {
    ImageLoaderListener imageLoadListener;

    public ImageLoader(ImageLoaderListener imageLoadListener) {
        this.imageLoadListener = imageLoadListener;
    }

    @Override
    protected Void doInBackground(Void... params) {
        imageLoadListener.imageLoadDelegate();
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        imageLoadListener.onLoadComplete();
    }
}

public class ImageLoadService {

    ArrayList<ImageItem> items;
    int last_loaded_item = 0;
    List<ImageLoadServiceListener> listeners = new LinkedList();
    boolean do_icons_loading = true;

    public ImageLoadService() {
        items = new ArrayList();
        File picture_folder = new File("/mnt/extSdCard/DCIM/Camera");
        if (picture_folder != null) {
            File[] files = picture_folder.listFiles();
            for (File file : files) {
                items.add(new ImageItem(file.getAbsolutePath(), file.getName()));
            }
        }
    }

    public List<ImageItem> getFileList() {
        return items;
    }

    public void loadFullImage(ImageItem imageItem) {
        final ImageItem image_to_load = imageItem;
        ImageLoader imageLoader = new ImageLoader(new ImageLoaderListener() {
            public void onLoadComplete() {
                loadImageComplete(image_to_load);
            }

            public void imageLoadDelegate() {
                loadImage(image_to_load);
            }
        });
        imageLoader.execute();
    }

    private void loadIconImages() {
        if (do_icons_loading == true) {
            while (last_loaded_item < items.size()) {
                final ImageItem image_to_load = items.get(last_loaded_item);
                ImageLoader imageLoader = new ImageLoader(new ImageLoaderListener() {

                    public void onLoadComplete() {
                        loadIconComplete(image_to_load);
                    }

                    public void imageLoadDelegate() {
                        loadIcon(image_to_load);
                    }
                });
                imageLoader.execute();
                break;
            }
        }
    }

    public long getImageSize(ImageItem imageItem) {
        if (imageItem.getFileSize() == 0) {
            File file = new File(imageItem.getPath());
            imageItem.setFileSize(file.length());
        }
        return imageItem.getFileSize();
    }

    private void loadIconComplete(ImageItem imageItem) {
        last_loaded_item++;
        for (ImageLoadServiceListener listener : listeners) {
            listener.onIconLoadComplete(imageItem);
        }
        loadIconImages();
    }

    private void loadImageComplete(ImageItem imageItem) {
        for (ImageLoadServiceListener listener : listeners) {
            listener.onImageLoadComplete(imageItem);
        }
    }

    public void addListener(ImageLoadServiceListener listener) {
        listeners.add(listener);

        if (listeners.size() != 0) {
            startIconLoading(true);
        }
    }

    public void removeListener(ImageLoadServiceListener _listener) {
        for (ImageLoadServiceListener listener : listeners) {

            listeners.remove(_listener);
            break;
        }

        if (listeners.size() == 0) {
            startIconLoading(false);
        }
    }

    public void startIconLoading(boolean loading) {
        this.do_icons_loading = loading;

        if (this.do_icons_loading == true) {
            loadIconImages();
        }
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int width, int height) {
        final int original_height = options.outHeight;
        final int original_width = options.outWidth;

        int inSampleSize = 1;

        if (original_height > height || original_width > width) {

            final int halfHeight = original_height / 2;
            final int halfWidth = original_width / 2;

            while ((halfHeight / inSampleSize) > height
                    && (halfWidth / inSampleSize) > width) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private void loadIcon(ImageItem item_to_load) {
        if (item_to_load != null) {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(item_to_load.getPath(), options);

            options.inSampleSize = calculateInSampleSize(options, 60, 60);
            options.inJustDecodeBounds = false;

            try {
                Bitmap bitmap = BitmapFactory.decodeFile(item_to_load.getPath(), options);
                item_to_load.setIcon(bitmap);

            } catch (Throwable throwable) {
                System.err.println("Unable to load file:" + item_to_load.getFileName());
            }
        }
    }

    private void loadImage(ImageItem imageItem) {
        while (true) {
            try {
                imageItem.setBitmap(BitmapFactory.decodeFile(imageItem.getPath()));
                System.err.println("------>full file " + imageItem.getPath() + " is loaded, size " + imageItem.getBitmap().getByteCount());
                break;
            } catch (OutOfMemoryError error) {
                System.err.println("Unable to load image " + imageItem.getPath() + ", try again...");
            }
        }
    }

}

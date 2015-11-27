package com.merann.smamonov.meraimageviewer.application;

import android.app.Application;

import com.merann.smamonov.meraimageviewer.controller.ImageLoadService;

/**
 * Created by samam_000 on 19.11.2015.
 */
public class App extends Application {

    ImageLoadService imageLoadService = new ImageLoadService();

    public ImageLoadService getImageLoadService() {
        return imageLoadService;
    }
}

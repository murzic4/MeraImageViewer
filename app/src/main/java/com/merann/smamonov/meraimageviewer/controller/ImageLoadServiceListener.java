package com.merann.smamonov.meraimageviewer.controller;

import com.merann.smamonov.meraimageviewer.model.ImageItem;

/**
 * Created by samam_000 on 18.11.2015.
 */
public interface ImageLoadServiceListener {
    void onImageLoadComplete(ImageItem imageItem);
    void onIconLoadComplete(ImageItem imageItem);
}

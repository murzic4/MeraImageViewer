package com.merann.smamonov.meraimageviewer.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.merann.smamonov.meraimageviewer.application.App;
import com.merann.smamonov.meraimageviewer.model.ImageItem;
import com.merann.smamonov.meraimageviewer.R;
import com.merann.smamonov.meraimageviewer.controller.ImageLoadService;
import com.merann.smamonov.meraimageviewer.controller.ImageLoadServiceListener;

public class ImageScreenActivity extends AppCompatActivity implements ImageLoadServiceListener {

    ImageLoadService imageLoadService;
    ImageItem imageItem;

    private ImageItem readImageItemFromParameters(Bundle savedInstanceState) {

        ImageItem result = null;
        try {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String imageItemString = extras.getString(getResources().getString(R.string.image_item_parameter));
                result = ImageItem.fromString(imageItemString);
            }
        } catch (Throwable throwable) {
            System.err.println("Unable to serialize image item from parameters");
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_screen);

        imageLoadService = ((App) getApplication()).getImageLoadService();
        imageItem = readImageItemFromParameters(savedInstanceState);

        final FloatingActionButton send_by_email_button = (FloatingActionButton) findViewById(R.id.send_by_email_button);
        send_by_email_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Send by email", Snackbar.LENGTH_LONG)
                        .setAction("Send", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                sendImageByEmail();
                            }
                        }).show();
            }
        });

        FloatingActionButton get_image_info_button = (FloatingActionButton) findViewById(R.id.get_image_info_button);
        get_image_info_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long fileSize = imageLoadService.getImageSize(imageItem);
                Snackbar.make(view, imageItem.getFileName() + " file size: " + imageItem.getFileSize() + " bytes", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onImageLoadComplete(ImageItem imageItem) {
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imageView.setVisibility(View.VISIBLE);
        imageView.setImageBitmap(imageItem.getBitmap());
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onIconLoadComplete(ImageItem imageItem) {
        System.err.println("This should never happen, image:" + imageItem.getPath());
    }

    @Override
    protected void onResume() {
        super.onResume();
        imageLoadService.addListener(this);
        imageLoadService.loadFullImage(imageItem);
    }

    @Override
    protected void onPause() {
        super.onPause();
        imageLoadService.removeListener(this);
    }

    private void sendImageByEmail() {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, imageItem.getFileName());
        emailIntent.setType("application/image");
        Uri uri = Uri.parse("file://" + imageItem.getPath());
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(emailIntent);
    }
}

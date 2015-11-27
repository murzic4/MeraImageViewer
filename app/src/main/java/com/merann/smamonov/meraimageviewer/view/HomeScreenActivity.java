package com.merann.smamonov.meraimageviewer.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.merann.smamonov.meraimageviewer.application.App;
import com.merann.smamonov.meraimageviewer.model.ImageItem;
import com.merann.smamonov.meraimageviewer.controller.ImageLoadService;
import com.merann.smamonov.meraimageviewer.controller.ImageLoadServiceListener;
import com.merann.smamonov.meraimageviewer.R;

import java.util.List;

public class HomeScreenActivity extends AppCompatActivity implements ImageLoadServiceListener {

    ImageLoadService imageLoadService;
    ImageListAdapter imageListAdapter;
    ScaleGestureDetector scaleDetector;
    float scaleFactor = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.err.println("------------>onCreate()");
        super.onCreate(null);
        setContentView(R.layout.activity_home_screen_1);

        App app = (App) getApplication();
        imageLoadService = app.getImageLoadService();

        List<ImageItem> images = imageLoadService.getFileList();
        final AbsListView listView = (AbsListView) findViewById(R.id.ImageList);
        imageListAdapter = new ImageListAdapter(this, getFragmentManager(), images);
        listView.setAdapter(imageListAdapter);

        final HomeScreenActivity intend_context = this;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageItem selectedImage = (ImageItem) (listView.getItemAtPosition(position));
                Intent open_image_activity_intent = new Intent(intend_context, ImageScreenActivity.class);
                open_image_activity_intent.putExtra(getResources().getString(R.string.image_item_parameter), selectedImage.toString());
                startActivity(open_image_activity_intent);
            }
        });

        imageListAdapter.notifyDataSetChanged();

        scaleDetector = new ScaleGestureDetector(getBaseContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener()
        {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                scaleFactor *= detector.getScaleFactor();
                scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));

                System.err.println("--->scaleFactor:" + scaleFactor);

                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        System.err.println("------------>onStart()");
        super.onStart();
    }

    @Override
    protected void onResume() {
        System.err.println("------------>onResume()");
        super.onResume();
        imageLoadService.addListener(this);
    }

    @Override
    protected void onPause() {
        System.err.println("------------>onPause()");
        super.onPause();
        imageLoadService.removeListener(this);
    }

    @Override
    public void onIconLoadComplete(ImageItem imageItem) {
        System.err.println("------------>onIconLoadComplete()" + imageItem.getPath());
        if (imageListAdapter != null) {
            imageListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onImageLoadComplete(ImageItem imageItem) {
        System.err.println("This should never happen, image:" + imageItem.getPath());
    }
}

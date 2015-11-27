package com.merann.smamonov.meraimageviewer.view;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.merann.smamonov.meraimageviewer.model.ImageItem;
import com.merann.smamonov.meraimageviewer.R;

import java.util.List;

/**
 * Created by sergeym on 18.11.2015.
 */
public class ImageListAdapter extends BaseAdapter {
    private final Context context;
    private List<ImageItem> images;
    FragmentManager fragmentManager;

    static class ViewHolderItem {
        TextView textItem;
        ImageView imageView;
        ProgressBar progressBar;
        ImageItem image;
    }

    public ImageListAdapter(Context _context,
                            FragmentManager _fragmentManager,
                            List<ImageItem> _images) {
        this.images = _images;
        this.context = _context;
        this.fragmentManager = _fragmentManager;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolderItem viewHolderItem;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            //convertView = inflater.inflate(R.layout.image_list_item_layout, parent, false);
            convertView = inflater.inflate(R.layout.image_grid_item_layout, parent, false);
            viewHolderItem = new ViewHolderItem();
            viewHolderItem.textItem = (TextView) convertView.findViewById(R.id.image_text);
            viewHolderItem.imageView = (ImageView) convertView.findViewById(R.id.image);
            viewHolderItem.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
            convertView.setTag(viewHolderItem);
        } else {
            viewHolderItem = (ViewHolderItem) convertView.getTag();
        }

        viewHolderItem.image = (ImageItem) getItem(position);
        viewHolderItem.textItem.setText(((ImageItem) getItem(position)).getFileName());

        if (((ImageItem) getItem(position)).getIcon() != null) {
            viewHolderItem.imageView.setImageBitmap(((ImageItem) getItem(position)).getIcon());
            viewHolderItem.progressBar.setVisibility(View.INVISIBLE);
            viewHolderItem.imageView.setVisibility(View.VISIBLE);
        } else {
            viewHolderItem.progressBar.setVisibility(View.VISIBLE);
            viewHolderItem.imageView.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }
}

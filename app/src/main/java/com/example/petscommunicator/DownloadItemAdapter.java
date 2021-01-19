package com.example.petscommunicator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DownloadItemAdapter extends ArrayAdapter<DownloadItem> {

    private Context mContext;

    public DownloadItemAdapter(@NonNull Context context, int resource, @NonNull ArrayList<DownloadItem> objects) {
        super(context, resource, objects);
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.download_item_layout, parent, false);
        }
        TextView title  = convertView.findViewById(R.id.title_id);
        ImageView img = convertView.findViewById(R.id.icon_id);
        DownloadItem item = getItem(position);
        assert item != null;
        title.setText(item.getTitle());
        img.setImageBitmap(item.getIcon());
        return convertView;
    }
}

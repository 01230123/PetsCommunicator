package com.example.petscommunicator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.petscommunicator.server.RetrofitInterface;
import com.example.petscommunicator.server.DogSoundList;
import com.google.android.material.shape.InterpolateOnScrollPositionChangeHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private Context context;
    private List<String> soundList;

    public static PlaceholderFragment newInstance(Context context, int index, List<String> soundList) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        fragment.context = context;
        fragment.soundList = soundList;
        return fragment;
    }

    private int index = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        ListView lv = root.findViewById(R.id.lv_id);
        ArrayList<DownloadItem> list = createDownloadItem();
        DownloadItemAdapter downloadItemAdapter = new DownloadItemAdapter(this.context, 0, list);
        lv.setAdapter(downloadItemAdapter);
        return root;
    }

    private ArrayList<DownloadItem> createDownloadItem() {
        if (index == 4)
            Log.d("@@@@", "Size: " + soundList.size());
        ArrayList<DownloadItem> list = new ArrayList<>();
        for (String sound : soundList)
        {
            DownloadItem item = new DownloadItem(sound, context);
            list.add(item);
        }
        return list;
    }



}
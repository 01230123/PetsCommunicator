package com.example.petscommunicator;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.petscommunicator.server.RetrofitInterface;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }

    public void download(String themeName, String emotionName){
        String BASE_URL = "http://10.0.2.2:3000";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        String audioExtension = ".mp3";
        String url = themeName + "/" + emotionName + audioExtension;
        final String fileName = "/" + emotionName + audioExtension;

        retrofitInterface.downlload(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File file = new File(path, fileName);
                    boolean a = file.createNewFile();
                    Files.asByteSink(file).write(response.body().bytes());
                    String file_size = Formatter.formatShortFileSize(getContext(),file.length());
                    Log.d("@@@@", file_size);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("@@@@", "Cannot down load file");
                    handleError();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("@@@@", "Disconnect from server");
                handleError();
            }
        });
    }

    private void handleError()
    {
        final Toast tError = Toast.makeText(mContext, "Disconnect from server", Toast.LENGTH_SHORT);
        tError.show();
    }
}

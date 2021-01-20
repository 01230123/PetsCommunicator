package com.example.petscommunicator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
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

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.download_item_layout, parent, false);
        }
        TextView title  = convertView.findViewById(R.id.title_id);
        ImageView img = convertView.findViewById(R.id.icon_id);
        DownloadItem item = getItem(position);
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(path, item.getTitle());
        boolean downloadFlag = !file.exists();
        img.setImageBitmap(item.getIcon());
        if (downloadFlag)
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    download(item.getPath(), title);
                }
            });
        else
            item.setTitle(item.getTitle() + " - Downloaded");
        title.setText(item.getTitle());
        return convertView;
    }

    public void download(String url, TextView title){
        String BASE_URL = "http://petcommunicator.herokuapp.com";
        Log.d("@@@@", "Connect to server " + BASE_URL + "/" + url + "...");
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        String themeName = url.substring(0, url.indexOf('/'));
        String fileName = url.substring(url.indexOf('/') + 1);

        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(path, fileName);
        if (file.exists()) return;

        Log.d("@@@@", "Start download file from " + url);
        Log.d("@@@@", "Theme name: " + themeName + ", filename: " + fileName);
        retrofitInterface.downlload(url).enqueue(new Callback<ResponseBody>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
//                    ContextWrapper contextWrapper = new ContextWrapper(mContext.getApplicationContext());
//                    File path = contextWrapper.getDir(
//                            R.string.internalPath + "/" + themeName,
//                            Context.MODE_PRIVATE);
                    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File file = new File(path, fileName);
//                    boolean a = file.createNewFile();
                    Files.asByteSink(file).write(response.body().bytes());
                    String file_size = Formatter.formatShortFileSize(getContext(),file.length());
                    Log.d("@@@@", "Download file |" + fileName + "| successfully, bytes: " + file_size + ", save at " + path);
                    title.setText(title.getText() + " - Downloaded");
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

    private void handleSuccess()
    {
        final Toast tSuccess = Toast.makeText(mContext, "Download successfully", Toast.LENGTH_SHORT);
        tSuccess.show();
    }

}

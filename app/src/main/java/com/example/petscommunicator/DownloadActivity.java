package com.example.petscommunicator;

import android.os.Bundle;

import com.example.petscommunicator.server.DogSoundList;
import com.example.petscommunicator.server.RetrofitInterface;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DownloadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("@@@@", "Intent success! Start to connect to server");
        getSoundNames();
    }

    public void setTabLayout(List<List<String>> soundList)
    {
        setContentView(R.layout.download_activity_layout);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager(), soundList);
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    public void getSoundNames()
    {
        String BASE_URL = "http://10.0.2.2:3000";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        retrofitInterface.getSounds().enqueue(new Callback<DogSoundList>() {
            @Override
            public void onResponse(Call<DogSoundList> call, Response<DogSoundList> response) {
                // This is a callback method - meaning it will execute after the server response
                // If you want to get the result (name list), you should put your code in this function
                // If you put your code outside of this function, your code may run before the server response
                // Thus creating a NullPointerException
                DogSoundList re = response.body();
                List<List<String>> soundList = re.getDogSounds();
                Log.d("@@@@", "Successfully get dog sound");
                Log.d("@@@@", "List size: " + soundList.size());
//                for (int i = 0; i < soundList.size(); i++)
//                {
//                    Log.d("@@@@", "List " + i + " size: " + soundList.get(i).size());
//                    Log.d("@@@@", "Content: " + soundList.get(i));
//                }
                setTabLayout(soundList);
            }

            @Override
            public void onFailure(Call<DogSoundList> call, Throwable t) {
                Log.d("@@@@", "Fail to get dog sound");
                handleError();
            }
        });
    };

    private void handleError()
    {
        final Toast tError = Toast.makeText(this,"Server is offline", Toast.LENGTH_SHORT);
        tError.show();
        finish();
    }

}
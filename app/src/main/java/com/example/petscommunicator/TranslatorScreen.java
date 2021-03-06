package com.example.petscommunicator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.petscommunicator.server.RetrofitInterface;
import com.example.petscommunicator.server.UploadAudio;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import org.apache.commons.io.IOUtils;

public class TranslatorScreen extends MySprite{
    private MainScreen mainScreen;
    public boolean rec=false;
    private MediaRecorder mediaRecorder = null;
    private String[] names;
    private TextView tw;
    private MySprite recButton;

    private View myLayout;
    private File outputDir;
    private File outputFile;
    private Handler handler = new Handler();
    private GraphView graph;

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = "http://petcommunicator.herokuapp.com";
    private String translation = "";

    private List<List<String>> soundList;
    private boolean translating = false;

    @SuppressLint("ClickableViewAccessibility")
    public TranslatorScreen(Context context, MainScreen mainScreen, float top, float left, int width, int height) {
        super(context, top, left, width, height);
        this.mainScreen = mainScreen;

        myLayout = LayoutInflater.from(getContext()).inflate(R.layout.translator_layout, null);
        names = getContext().getResources().getStringArray(R.array.sentence);
        tw = myLayout.findViewById(R.id.textView);
        recButton = new MySprite(
                getContext(),
                (float)(getHeight() * 0.47),
                (float)(getWidth() * 0.46),
                (int)(getWidth() * 0.5),
                (int)(getHeight() * 0.29)
        );
        recButton.addBmp(new int[]{
                R.drawable.microphone,
                R.drawable.rec
        });
        createGraph();

        retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

    }

    private double getGaussianCurvePoint(double mean, double std, double x)
    {
        double stdDeviation = std;
        double variance = std*std;
        return Math.exp(-(((x - mean) * (x - mean)) / ((2 * variance))))* (1 / (stdDeviation * Math.sqrt(2 * Math.PI)));
    }

    //@RequiresApi(api = Build.VERSION_CODES.O)
    private void startRecording() {
        //final Toast tStart = Toast.makeText(getContext(),"Start Recording ", Toast.LENGTH_SHORT);
        //tStart.show();
        tw.setText("");
        outputDir = getContext().getCacheDir();
        // Required empty public constructor
        try {
            outputFile = File.createTempFile("recorded", ".3gp", outputDir);
        } catch (IOException e)
        {
            //Do something but I don't really know
            Log.d("@@@F", "TranslatorFragment: Fail to create catch file");
        }
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(outputFile.getAbsolutePath());
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IllegalStateException ise) {
            // make something ...
        } catch (IOException ioe) {
            // make something
        }
        handler.post(updateGraph);
    }

    private void stopRecording() {
        //final Toast tStop = Toast.makeText(getContext(),"Stop Recording", Toast.LENGTH_SHORT);
        //tStop.show();
        tw.setText("Translating...");
        translating = true;
        dogVoiceToTextTranslation(outputFile);

        try {
            mediaRecorder.stop();
            mediaRecorder.release();
        } catch(RuntimeException stopException) {
            // handle cleanup here
        }
        handler.removeCallbacks(updateGraph);
    }

    private void createGraph() {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        generateDataSeries(100);

        graph = myLayout.findViewById(R.id.graph);

        graph.getViewport().setMinX(-10);
        graph.getViewport().setMaxX(10);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMaxY(0.5);
        graph.getViewport().setMinY(0);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graph.setBackgroundColor(Color.TRANSPARENT);
        graph.addSeries(series);
    }

    private LineGraphSeries generateDataSeries(double std) {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        double x = -10, y = 0;

        double mean = 0;
        int numberOfData = 400;
        for (int i = 0; i < numberOfData; i++)
        {
            x = x + 0.1;
            y = getGaussianCurvePoint(mean, std, x);
            series.appendData(new DataPoint(x, y), true, numberOfData);
        }
        return series;
    }

    void updateGraph(double std)
    {
        graph.removeAllSeries();
        graph.addSeries(generateDataSeries(std));
    }


    String dogVoiceToTextTranslation(File audioFile)
    {
        //Upload the file to the server
        String fileString = "test";
        translation = "here1";

        HashMap<String, String> map = new HashMap<>();
        map.put("name", "test");

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), audioFile);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("audio", audioFile.getName(), requestFile);

        Call<UploadAudio> call = retrofitInterface.executeUpload(body);

        call.enqueue(new Callback<UploadAudio>()
        {

            @Override
            public void onResponse(Call<UploadAudio> call, Response<UploadAudio> response) {
                if (response.code() == 200)
                {
                    Log.d("@@@@", "Success");
                    UploadAudio re = response.body();
                    String text = re.getName();
                    tw.setText(text);
                    translating = false;
                    mainScreen.invalidate();
                }
            }
            @Override
            public void onFailure(Call<UploadAudio> call, Throwable t) {
                Log.d("@@@@", "Failed");
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                tw.setText("Error");
                translating = false;
                mainScreen.invalidate();
            }
        });



        int randomIndex = new Random().nextInt(names.length);
        String randomName = names[randomIndex];
        return translation;
    }

    private Runnable updateGraph = new Runnable() {
        @Override
        public void run() {
            if (rec)
            {
                int x = mediaRecorder.getMaxAmplitude();
                updateGraph(getStdForGraph(x));
                handler.postDelayed(this, 40);
                mainScreen.postInvalidate();
            }
        }
    };

    private double getStdForGraph(int amp)
    {
//        Log.d("@@@@", "getStdForGraph: " + amp);
        if (amp == 0)
            return 100;

        double res = 0.8;
        double max = 32767;
        double realValue = (amp/max)*10;

        res = 5 - realValue;


        if (res < 0.8)
            return  0.8;
        return res;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        recButton.draw(canvas);

        @SuppressLint("Range")
        int widthSpec = View.MeasureSpec.makeMeasureSpec (ViewGroup.LayoutParams.WRAP_CONTENT, View.MeasureSpec.UNSPECIFIED);
        int heightSpec = View.MeasureSpec.makeMeasureSpec (400, View.MeasureSpec.UNSPECIFIED);
        tw.measure(widthSpec, heightSpec);
        tw.layout(0, 0, tw.getMeasuredWidth(), tw.getMeasuredHeight());
        canvas.save();
        canvas.translate((float)(getWidth() - tw.getMeasuredWidth()) / 2, 300);
        tw.draw(canvas);
        canvas.restore();

        if (rec)
        {
            graph.measure(widthSpec, heightSpec);
            graph.layout(0, 0, this.getWidth(), 900);
            canvas.save();
            canvas.translate(0, -400);
            graph.draw(canvas);
            canvas.restore();
        }
    }

    //@RequiresApi(api = Build.VERSION_CODES.O)
    public void handleClick(float x, float y)
    {
        if (recButton.isSelected(x, y))
            doRecord();
    }

    //@RequiresApi(api = Build.VERSION_CODES.O)
    public void doRecord() {
        if (translating) return;
        if (!rec)
        {
            getRecordPermission();
            getWritePermission();
            getInternetPermission();
            if (checkWritePermission() && checkRecordPermission() && checkInternetPermission())
            {
                rec = true;
                startRecording();
            }
            else
                return;
        }
        else
        {
            rec = false;
            stopRecording();
        }
        recButton.update();
        mainScreen.invalidate();
    }

    private boolean checkRecordPermission()
    {
        int permission = ActivityCompat.checkSelfPermission(
                getContext(),
                Manifest.permission.RECORD_AUDIO
        );
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    private void getRecordPermission()
    {
        // Check for permissions
        if (!checkRecordPermission()){
            ActivityCompat.requestPermissions(
                    (Activity)getContext(),
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    0
            );
        }
    }

    private boolean checkInternetPermission()
    {
        int permission = ActivityCompat.checkSelfPermission(
                getContext(),
                Manifest.permission.INTERNET
        );
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    private void getInternetPermission()
    {
        // Check for permissions
        if (!checkRecordPermission()){
            ActivityCompat.requestPermissions(
                    (Activity)getContext(),
                    new String[]{Manifest.permission.INTERNET},
                    0
            );
        }
    }

    private boolean checkWritePermission()
    {
        int permission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permission == PackageManager.PERMISSION_GRANTED;
    }

    private void getWritePermission() {
        // Check for permissions
        if (!checkWritePermission()) {
            String[] PERMISSIONS_STORAGE = {
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            int REQUEST_EXTERNAL_STORAGE = 1;

            ActivityCompat.requestPermissions(
                    (Activity)getContext(),
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}
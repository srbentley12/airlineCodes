package com.example.steve.flashcards2;

import android.content.res.AssetManager;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    List<String> lines = new ArrayList<String>();
    private String[] cities;
    private String[] codes;
    private int[] index;
    private int currentIndex = 0;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            readLines();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cities = new String[lines.size()/2];
        codes = new String[lines.size()/2];
        index = new int[lines.size()/2];
        index[0] = 0;
        cities[0] = lines.get(0);
        codes[0] = lines.get(1);
        index[1] = 1;
        cities[1] = lines.get(2);
        codes[1] = lines.get(3);
        for(int i = 2; i < cities.length; i++){
            index[i] = i;
            cities[i] = lines.get(2*i);
            codes[i] = lines.get((2*i) + 1);
        }

        tts = new TextToSpeech(this, this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shuffle();
        Button buttonClick = (Button) findViewById(R.id.nextButton);
        Button buttonShuffleClick = (Button) findViewById(R.id.reshuffleButton);
        buttonClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView city = (TextView) findViewById(R.id.cityTextView);
                TextView code = (TextView) findViewById(R.id.codeTextView);
                if(code.getText() == ""){
                    code.setText(codes[index[currentIndex]]);
                    speakOut(code);
                }
                else if(currentIndex == index.length - 1) {
                    shuffle();
                }
                else {
                    currentIndex++;
                    city.setText(cities[index[currentIndex]]);
                    code.setText("");
                    speakOut(city);
                }
            }
        });

        buttonShuffleClick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                shuffle();
            }
        });
    }

    @Override
    public void onInit(int status) {
        tts.setLanguage(Locale.US);
        tts.setSpeechRate((float) 0.8);
        tts.setPitch((float) 0.8);
        }

        public void speakOut(TextView text) {
            CharSequence ttsText = text.getText();
            tts.speak(ttsText, TextToSpeech.QUEUE_FLUSH, null, "id1");
        }


    public void readLines() throws IOException {
        AssetManager assetManager = getAssets();
        InputStream is = assetManager.open("cities.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }
        bufferedReader.close();
    }

    public void shuffle(){
        currentIndex = 0;
        shuffleArray(index);
        TextView city = (TextView) findViewById(R.id.cityTextView);
        city.setText(cities[index[currentIndex]]);
        TextView code = (TextView) findViewById(R.id.codeTextView);
        code.setText("");
        speakOut(city);
    }

    public static void shuffleArray(int[] arr) {
        int n = arr.length;
        Random random = new Random();
        random.nextInt();
        for (int i = 0; i < n; i++) {
            int change = i + random.nextInt(n - i);
            swap(arr, i, change);
        }
    }

    private static void swap(int[] arr, int i, int change) {
        int helper = arr[i];
        arr[i] = arr[change];
        arr[change] = helper;
    }
}

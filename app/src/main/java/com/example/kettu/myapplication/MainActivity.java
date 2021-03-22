package com.example.kettu.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.widget.Spinner;

import android.text.TextWatcher;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;




public class MainActivity extends Activity {
    Context context = null;
    Spinner theaterSpinner;
    Spinner spinnerDateStart;
    Spinner spinnerDateStop;
    ListView movieListView;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    TheaterJanitor finnKino = new TheaterJanitor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        theaterSpinner = findViewById(R.id.pickUpTheater);
        spinnerDateStart = findViewById(R.id.spinnerDateStart);
        spinnerDateStop = findViewById(R.id.spinnerDateStop);
        movieListView = findViewById(R.id.movieListView);

        // Some weird stuff
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        System.out.println(context.getFilesDir());
        readXML();
    }


    public void readXML(){
        System.out.println("Read XML");

        ArrayList stringOfTheaters;
        stringOfTheaters = finnKino.getTheaterStrings();

        ArrayList dateList;
        dateList = finnKino.getDateStrings();

        // Theaters
        ArrayAdapter adapter = new ArrayAdapter(this,
                                                android.R.layout.simple_spinner_item,
                                                stringOfTheaters);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        theaterSpinner.setAdapter(adapter);

        // Start & stop dates
        ArrayAdapter adapter2 = new ArrayAdapter(this,
                                                android.R.layout.simple_spinner_item,
                                                dateList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDateStart.setAdapter(adapter2);
        spinnerDateStop.setAdapter(adapter2);
    }

    public void searchMovies(View v) throws ParseException {
        movieListView.setEmptyView(movieListView);

        Date d1 = format.parse("2021-03-24");
        Date d2 = format.parse("2021-03-27");
        int chosen_one = theaterSpinner.getSelectedItemPosition();
        System.out.println("chosen id = " + chosen_one);

        ArrayAdapter adapter3 = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item,
                finnKino.getMovies(d1, d2, chosen_one));

        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        movieListView.setAdapter(adapter3);
    }
}

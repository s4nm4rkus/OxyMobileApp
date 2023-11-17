package com.capstone.oxy;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class ReportsFragment extends Fragment {

    private LineChart linechart_report;
    private Button datePickerButton, dayBtn, weekBtn, monthBtn;
    private TextView dateTextView;
    private Calendar calendar;

    public ReportsFragment() {
        // Required empty public constructor
    }

    public static ReportsFragment newInstance() {
        return new ReportsFragment();
    }

    @SuppressLint({"MissingInflatedId", "UseCompatLoadingForDrawables"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        dayBtn = view.findViewById(R.id.dayBtn);
        weekBtn = view.findViewById(R.id.weekBtn);
        monthBtn = view.findViewById(R.id.monthBtn);
        linechart_report = view.findViewById(R.id.line_chart_report);
        datePickerButton = view.findViewById(R.id.datePickerButton);
        dateTextView = view.findViewById(R.id.dateTextView);
        calendar = Calendar.getInstance();


        dayBtn.setBackground(getResources().getDrawable(R.drawable.category_reportbtn_disabled));
        dayBtn.setTextColor(getResources().getColor(R.color.white));
        dayBtn.setEnabled(false);
        weekBtn.setEnabled(true);
        monthBtn.setEnabled(true);

        // Firestore data retrieval for the current date



        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current date
                Calendar currentDate = Calendar.getInstance();
                int year = currentDate.get(Calendar.YEAR);
                int month = currentDate.get(Calendar.MONTH);
                int day = currentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                calendar.set(year, month, dayOfMonth);
                                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                                dateTextView.setText("Selected Date: " + selectedDate);

                                // Call a method to update the chart based on the selected date
                                setupChart(calendar.getTime());
                            }
                        },
                        year,
                        month,
                        day
                );
                datePickerDialog.show();
            }
        });

        dayBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                dayBtn.setEnabled(false);
                weekBtn.setEnabled(true);
                monthBtn.setEnabled(true);
                dayBtn.setBackground(getResources().getDrawable(R.drawable.category_reportbtn_disabled));
                dayBtn.setTextColor(getResources().getColor(R.color.white));
                weekBtn.setBackground(getResources().getDrawable(R.drawable.category_reports_btn));
                weekBtn.setTextColor(getResources().getColor(R.color.tealmain));
                monthBtn.setBackground(getResources().getDrawable(R.drawable.category_reports_btn));
                monthBtn.setTextColor(getResources().getColor(R.color.tealmain));
                setupChart(calendar.getTime());


            }
        });

        weekBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                dayBtn.setEnabled(true);
                weekBtn.setEnabled(false);
                monthBtn.setEnabled(true);
                weekBtn.setBackground(getResources().getDrawable(R.drawable.category_reportbtn_disabled));
                weekBtn.setTextColor(getResources().getColor(R.color.white));
                monthBtn.setBackground(getResources().getDrawable(R.drawable.category_reports_btn));
                monthBtn.setTextColor(getResources().getColor(R.color.tealmain));
                dayBtn.setTextColor(getResources().getColor(R.color.tealmain));
                dayBtn.setBackground(getResources().getDrawable(R.drawable.category_reports_btn));

            }
        });

        monthBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {
                dayBtn.setEnabled(true);
                weekBtn.setEnabled(true);
                monthBtn.setEnabled(false);
                monthBtn.setBackground(getResources().getDrawable(R.drawable.category_reportbtn_disabled));
                monthBtn.setTextColor(getResources().getColor(R.color.white));
                weekBtn.setTextColor(getResources().getColor(R.color.tealmain));
                dayBtn.setTextColor(getResources().getColor(R.color.tealmain));
                weekBtn.setBackground(getResources().getDrawable(R.drawable.category_reports_btn));
                dayBtn.setBackground(getResources().getDrawable(R.drawable.category_reports_btn));

            }
        });

        return view;
    }

    // Set up the chart based on the selected date
    private void setupChart(Date selectedDate) {
        // Initialize an empty list for X-axis labels
        List<String> xValues = new ArrayList<>();

        Description description = new Description();
        description.setText("");
        linechart_report.setDescription(description);

        YAxis rightYAxis = linechart_report.getAxisRight();
        rightYAxis.setDrawLabels(false);

        XAxis bottomXAxis = linechart_report.getXAxis();
        bottomXAxis.setAxisLineWidth(2.5f);
        bottomXAxis.setAxisLineColor(getResources().getColor(R.color.tealmain));
        YAxis leftYAxis = linechart_report.getAxisLeft();
        leftYAxis.setAxisLineWidth(2.5f);
        leftYAxis.setAxisLineColor(getResources().getColor(R.color.tealmain));
        YAxis yAxis = linechart_report.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(500f);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Date endDate = new Date(selectedDate.getTime() + 24 * 60 * 60 * 1000);
        db.collection("sensorData")
                .whereEqualTo("room_no", "room_1")
                .whereGreaterThanOrEqualTo("timestamp", selectedDate)
                .whereLessThan("timestamp", endDate)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Entry> coEntries = new ArrayList<>();
                        List<Entry> vocEntries = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {

                            Double coValue = document.getDouble("CO");
                            Double vocValue = document.getDouble("TVOC");


                            Timestamp timestamp = document.getTimestamp("timestamp");

                            if (coValue != null && vocValue != null && timestamp != null) {

                                SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
                                String timeLabel = sdf.format(timestamp.toDate());


                                xValues.add(timeLabel);

                                // Add the CO and TVOC values to the corresponding data arrays
                                coEntries.add(new Entry(xValues.size() - 1, coValue.floatValue()));
                                vocEntries.add(new Entry(xValues.size() - 1, vocValue.floatValue()));
                            }
                        }

                        LineDataSet dataSet1 = new LineDataSet(coEntries, "CO");
                        dataSet1.setColor(getResources().getColor(R.color.redoxy));
                        dataSet1.setLineWidth(2f);
                        dataSet1.setCircleColor(getResources().getColor(R.color.redoxy));
                        dataSet1.setCircleHoleColor(getResources().getColor(R.color.redoxy));
                        dataSet1.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getPointLabel(Entry entry) {
                                return (int) entry.getY() + " ppm";
                            }
                        });

                        LineDataSet dataSet2 = new LineDataSet(vocEntries, "VOC");
                        dataSet2.setColor(getResources().getColor(R.color.orangeoxy));
                        dataSet2.setLineWidth(2f);
                        dataSet2.setCircleColor(getResources().getColor(R.color.orangeoxy));
                        dataSet2.setCircleHoleColor(getResources().getColor(R.color.orangeoxy));
                        dataSet2.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getPointLabel(Entry entry) {
                                return (int) entry.getY() + " ppm";
                            }
                        });

                        LineData lineData = new LineData(dataSet1, dataSet2);

                        // Set the X-axis labels
                        XAxis xAxis = linechart_report.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setGranularity(1f);
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));

                        linechart_report.setData(lineData);
                        lineData.setValueTextColor(Color.BLACK);
                        linechart_report.invalidate();
                    } else {
                        // Handle errors, e.g., display a toast or log the error
                        Toast.makeText(getContext(), "Error retrieving data", Toast.LENGTH_SHORT).show();
                        Log.e("FirestoreError", "Error getting documents: " + task.getException());
                    }
                });
            }
        }

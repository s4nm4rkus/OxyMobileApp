package com.capstone.oxy;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
import com.google.android.play.core.integrity.v;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class weekFragmentReport extends Fragment {


    private LineChart linechart_report, linechart_reportweek, linechart_reportmonth;
    private Button datePickerButton;
    private TextView dateTextView;
    private Calendar calendar;

    public weekFragmentReport() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week_report, container, false);

        // Now, reference your views from the inflated layout
        linechart_report = view.findViewById(R.id.line_chart_report);
        linechart_reportweek = view.findViewById(R.id.line_chart_reportWeek);
        datePickerButton = view.findViewById(R.id.datePickerButton);
        dateTextView = view.findViewById(R.id.dateTextView);
        calendar = Calendar.getInstance();

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
                                setupWeekChart(calendar.getTime());

                            }
                        },
                        year,
                        month,
                        day
                );
                datePickerDialog.show();
            }
        });

        return view;
    }

    private void setupWeekChart(Date selectedDate) {
        List<String> xValues = new ArrayList<>();
        List<Entry> coEntries = new ArrayList<>();
        List<Entry> vocEntries = new ArrayList<>();

        // Define the days of the week as labels
        String[] daysOfWeekLabels = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};

        Description description = new Description();
        description.setText("");
        linechart_reportweek.setDescription(description);

        YAxis rightYAxis = linechart_reportweek.getAxisRight();
        rightYAxis.setDrawLabels(false);

        XAxis bottomXAxis = linechart_reportweek.getXAxis();
        bottomXAxis.setAxisLineWidth(2.5f);
        bottomXAxis.setAxisLineColor(getResources().getColor(R.color.tealmain));
        YAxis leftYAxis = linechart_reportweek.getAxisLeft();
        leftYAxis.setAxisLineWidth(2.5f);
        leftYAxis.setAxisLineColor(getResources().getColor(R.color.tealmain));
        YAxis yAxis = linechart_reportweek.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(500f);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY); // Set to Sunday of the selected week

        for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++) {
            Date currentDate = calendar.getTime();
            Date endDate = new Date(currentDate.getTime() + 24 * 60 * 60 * 1000);

            int finalI = i;
            db.collection("sensorData")
                    .whereEqualTo("room_no", "room_2")
                    .whereGreaterThanOrEqualTo("timestamp", currentDate)
                    .whereLessThan("timestamp", endDate)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            float coSum = 0f;
                            float vocSum = 0f;
                            int count = 0;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Double coValue = document.getDouble("CO");
                                Double vocValue = document.getDouble("TVOC");

                                if (coValue != null && vocValue != null) {
                                    coSum += coValue.floatValue();
                                    vocSum += vocValue.floatValue();
                                    count++;
                                }
                            }

                            // Calculate the average for the day
                            float coAvg = (count > 0) ? coSum / count : 0f;
                            float vocAvg = (count > 0) ? vocSum / count : 0f;

                            // Add the average values to the chart's data
                            coEntries.add(new Entry(finalI - 1, coAvg));
                            vocEntries.add(new Entry(finalI - 1, vocAvg));

                            if (finalI == Calendar.SATURDAY) {
                                // Add data to the LineDataSet
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

                                XAxis xAxis = linechart_reportweek.getXAxis();
                                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                xAxis.setGranularity(1f);
                                xAxis.setValueFormatter(new IndexAxisValueFormatter(daysOfWeekLabels));
                                xAxis.setLabelCount(7); // Ensure labels for all days

                                linechart_reportweek.setData(lineData);
                                lineData.setValueTextColor(Color.BLACK);
                                linechart_reportweek.invalidate();
                            }
                        } else {
                            // Handle errors, e.g., display a toast or log the error
                            Toast.makeText(getContext(), "Error retrieving data", Toast.LENGTH_SHORT).show();
                            Log.e("FirestoreError", "Error getting documents: " + task.getException());
                        }
                    });

            // Move to the next day
            calendar.add(Calendar.DAY_OF_WEEK, 1);
        }
    }

}
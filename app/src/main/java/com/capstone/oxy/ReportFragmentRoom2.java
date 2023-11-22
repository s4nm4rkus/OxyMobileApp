package com.capstone.oxy;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
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
import com.github.mikephil.charting.highlight.Highlight;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportFragmentRoom2 extends Fragment {

    private LineChart linechart_report, linechart_reportweek, linechart_reportmonth;
    private Button datePickerButton;
    private TextView dateTextView, dayFrag, weekFrag, monthFrag;
    private Calendar calendar;

    public ReportFragmentRoom2() {
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

        linechart_report = view.findViewById(R.id.line_chart_report);
        linechart_reportweek = view.findViewById(R.id.line_chart_reportWeek);
        linechart_reportmonth = view.findViewById(R.id.line_chart_reportMonth);
        datePickerButton = view.findViewById(R.id.datePickerButton);
        dateTextView = view.findViewById(R.id.dateTextView);
        calendar = Calendar.getInstance();
        dayFrag = view.findViewById(R.id.dayBtn);
        weekFrag = view.findViewById(R.id.weekBtn);
        monthFrag = view.findViewById(R.id.monthBtn);

        dayFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linechart_report.setVisibility(View.VISIBLE);
                linechart_reportweek.setVisibility(View.GONE);
                linechart_reportmonth.setVisibility(View.GONE);
                dayFrag.setBackground(getResources().getDrawable(R.drawable.save_button));
                weekFrag.setBackground(getResources().getDrawable(R.drawable.bg_transparent));
                monthFrag.setBackground(getResources().getDrawable(R.drawable.bg_transparent));


            }
        });

        weekFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linechart_reportweek.setVisibility(View.VISIBLE);
                linechart_report.setVisibility(View.GONE);
                linechart_reportmonth.setVisibility(View.GONE);
                weekFrag.setBackground(getResources().getDrawable(R.drawable.save_button));
                dayFrag.setBackground(getResources().getDrawable(R.drawable.bg_transparent));
                monthFrag.setBackground(getResources().getDrawable(R.drawable.bg_transparent));

            }
        });

        monthFrag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linechart_report.setVisibility(View.GONE);
                linechart_reportweek.setVisibility(View.GONE);
                linechart_reportmonth.setVisibility(View.VISIBLE);
                monthFrag.setBackground(getResources().getDrawable(R.drawable.save_button));
                weekFrag.setBackground(getResources().getDrawable(R.drawable.bg_transparent));
                dayFrag.setBackground(getResources().getDrawable(R.drawable.bg_transparent));
            }
        });


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
                                setupWeekChart(calendar.getTime());
                                setupMonthlyChart(calendar.getTime());

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
                .whereEqualTo("room_no", "room_2")
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


    private void setupMonthlyChart(Date selectedDate) {
        Description description = new Description();
        description.setText("");
        linechart_reportmonth.setDescription(description);

        YAxis rightYAxis = linechart_reportmonth.getAxisRight();
        rightYAxis.setDrawLabels(false);

        XAxis bottomXAxis = linechart_reportmonth.getXAxis();
        bottomXAxis.setAxisLineWidth(2.5f);
        bottomXAxis.setAxisLineColor(getResources().getColor(R.color.tealmain));

        YAxis leftYAxis = linechart_reportmonth.getAxisLeft();
        leftYAxis.setAxisLineWidth(2.5f);
        leftYAxis.setAxisLineColor(getResources().getColor(R.color.tealmain));
        leftYAxis.setAxisMinimum(0f);
        leftYAxis.setAxisMaximum(500f);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        int currentMonth = calendar.get(Calendar.MONTH);
        int currentWeek = 1; // Initialize the current week number

        List<String> xValues = new ArrayList<>();
        List<Entry> coWeeklyAverages = new ArrayList<>();
        List<Entry> vocWeeklyAverages = new ArrayList<>();
        fetchWeeklyDataForMonth(calendar.getTime(), currentWeek, xValues, coWeeklyAverages, vocWeeklyAverages, currentMonth);
    }

    private void fetchWeeklyDataForMonth(Date startDate, int currentWeek, List<String> xValues,
                                         List<Entry> coWeeklyAverages, List<Entry> vocWeeklyAverages, int currentMonth) {
        Calendar endDateCalendar = Calendar.getInstance();
        endDateCalendar.setTime(startDate);
        endDateCalendar.add(Calendar.DATE, 6); // Set end date to the end of the week (7 days ahead)

        FirebaseFirestore.getInstance().collection("sensorData")
                .whereEqualTo("room_no", "room_2")
                .whereGreaterThanOrEqualTo("timestamp", startDate)
                .whereLessThan("timestamp", endDateCalendar.getTime())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        float coSum = 0f;
                        float vocSum = 0f;
                        int count = 0;

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Process the data and calculate averages
                            Double coValue = document.getDouble("CO");
                            Double vocValue = document.getDouble("TVOC");

                            if (coValue != null && vocValue != null) {
                                coSum += coValue.floatValue();
                                vocSum += vocValue.floatValue();
                                count++;
                            }
                        }

                        // Calculate averages
                        float coAvg = (count > 0) ? coSum / count : 0f;
                        float vocAvg = (count > 0) ? vocSum / count : 0f;

                        // Add the average values to the chart's data
                        coWeeklyAverages.add(new Entry(currentWeek, coAvg));
                        vocWeeklyAverages.add(new Entry(currentWeek, vocAvg));

                        // Add week label to X-axis
                        xValues.add("Week " + currentWeek);

                        // Update UI, e.g., update chart, set xAxis labels, etc.
                        updateMonthlyChart(xValues, coWeeklyAverages, vocWeeklyAverages);

                        // Move to the next week or check if the month has ended
                        if (endDateCalendar.get(Calendar.MONTH) == currentMonth) {
                            startDate.setTime(endDateCalendar.getTimeInMillis() + 24 * 60 * 60 * 1000); // Move to the next day
                            int nextWeek = currentWeek + 1;
                            fetchWeeklyDataForMonth(startDate, nextWeek, xValues, coWeeklyAverages, vocWeeklyAverages, currentMonth);
                        }
                    } else {
                        // Handle errors
                    }
                });
    }
    private void updateMonthlyChart(List<String> xValues, List<Entry> coWeeklyAverages, List<Entry> vocWeeklyAverages) {
        LineDataSet dataSet1 = new LineDataSet(coWeeklyAverages, "CO");
        LineDataSet dataSet2 = new LineDataSet(vocWeeklyAverages, "VOC");

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

        XAxis xAxis = linechart_reportmonth.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));

        linechart_reportmonth.setData(lineData);
        linechart_reportmonth.invalidate();
    }

}

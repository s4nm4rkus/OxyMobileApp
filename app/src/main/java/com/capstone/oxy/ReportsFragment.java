package com.capstone.oxy;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportsFragment extends Fragment {

    private LineChart linechart_report;

    public ReportsFragment() {
        // Required empty public constructor
    }

    public static ReportsFragment newInstance() {
        return new ReportsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        linechart_report = view.findViewById(R.id.line_chart_report);

        Description description = new Description();
        description.setText("Air Quality Report");
        description.setPosition(300f, 15f);
        description.setTextSize(10);
        description.setTextAlign(Paint.Align.CENTER);
        description.setTextColor(getResources().getColor(R.color.oxyblack));

        linechart_report.setDescription(description);
        linechart_report.getAxisRight().setDrawLabels(false);

        XAxis xAxis = linechart_report.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);

        // Initialize an empty list for X-axis labels
        List<String> xValues = new ArrayList<>();

        YAxis yAxis = linechart_report.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(500f);
        yAxis.setAxisLineColor(getResources().getColor(R.color.lightgrey));

        // Firestore data retrieval
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("sensorData")
                .whereEqualTo("room_no", "room_1") // Filter for documents with "room_no" field equal to "room_1"
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Entry> coEntries = new ArrayList<>();
                        List<Entry> vocEntries = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Assuming your Firestore documents have fields 'co' and 'voc'
                            float coValue = document.getDouble("CO").floatValue();
                            float vocValue = document.getDouble("TVOC").floatValue();

                            // Assuming you have a field "timestamp" with a timestamp
                            long timestamp = document.getTimestamp("timestamp").getSeconds();

                            // Convert the timestamp to a formatted time string
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                            String timeLabel = sdf.format(new Date(timestamp * 1000)); // Multiply by 1000 to convert seconds to milliseconds

                            // Add the time label to the X-axis labels
                            xValues.add(timeLabel);

                            // Add the CO and TVOC values to the corresponding data arrays
                            coEntries.add(new Entry(xValues.size() - 1, coValue));
                            vocEntries.add(new Entry(xValues.size() - 1, vocValue));
                        }

                        LineDataSet dataSet1 = new LineDataSet(coEntries, "CO");
                        dataSet1.setColor(Color.RED);

                        LineDataSet dataSet2 = new LineDataSet(vocEntries, "VOC");
                        dataSet2.setColor(Color.GREEN);

                        LineData lineData = new LineData(dataSet1, dataSet2);

                        // Set the X-axis labels
                        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));

                        linechart_report.setData(lineData);
                        linechart_report.invalidate();
                    } else {
                        // Handle errors here
                    }
                });

        return view;
    }
}

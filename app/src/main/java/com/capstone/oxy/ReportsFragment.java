package com.capstone.oxy;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReportsFragment extends Fragment {

    private LineChart linechart_report;

    private List<String> xValues;

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

        xValues = Arrays.asList("SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT");

        XAxis xAxis = linechart_report.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));
        xAxis.setLabelCount(8);
        xAxis.setGranularity(1f);

        YAxis yAxis = linechart_report.getAxisLeft();
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(500f);
        yAxis.setAxisLineColor(getResources().getColor(R.color.lightgrey));
        yAxis.setLabelCount(8);

        List<Entry> entries1 = new ArrayList<>();
        entries1.add(new Entry(0, 50f));
        entries1.add(new Entry(1, 10f));
        entries1.add(new Entry(2, 30f));
        entries1.add(new Entry(3, 40f));
        entries1.add(new Entry(4, 30f));
        entries1.add(new Entry(5, 400f));
        entries1.add(new Entry(6, 50f));

        List<Entry> entries2 = new ArrayList<>();
        entries2.add(new Entry(0, 50f));
        entries2.add(new Entry(1, 100f));
        entries2.add(new Entry(2, 150f));
        entries2.add(new Entry(3, 200f));
        entries2.add(new Entry(4, 30f));
        entries2.add(new Entry(5, 40f));
        entries2.add(new Entry(6, 10f));

        LineDataSet dataSet1 = new LineDataSet(entries1, "CO");
        dataSet1.setColor(Color.RED);

        LineDataSet dataSet2 = new LineDataSet(entries2, "VOC");
        dataSet2.setColor(Color.GREEN);

        LineData lineData = new LineData(dataSet1, dataSet2);
        linechart_report.setData(lineData);
        linechart_report.invalidate();


        return view;
    }
}
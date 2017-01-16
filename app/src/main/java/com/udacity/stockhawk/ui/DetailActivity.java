package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.udacity.stockhawk.R;

import java.util.ArrayList;
import java.util.List;

import static com.udacity.stockhawk.R.id.testchart;


/**
 * Created by pestonio on 07/01/2017.
 */

public class DetailActivity extends AppCompatActivity {
    private LineChart chart;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        Intent intent = getIntent();
        String currentStockSymbol = intent.getStringExtra("Symbol");
        String currentStockHistory = intent.getStringExtra("History");
//        Toast.makeText(this, "Current Stock: " + currentStockSymbol, Toast.LENGTH_SHORT).show();
        TextView test = (TextView) findViewById(R.id.test);
        TextView testHistory = (TextView) findViewById(R.id.history);
        test.setText(currentStockSymbol);
//        testHistory.setText(currentStockHistory);
        chart = (LineChart) findViewById(testchart);
        createGraph (currentStockHistory);
    }

    private void createGraph(String history){
        List<Entry> entries = new ArrayList<Entry>();
        final List<String> dates = new ArrayList<String>();
        String[] lines = history.split("\n");
        int position = lines.length;
        int values = position-1;
        int index = 0;
        while (values >=0){
            dates.add(String.valueOf(DateFormat.format("dd/MM/yyyy", Long.parseLong(lines[values].replace(" ","").split(",")[0]))));
            Entry entry = new Entry(index++, Float.parseFloat(lines[values].replace(" ","").split(",")[1]));
            entries.add(entry);
            values--;
        }
        IAxisValueFormatter axisValueFormatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return (String) dates.toArray()[(int) value];
            }
        };
        LineDataSet dataSet = new LineDataSet(entries, getString(R.string.chart_key));
        LineData lineData = new LineData(dataSet);
        int colour = ContextCompat.getColor(getApplicationContext(), R.color.line_chart);
        chart.getAxisLeft().setTextColor(colour);
        chart.getAxisRight().setTextColor(colour);
        chart.getXAxis().setTextColor(colour);
        chart.getLegend().setTextColor(colour);
        XAxis xAxis = chart.getXAxis();
        dataSet.setValueTextColor(colour);
        xAxis.setDrawAxisLine(true);
        xAxis.setGranularity(1f);
        chart.setScaleYEnabled(false);
        Description description = new Description();
        description.setText("");
        chart.setDescription(description);
        chart.setDrawBorders(true);
        xAxis.setLabelRotationAngle(30);
        xAxis.setValueFormatter(axisValueFormatter);
        chart.setData(lineData);
        chart.invalidate();
    }
}
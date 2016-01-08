package me.alpha12.ecarnet.charts;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guilhem on 07/01/2016.
 */
public class LineChartCustom {

    private LineChart chart;
    private XAxis xAxis;
    private YAxis yLeftAxis;
    private YAxis yRightAxis;
    private Legend legend;
    private LineDataSet dataset;
    private List<Integer> colors;
    private LineData data;
    private ArrayList<Entry> entries;


    public LineChartCustom(LineChart graphical, ArrayList<Entry> entries, String entriesLegend, ArrayList<String> labels, String chartDecription)
    {
        this.entries = entries;
        this.chart = graphical;
        this.dataset = new LineDataSet(entries, entriesLegend);
        this.data = new LineData(labels, this.dataset);
        this.colors = new ArrayList<>();
        this.yLeftAxis = chart.getAxisLeft();
        this.yRightAxis = chart.getAxisRight();
        this.xAxis = chart.getXAxis();
        this.legend = chart.getLegend();
        this.chart.setAutoScaleMinMaxEnabled(true);
        this.chart.setTouchEnabled(false);
        if(chartDecription!= null)
            chart.setDescription(chartDecription);
        else chart.setDescription("");
        setDefaultChart();
        setDefaultLegend();
        setDefaultAxes();
    }

    public void addEntries(ArrayList<Entry> entries)
    {
        LineDataSet addData = new LineDataSet(entries, "");
        this.data.addDataSet(addData);
        addData.setDrawFilled(true);
        addData.setColor(0xFFFF5722);
    }

    public void setDefaultChart()
    {
        this.dataset.setDrawValues(false);
        this.dataset.setDrawCircles(true);
        this.dataset.setCircleColor(0xFFB71C1C);
        this.dataset.setLineWidth(1.5f);
        this.dataset.setCircleSize(2);
        this.data.setValueTextSize(12f);
        this.data.setValueTextColor(Color.BLACK);
        this.chart.setDrawGridBackground(false);
        this.chart.setData(data);
        setColor(0xFFF44336);
        this.dataset.setDrawFilled(false);
        this.dataset.setColor(0xFFF44336);
        this.dataset.setCircleColorHole(0xFFB71C1C);
        this.dataset.setFillColor(0xFFF44336);
    }

    public void setDefaultLegend()
    {
        this.legend.setEnabled(false);
    }

    public void setDefaultAxes() {
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setEnabled(false);
        yLeftAxis.setEnabled(false);
        yRightAxis.setEnabled(false);
        chart.animateY(2000);
        chart.animateX(1000);
    }

    public void setColor(int color)
    {
        this.colors.clear();
        this.colors.add(color);
        this.dataset.setColors(colors);
    }
}

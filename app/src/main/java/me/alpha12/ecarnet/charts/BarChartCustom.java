package me.alpha12.ecarnet.charts;

import android.graphics.Color;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import me.alpha12.ecarnet.R;

/**
 * Created by guilhem on 06/01/2016.
 */
public class BarChartCustom {

    private BarChart chart;
    private XAxis xAxis;
    private YAxis yLeftAxis;
    private YAxis yRightAxis;
    private Legend legend;
    private BarDataSet dataset;
    private List<Integer> colors;
    private BarData data;
    private LimitLine average;
    private ArrayList<BarEntry> entries;
    private boolean averageBar = false;


    public BarChartCustom(BarChart graphical, ArrayList<BarEntry> entries, String entriesLegend, ArrayList<String> labels, String chartDecription)
    {
        this.entries = entries;
        this.chart = graphical;
        this.dataset = new BarDataSet(entries, entriesLegend);
        this.data = new BarData(labels, this.dataset);
        this.colors = new ArrayList<>();
        this.yLeftAxis = chart.getAxisLeft();
        this.yRightAxis = chart.getAxisRight();
        this.xAxis = chart.getXAxis();
        chart.setTouchEnabled(false);
        this.average = new LimitLine(getAverage(), String.format("%1$s : %2$.2f", "Moyenne", getAverage()));
        this.average.setTextSize(10);
        this.average.setLineColor(Color.RED);
        this.average.setLineWidth(1.5f);
        this.yLeftAxis.addLimitLine(average);
        this.average.setEnabled(false);
        this.legend = chart.getLegend();
        if(chartDecription!= null)
            chart.setDescription(chartDecription);
        else chart.setDescription("");
        setDefaultChart();
        setDefaultLegend();
        setDefaultAxes();
    }


    public void setAverageBar(boolean result)
    {
        if(result)
        {
            this.average.setEnabled(true);
        }
        else
        {
            this.average.setEnabled(false);
        }
    }

    public float getAverage()
    {
        float value = 0f;
        for(BarEntry entry : this.entries)
        {
            value += entry.getVal();
        }
        return value / this.entries.size();
    }

    public void addEntries(ArrayList<BarEntry> entries)
    {
        BarDataSet addData = new BarDataSet(entries, "");
        addData.setDrawValues(false);
        addData.setColor(0xFF2196F3);
        this.data.addDataSet(addData);
    }

    public void setDefaultChart()
    {
        this.data.setDrawValues(false);
        this.data.setValueTextSize(18f);
        this.data.setValueTextColor(Color.WHITE);
        this.chart.setDrawGridBackground(false);
        this.chart.setData(data);
        setColor(0xFFF44336);
        chart.setDrawValueAboveBar(false);
    }

    public void setDefaultLegend()
    {
        this.legend.setEnabled(false);
    }

    public void setDefaultAxes() {
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(60);
        xAxis.setDrawLimitLinesBehindData(false);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelsToSkip(0);
        xAxis.setTextSize(8);
        yLeftAxis.setEnabled(false);
        yRightAxis.setEnabled(false);
        xAxis.setGridColor(Color.WHITE);
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

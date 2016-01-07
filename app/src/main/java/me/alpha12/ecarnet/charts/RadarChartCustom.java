package me.alpha12.ecarnet.charts;

import android.graphics.Color;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guilhem on 07/01/2016.
 */
public class RadarChartCustom {

    private RadarChart chart;
    private Legend legend;
    private RadarDataSet dataset;
    private List<Integer> colors;
    private XAxis xAxis;
    private YAxis yAxis;
    private RadarData data;
    private ArrayList<Entry> entries;


    public RadarChartCustom(RadarChart graphical, ArrayList<Entry> entries, String entriesLegend, ArrayList<String> labels, String chartDecription)
    {
        this.entries = entries;
        this.chart = graphical;
        this.dataset = new RadarDataSet(entries, entriesLegend);
        this.data = new RadarData(labels, this.dataset);
        this.chart.setData(this.data);
        this.colors = new ArrayList<>();
        this.yAxis = chart.getYAxis();
        this.xAxis = chart.getXAxis();
        this.legend = chart.getLegend();
        this.chart.setSkipWebLineCount(1);
        if(chartDecription!= null)
            chart.setDescription(chartDecription);
        else chart.setDescription("");
        setDefaultChart();
        setDefaultLegend();
        setDefaultAxes();
    }

    public void setDefaultChart()
    {
        this.dataset.setDrawFilled(true);
        this.dataset.setDrawValues(false);
        this.data.setValueTextSize(14f);
        this.chart.setData(data);
        setColor(0xFF2196F3);
    }

    public void setDefaultLegend()
    {
        this.legend.setEnabled(false);
    }

    public void setDefaultAxes() {
        this.xAxis.setDrawGridLines(false);
        yAxis.setEnabled(false);
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

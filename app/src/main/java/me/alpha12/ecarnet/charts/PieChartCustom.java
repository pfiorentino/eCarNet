package me.alpha12.ecarnet.charts;

import android.graphics.Color;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guilhem on 06/01/2016.
 */
public class PieChartCustom {

    private PieChart chart;
    private Legend legend;
    private PieDataSet dataset;
    private List<Integer> colors;
    private PieData data;
    private ArrayList<Entry> entries;


    public PieChartCustom(PieChart graphical, ArrayList<Entry> entries, String entriesLegend, ArrayList<String> labels, String chartDecription)
    {
        this.entries = entries;
        this.chart = graphical;
        this.dataset = new PieDataSet(entries, entriesLegend);
        this.data = new PieData(labels, this.dataset);
        this.colors = new ArrayList<>();
        //chart.setTouchEnabled(false);
        this.legend = chart.getLegend();
        if(chartDecription!= null)
            chart.setDescription(chartDecription);
        else chart.setDescription("");
        setDefaultChart();
        setDefaultLegend();
    }

    public void setDefaultChart()
    {
        this.data.setValueTextSize(18f);
        this.data.setValueTextColor(Color.WHITE);
        this.chart.setData(data);
        this.chart.setDrawHoleEnabled(false);
        this.chart.setDrawHoleEnabled(true);
        this.chart.setTransparentCircleRadius(10);
        this.chart.setRotationAngle(0);
        this.chart.setDrawSliceText(true);
        this.chart.setRotationEnabled(true);
        setColor();
    }

    public void setDefaultLegend()
    {
        this.legend.setEnabled(false);
    }

    public void setColor()
    {
        this.colors.clear();
        this.colors.add(0xFF2196F3);
        this.dataset.setColors(colors);
    }
}

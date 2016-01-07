package me.alpha12.ecarnet.charts;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.util.Log;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

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
        float sum = 0f;
        for(int i = 0; i<entries.size(); i++)
        {
            sum += entries.get(i).getVal();
        }
        this.chart.setCenterText(String.format("%1$s\n%2$.2f", "Total", sum));
        this.chart.setCenterTextSize(16);
        Paint text = new Paint();
        text.setTextAlign(Paint.Align.CENTER);
        this.chart.setPaint(text, Chart.PAINT_CENTER_TEXT);
        this.chart.setUsePercentValues(true);
        setDefaultChart();
        setDefaultLegend();
    }

    public void setDefaultChart()
    {
        this.data.setValueTextSize(14f);
        this.data.setValueTextColor(Color.WHITE);
        this.chart.setData(data);
        this.chart.setDrawHoleEnabled(false);
        this.chart.setDrawHoleEnabled(true);
        this.chart.setTransparentCircleRadius(10);
        this.chart.setRotationAngle(0);
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
        this.colors.add(0xFFF44336);
        this.colors.add(0xFF009688);
        this.colors.add(0xFF8BC34A);
        this.colors.add(0xFFFFC107);
        this.colors.add(0xFFFF5722);
        this.colors.add(0xFF795548);
        this.colors.add(0xFF9E9E9E);
        this.colors.add(0xFF607D8B);
        this.dataset.setColors(colors);
    }
}

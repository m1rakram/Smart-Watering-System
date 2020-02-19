package com.example.mirakram.fianlsuvarma;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by Mirakram on 12/16/2017.
 */

public class DynamicChart {

    Activity activity;
    Context context;
    public int tempdata=10;
    public int soil=10;
    public int humidity=10;
    public GraphView graph;
    public GraphView graph2;
    public GraphView graph3;
    public TextView cursoil, curtemp, curhumid;

    public int count=0;
    public int count1=0;
    public int count2=0;
    public LineGraphSeries<DataPoint> soilseries = new LineGraphSeries<DataPoint>(new DataPoint[] {
            new DataPoint(count, 0),
    });

    public LineGraphSeries<DataPoint> tempseries = new LineGraphSeries<DataPoint>(new DataPoint[] {
            new DataPoint(count1, 0),
    });

    public LineGraphSeries<DataPoint> humidseries = new LineGraphSeries<DataPoint>(new DataPoint[] {
            new DataPoint(count2, 0),
    });

    public DynamicChart(Activity activity, Context context){
        this.activity=activity;
        this.context=context;

        cursoil=(TextView)activity.findViewById(R.id.cursoil);
        curtemp=(TextView)activity.findViewById(R.id.curtemp);
        curhumid=(TextView)activity.findViewById(R.id.curhumid);
    }

    public void graphing(){
        graph = (GraphView) activity.findViewById(R.id.graph);
        graph2 = (GraphView) activity.findViewById(R.id.graph2);
        graph3 = (GraphView) activity.findViewById(R.id.graph3);
        this.count++;
        this.count1++;
        this.count2++;

        graph.addSeries(soilseries);
        graph.setTitle("Soil Data");
        graph.getViewport().setMinX(1);
        graph.getViewport().setMaxX(soil);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setScrollable(true);
        graph.getGridLabelRenderer().setPadding(48);

        graph2.addSeries(humidseries);
        graph2.setTitle("Humidity Data");
        graph2.getViewport().setMinX(1);
        graph2.getViewport().setMaxX(humidity);
        graph2.getViewport().setXAxisBoundsManual(true);
        graph2.getViewport().setScrollable(true);
        graph2.getGridLabelRenderer().setPadding(48);

        graph3.addSeries(tempseries);
        graph3.setTitle("Temperature Data");
        graph3.getViewport().setMinX(1);
        graph3.getViewport().setMaxX(tempdata);
        graph3.getViewport().setXAxisBoundsManual(true);
        graph3.getViewport().setScrollable(true);
        graph3.getGridLabelRenderer().setPadding(48);

    }

    public void newdata(String topic, float data){
        if(topic.trim().equals("nijat97/feeds/soildata")) {
            if(count==soil){
                soil=soil*2;
                graph.getViewport().setMaxX(soil);
            }
            this.soilseries.appendData(new DataPoint(this.count, data), false, 1024);
            this.count++;
            cursoil.setText("Soil data: "+data);
        }
        else{
            if(topic.trim().equals("nijat97/feeds/temperature")) {
                if(count1==tempdata) {
                    tempdata = tempdata * 2;
                    graph3.getViewport().setMaxX(tempdata);
                }
                this.tempseries.appendData(new DataPoint(this.count1, data), false, 1024);
                this.count1++;
                curtemp.setText("Temperature: "+ data+"°C");
            }
            else{
                    if(count2==humidity) {
                        humidity = humidity * 2;
                        graph2.getViewport().setMaxX(tempdata);
                    }
                this.humidseries.appendData(new DataPoint(this.count2, data), false, 1024);
                this.count2++;
                curhumid.setText("Humidity: "+data+"%");
            }
        }
    }

}

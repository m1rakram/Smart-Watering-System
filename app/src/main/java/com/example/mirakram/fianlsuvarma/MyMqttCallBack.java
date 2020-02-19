package com.example.mirakram.fianlsuvarma;

import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by Mirakram on 12/12/2017.
 */

public class MyMqttCallBack implements MqttCallback {
    private String message = "400";
    public DynamicChart d;

    public String getMessage()
    {

            return message;


    }

    public void putchart(DynamicChart d){
        this.d=d;
    }
    @Override
    public void connectionLost(Throwable cause) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        this.message = message.toString();
        d.newdata(topic,Float.parseFloat(message.toString()));

        Log.d("Wotah", topic + " " + message);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }



}

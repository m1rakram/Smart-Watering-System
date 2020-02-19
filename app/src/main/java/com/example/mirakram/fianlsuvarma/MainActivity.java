package com.example.mirakram.fianlsuvarma;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {
    private String server_uri = "tcp://io.adafruit.com:1883";
    private String id = "ba9f726010154a4b845a0d17b04dc445";
    private String username = "nijat97";
    private String publish_topic1 = "nijat97/feeds/threshold";
    private String publish_topic2 = "nijat97/feeds/command";
    private String subscribe_topic ;
    private String subscribe_topic1 = "nijat97/feeds/soildata";
    private String subscribe_topic2 = "nijat97/feeds/temperature";
    private String subscribe_topic3 = "nijat97/feeds/humidity";
    private int qos = 0;
    public String seekvalue;
    String s="problem ";
    private MqttAndroidClient client;
    MyMqttCallBack mqtt = new MyMqttCallBack();
    MqttMessage mes;
    public DynamicChart d;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = findViewById(R.id.activity_main);

        this.d=new DynamicChart(MainActivity.this, getApplicationContext());
        mqtt.putchart(d);
        client = new MqttAndroidClient(getApplicationContext(), server_uri, id);
        connectServer();


        //publish
        Button publish = (Button) findViewById(R.id.publish);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishData(seekvalue, publish_topic1);
                Snackbar.make(view, "Published", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Switch auto_switch = (Switch)findViewById(R.id.switch_auto_watering);
        final Switch manual_switch = (Switch)findViewById(R.id.switch_manual_watering);


        auto_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Toast.makeText(getApplicationContext(), "On", Toast.LENGTH_SHORT).show();
                    manual_switch.setEnabled(false);
                    publishData("AON", publish_topic2);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Off", Toast.LENGTH_SHORT).show();
                    manual_switch.setEnabled(true);
                    publishData("AOFF", publish_topic2);
                    publishData("U_M_OFF", publish_topic2);
                }
            }
        });
        manual_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Toast.makeText(getApplicationContext(), "On", Toast.LENGTH_SHORT).show();
                    publishData("U_M_ON", publish_topic2);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Off", Toast.LENGTH_SHORT).show();
                    publishData("U_M_OFF", publish_topic2);
                }
            }
        });




        CrystalSeekbar seekbar = (CrystalSeekbar)findViewById(R.id.rangeSeekbar1);
        final TextView rangeMin = (TextView)findViewById(R.id.textview_for_seekbar_min);

        seekbar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                rangeMin.setText("Threshold: "+String.valueOf(value)+"0");
                seekvalue=String.valueOf(value)+"0";
            }
        });


    }

    @Override
    public void onBackPressed() {
    }

    public void connectServer()
    {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(true);
        char[] password =  id.toCharArray();
        options.setPassword(password);
        options.setUserName(username);

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d("Wotah", "Connected");
                    subscribeData(subscribe_topic1);
                    subscribeData(subscribe_topic2);
                    subscribeData(subscribe_topic3);
                    d.graphing();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d("Wotah", "NOT Connected");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    public void publishData(String data, String publish_topic)
    {

        String payload = data;
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setQos(0);
            message.setRetained(true);
            message.setPayload(encodedPayload);

            IMqttToken token = client.publish(publish_topic, message);

            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    Log.d("Wotah","Published");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.d("Wotah","NOT Published");
                }
            });
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }


    public void subscribeData(String topic)
    {
        subscribe_topic=topic;
        try {
            IMqttToken subToken = client.subscribe(subscribe_topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // The message was published
                    Log.d("Wotah","Subscribed");
                    Snackbar.make(view, "Subscribed", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // The subscription could not be performed, maybe the user was not
                    // authorized to subscribe on the specified topic e.g. using wildcards
                    Log.d("Wotah","NOT Subscribed");
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        client.setCallback(mqtt);

    }




}

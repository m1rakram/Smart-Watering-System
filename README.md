## Automatic Watering System
This project is student startup that was intended to control watering system actions in soil and monitor current situation. Project has 2 parts:
1. Electronics
2. Android

This repository shows only android part of project. Principle is very simple. Here we have 2 control modes Automatic and Manual. In Automatic mode we can adjust the threshold in order to balance the humidity in soil. Manual mode allows us control the pump however we want. Graphs are for monitoring the temperature and humidity. 
To store and subscribe the data, I used Adafruit.io server. We subscribe some channels and we get information whenever data comes. Pump controller is also subscribed to the channel to get commands about modes and on/off.


<img src="https://raw.githubusercontent.com/mirakram1/Smart-Watering-System/master/photos/Automatic_watering_system.jpg" width="700" height="450">

<img src="https://raw.githubusercontent.com/mirakram1/Smart-Watering-System/master/photos/photo.jpeg" width="250" height="500">

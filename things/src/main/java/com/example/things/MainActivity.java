package com.example.things;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManager;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private Gpio gpioPin;
    private boolean status;
    SensorCallBack callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Устанавливаем соединение с сенсором
        PeripheralManager manager = PeripheralManager.getInstance();
        gpioPin = null;
        try {
            gpioPin = manager.openGpio(BoardPins.getPirPin());
            gpioPin.setDirection(Gpio.DIRECTION_IN);
            gpioPin.setActiveType(Gpio.ACTIVE_HIGH);
            // Устанавливаем событие, которое хотим прослушивать
            gpioPin.setEdgeTriggerType(Gpio.EDGE_RISING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Устанавливаем обработчик колбека
        callback = new SensorCallBack();
        try {
            gpioPin.registerGpioCallback(callback);
        } catch (IOException e) {
            e.printStackTrace();
        }
        TextView text1 = findViewById(R.id.text1);
        text1.setText("This app runs on the "+BoardPins.getBoardName()+" board using "+BoardPins.getPirPin()+" pin!");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MainActivity", "onDestroy");
        if (gpioPin != null) {
            gpioPin.unregisterGpioCallback(callback);
            try {
                gpioPin.close();
            } catch (IOException e) {
                e.printStackTrace();

            }
            gpioPin = null;
        }
    }

        // Callback обработчик
    public class SensorCallBack implements GpioCallback {
        @Override
        public boolean onGpioEdge(Gpio gpio) {
            try {
                boolean callBackState = gpio.getValue();
                Log.d("SensorCallBack","Callback state ["+callBackState+"]");
                NotificationManager.getInstance().sendNotificaton("Alarm!", "AAAAqGLurao:APA91bHaJ6p1MtLwDgo1IdZPvYEfJElegtXsrKybwaPSZ7B4RsyTOtSJWzoP7JpO5utLyZZvGyNT2faRcQV1tFd7XbyNqjDW0kH4ZwfGhIOXIsYGH8ZmZYV6fNctSqWnCeleYq2rR1-p");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
        @Override
        public void onGpioError(Gpio gpio, int error) {
            Log.e("SensorCallBack","GPIO error");
        }
    }

}
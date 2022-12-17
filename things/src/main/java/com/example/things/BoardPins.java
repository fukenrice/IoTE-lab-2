package com.example.things;

import android.os.Build;
import com.google.android.things.pio.PeripheralManager;
import java.util.List;

// Класс-словарь, предоставляющий корректные названия пинов для разных моделей плат
public class BoardPins {
    private static final String EDISON_ARDUINO = "edison_arduino";
    private static final String RASPBERRY ="rpi3";
    public static String getPirPin() {
        switch (getBoardName()) {
            case RASPBERRY:
                return "BCM4";
            case EDISON_ARDUINO:
                return "IO4";
            default:
                throw new IllegalArgumentException ("Unsupported device");
        }
    }
    public static String getBoardName() {
        String name = Build.BOARD;
        if (name.equals("edison")) {
            PeripheralManager manager = PeripheralManager.getInstance();
            List<String> pinList = manager.getGpioList();
            if (pinList.size() > 0) {
                String pinName = pinList.get(0);
                if (pinName.startsWith("IO")) return EDISON_ARDUINO;
            }
        }
        return name;
    }
}

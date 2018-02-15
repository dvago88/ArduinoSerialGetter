package com.danielvargas;

import com.danielvargas.rfid.Interface;
import com.danielvargas.rfid.PiApplication;
import com.danielvargas.rfid.RfidListener;
import com.danielvargas.serial.SerialReader2;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws Exception {
        SerialReader2 main = new SerialReader2();
//        main.initialize("COM7");
        main.initialize("/dev/ttyACM0");

        Thread rfid = new Thread(() -> {
            PiApplication piApplication = new PiApplication();
            while (true) {
                piApplication.startReading();
            }
        });
        rfid.start();
        Thread secondPort = new Thread(() -> {
            SerialReader2 secondary = new SerialReader2();
//            secondary.initialize("COM4");
            secondary.initialize("/dev/ttyUSB0");
        });
//        secondPort.start();
        Thread thirdPort = new Thread(() -> {
            SerialReader2 third = new SerialReader2();
//            third.initialize("COM5");
            third.initialize("/dev/ttyACM1");
        });
//        thirdPort.start();
        Thread t = new Thread(() -> {
            //Esto es para que no se cierre la conexion
            try {
                Thread.sleep(1000000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        });
        t.start();
        System.out.println("Iniciado correctamente");
    }
}

package com.danielvargas;

//import com.danielvargas.rfid.PiApplication;

import com.danielvargas.rfid.PiApplication;
//import com.danielvargas.serial.SerialReader2;
import com.danielvargas.rfid.PiApplicationMocker;
import com.danielvargas.serial.SerialReader2;

public class Application {

    public static void main(String[] args) throws Exception {
        SerialReader2 main = new SerialReader2();
                main.initialize("COM3");
//        main.initialize("/dev/ttyACM0");

        Thread rfid = new Thread(() -> {
//            PiApplication piApplication = new PiApplication(main);
            PiApplicationMocker piApplication = new PiApplicationMocker(main);
            while (true) {
                piApplication.startReading();
            }
        });
        rfid.start();
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

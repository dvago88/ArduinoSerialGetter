package com.danielvargas.rfid;

import com.danielvargas.serial.SerialReader2;

import java.util.Scanner;
import java.util.logging.Logger;

public class PiApplicationMocker {

    private static final Logger log = Logger.getLogger(PiApplication.class.getName());
    private SerialReader2 serialReader;
    private Scanner scanner;

    public PiApplicationMocker(SerialReader2 serialReader01) {
        serialReader = serialReader01;
        Interface.init("/home/pi/MFRC522-python/Read.py", "/home/pi/MFRC522-python/Write.py");
        scanner = new Scanner(System.in);
    }

    @SuppressWarnings({"LoggerStringConcat", "CallToPrintStackTrace"})
    public void startReading() {

        log.info("RFID leyendo:");
        long rfid = scanner.nextLong();
        String content = scanner.next();
        serialReader.usarEstacion(rfid, content);
        log.info("Id: " + rfid);
        log.info("Contenido: " + content);
    }
}

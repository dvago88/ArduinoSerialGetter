package com.danielvargas.rfid;

import com.danielvargas.serial.SerialReader2;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Test class
 *
 * @author Sacredgamer (https://github.com/Sacredgamer/RFID-RC552-Interface)
 */


public class PiApplication {

    static final Logger log = Logger.getLogger(PiApplication.class.getName());
    public SerialReader2 serialReader2;

    public PiApplication() {
        serialReader2 = new SerialReader2();
        Interface.init("/home/pi/MFRC522-python/Read.py", "/home/pi/MFRC522-python/Write.py");
    }

    @SuppressWarnings({"LoggerStringConcat", "CallToPrintStackTrace"})
    public void startReading() {

        log.info("Reader test:");
        try {
            RfidListener reader = new RfidListener();
            reader.read();
            long rfid = reader.getId();
            String content = reader.getContent();
            serialReader2.usarEstacion(rfid, content);
            log.info("Id: " + rfid);
            log.info("Content: " + content);
        } catch (IOException ioe) {
            Logger.getLogger(PiApplication.class.getName()).log(Level.SEVERE, null, ioe);
        }
    }

}

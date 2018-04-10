
package com.danielvargas.rfid;

import com.danielvargas.serial.SerialReader2;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Test class
 * All credit to:
 * @author Sacredgamer (https://github.com/Sacredgamer/RFID-RC552-Interface)
 */


public class PiApplication {

    private static final Logger log = Logger.getLogger(PiApplication.class.getName());
    private SerialReader2 serialReader;

    public PiApplication(SerialReader2 serialReader01) {
        serialReader = serialReader01;
        Interface.init("/home/pi/MFRC522-python/Read.py", "/home/pi/MFRC522-python/Write.py");
    }

    @SuppressWarnings({"LoggerStringConcat", "CallToPrintStackTrace"})
    public void startReading() {

        log.info("RFID leyendo:");
        try {
            RfidListener reader = new RfidListener();
            reader.read();
            long rfid = reader.getId();
            String content = reader.getContent();
            serialReader.usarEstacion(rfid, content);
            log.info("Id: " + rfid);
            log.info("Contenido: " + content);
        } catch (IOException ioe) {
            Logger.getLogger(PiApplication.class.getName()).log(Level.SEVERE, null, ioe);
        }
    }

}


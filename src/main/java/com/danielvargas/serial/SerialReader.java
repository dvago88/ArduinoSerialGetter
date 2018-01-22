package com.danielvargas.serial;

import com.danielvargas.entities.DataEntity;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.danielvargas.rest.*;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.util.Enumeration;


public class SerialReader implements SerialPortEventListener {
    private SerialPort serialPort;
    private SerialFormaterForRestRequest sf = new SerialFormaterForRestRequest();
    private GetRequest getReq = new GetRequest();
    private PostRequest postReq = new PostRequest();
    private PutRequest putReq = new PutRequest();
    private String[] data;

    private Boolean primera = true;
    private boolean switcher = true;
    /**
     * The port we're normally going to use.
     */
    private static final String PORT_NAMES[] = {
            "/dev/tty.usbserial-A9007UX1", // Mac OS X
            "/dev/ttyACM0", // Raspberry Pi
            "/dev/ttyUSB0", // Linux
            "COM3", // Windows
    };
    /**
     * A BufferedReader which will be fed by a InputStreamReader
     * converting the bytes into characters
     * making the displayed results codepage independent
     */
    private BufferedReader input;
    /**
     * The output stream to the port
     */
    private OutputStream output;
    /**
     * Milliseconds to block while waiting for port open
     */
    private static final int TIME_OUT = 2000;
    /**
     * Default bits per second for COM port.
     */
    private static final int DATA_RATE = 9600;

    public void initialize() {
        // http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
        // La siguiente linea solo se debe poner con la raspberry pi
//        System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");

        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        //First, Find an instance of serial port as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            for (String portName : PORT_NAMES) {
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;
                    break;
                }
            }
        }
        if (portId == null) {
            System.out.println("No se encuentra el puerto COM.");
            return;
        }

        try {
            // open serial port, and use class name for the appName.
            serialPort = (SerialPort) portId.open(this.getClass().getName(),
                    TIME_OUT);

            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            // open the streams
            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            output = serialPort.getOutputStream();

            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }

    /**
     * Se llama cuando se deje usar el puerto
     * Esto evita que se bloqueen los puertos.
     */
    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    /**
     * Esto es lo que lee los datos (maneja los eventos)
     */
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                String line = input.readLine();
//               TODO: Anexar una 'a' y 'b' (u otra señal) al codigo para reconocimiento
                if (line.charAt(0) == 'a') {
//                    TODO: hacer get al servidor para comparar el codigo
                    if (true) {
//                    TODO: Si no hay conexion revisar copia interna
                    }
                } else if (line.charAt(0) == 'b') {
//                    TODO: Almacenar codigo "codificado" en un txt en la raspberry pi
//                    TODO: Hacer post mandado el codigo a la API
                } else {
                    data = line.split("/");
//                    TODO: procesar los datos
                }

            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        SerialReader main = new SerialReader();
        main.initialize();
        Thread t = new Thread() {
            public void run() {
                //Esto es para que no se cierre la conexion
                try {
                    Thread.sleep(1000000);
                } catch (InterruptedException ie) {
                }
            }
        };
        t.start();
        System.out.println("Iniciado correctamente");
    }
}

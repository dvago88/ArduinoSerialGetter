package com.danielvargas.serial;

import com.danielvargas.entities.DataEntity;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
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
    private String url = "https://aqueous-temple-46001.herokuapp.com/";
//    private String url = "http://localhost:8090/";

    private Boolean primera = true;
    private boolean switcher = true;
    /**
     * The port we're normally going to use.
     */
   /* private static final String PORT_NAMES[] = {
            "/dev/tty.usbserial-A9007UX1", // Mac OS X
//            TODO: get all ports for the arduinos in the raspberry pi
            "/dev/ttyACM0", // Raspberry Pi
            "/dev/ttyUSB0", // Linux
            "COM3", // Windows
            "COM4", // Windows
    };*/
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

    //    TODO: Entregar solo un String al metodo en vez de un vector
    public void initialize(String portName) {
        // http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
        // La siguiente linea solo se debe poner con la raspberry pi
//        System.setProperty("gnu.io.rxtx.SerialPorts", portName);

        CommPortIdentifier portId = null;
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

        //First, Find an instance of serial port as set in PORT_NAMES.
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
                if (currPortId.getName().equals(portName)) {
                    portId = currPortId;
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
                String code;
                DataEntity dataEntity;
                String[] datos;
                switch (line.charAt(0)) {
//                    TODO: Anexar una 'a' y 'b' (u otra señal) al codigo arduino para reconocimiento
                    case 'a':
                        code = line.substring(1);
                        datos = input.readLine().split("/");
                        dataEntity = new DataEntity(
                                Integer.parseInt(datos[0]),
                                Integer.parseInt(datos[1])
                                , Integer.parseInt(datos[2]),
                                Integer.parseInt(datos[3]),
                                code
                        );
                        postReq.postData(url, dataEntity);
//                        TODO: refactorizar, evitar crear dataentity incesariamente
                        postReq.postData(url + "stations/" + datos[0], new DataEntity());
                        if (postReq.getResponseCode() != 201) {
//                            TODO: agregar marca para saber que no llegó al servidor
                        }
//                        TODO: Almacenar codigo "codificado" en un txt en la raspberry pi
                        break;
                    case 'b':
                        code = line.substring(1);
                        datos = input.readLine().split("/");
                        try {
//                            TODO: Revisar primero que sí se pudo subir al servidor previamente
                            dataEntity = getReq.call_me(url + datos[0]);
                            if (dataEntity.getRfid().equals(code)) {
                                output.write(0);
                                postReq.postData(url + "stations/" + datos[0], new DataEntity());
                            } else {
                                output.write(1);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        break;
                    case 'd':
                        data = line.split("/");
//                    TODO: procesar los datos
                        break;
                    default:
                        System.out.println("Se recibió esto: " + line);
                }
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        SerialReader main = new SerialReader();
        main.initialize("COM3");
//        main.initialize("/dev/ttyACM0");
        Thread secondPort = new Thread(() -> {
            SerialReader secondary = new SerialReader();
            secondary.initialize("COM4");
//            secondary.initialize("/dev/ttyACM1");
        });
        secondPort.start();
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

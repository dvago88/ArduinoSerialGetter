/*
package com.danielvargas.serial;

import com.danielvargas.entities.DataEntity;
import com.danielvargas.entities.Tiempo;
import com.danielvargas.rest.get.GetAvailabilityOfStation;
import com.danielvargas.rest.get.GetDataEntity;
import com.danielvargas.rest.get.GetUser;
import com.danielvargas.rfid.Interface;
import com.danielvargas.rfid.RfidListener;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.danielvargas.rest.*;


public class SerialReader implements SerialPortEventListener {
    //    TODO: crear constructor
    private SerialPort serialPort;
    private GetDataEntity getDataEntity = new GetDataEntity();
    private GetUser getUser = new GetUser();
    private GetAvailabilityOfStation isStationAvailable = new GetAvailabilityOfStation();
    //    private PostRequest postReq = new PostRequest();
    private PutRequest putReq = new PutRequest();
    private String[] data;
    private String url = "https://aqueous-temple-46001.herokuapp.com/";
//    private String url = "http://localhost:8090/";

    private Boolean primera = true;
    private boolean switcher = true;

    private Scanner scanner = new Scanner(System.in);

    */
/**
     * The port we're normally going to use.
     *//*

   */
/* private static final String PORT_NAMES[] = {
            "/dev/tty.usbserial-A9007UX1", // Mac OS X
//            TODO: get all ports for the arduinos in the raspberry pi
            "/dev/ttyACM0", // Raspberry Pi
            "/dev/ttyUSB0", // Linux
            "COM3", // Windows
            "COM4", // Windows
    };*//*

    */
/**
     * A BufferedReader which will be fed by a InputStreamReader
     * converting the bytes into characters
     * making the displayed results codepage independent
     *//*

    private BufferedReader input;
    */
/**
     * The output stream to the port
     *//*

    private OutputStream output;
    */
/**
     * Milliseconds to block while waiting for port open
     *//*

    private static final int TIME_OUT = 2000;
    */
/**
     * Default bits per second for COM port.
     *//*

    private static final int DATA_RATE = 9600;

    //    TODO: Entregar solo un String al metodo en vez de un vector
    public void initialize(String portName) {
        // http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
        // La siguiente linea solo se debe poner con la raspberry pi
        System.setProperty("gnu.io.rxtx.SerialPorts", portName);

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

    */
/**
     * Se llama cuando se deje usar el puerto
     * Esto evita que se bloqueen los puertos.
     *//*

    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    */
/**
     * Esto es lo que lee los datos (maneja los eventos)
     *//*

//    TODO: Refactorizar (metodo demasido largo y creciendo)
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        PostRequest postReq = new PostRequest();
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
                        long userId = getUser.makeRequest(url + "user/" + code);
                        if (userId == -1) {
                            System.out.println("Usuario no registrado");
                            output.write(0);
                            break;
                        }
//                        Aca en vez de scanner se usa un GUI para que el usuario ponga su elección
                        int estacionCodigo = scanner.nextInt();
                        int estacion = convertidor(estacionCodigo);
                        int res = isStationAvailable.makeRequest(url + "stations/" + estacion);
                        if (res == -1) {
                            System.out.println("Estación inexistente");
                            output.write(0);
                            break;
                        } else if (res == 0) {
                            int toSend = estacionCodigo * 10;
                            System.out.println("mando esto " + toSend);
                            output.write(toSend);
                            try {
//                            TODO: Revisar primero que sí se pudo subir al servidor previamente
                                dataEntity = getDataEntity.makeRequest(url + estacion);
                                if (dataEntity.getRfid().equals(code)) {
                                    postReq.postData(url + "stations/" + estacion, new DataEntity());
                                    Tiempo tiempo = new Tiempo();
//                                  TODO: hacer algo si no se pudo conectar con el servidor para enviar el historial
//                                    postReq.postData(url + "historial/" + estacion + "/" + code, tiempo);
                                    output.write(11);
                                } else {
                                    output.write(0);
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }

                        } else {
                            output.write(estacionCodigo);
                        }
                        break;
                    case 'b':
                        code = line.substring(1);
                        datos = input.readLine().split("/");
                        dataEntity = new DataEntity(
                                convertidor(Integer.parseInt(datos[0])),
                                (int) Math.round(Double.parseDouble(datos[1])),
                                Integer.parseInt(datos[2]),
                                Integer.parseInt(datos[3]),
                                code
                        );
                        Tiempo tiempo = new Tiempo();

                        postReq.postData(url + "historial/" + dataEntity.getStationNumber() + "/" + code, tiempo);
//                        Si el usuario no existe en la base de datos lo rechaza:
                        if (postReq.getResponseCode() == 401) {
                            output.write(0);
                            break;
                        } else if (postReq.getResponseCode() == 201) {
                            postReq.postData(url, dataEntity);
//                        TODO: refactorizar, evitar crear dataentity incesariamente
                            postReq.postData(url + "stations/" + dataEntity.getStationNumber(), new DataEntity());
                            if (postReq.getResponseCode() != 201 && postReq.getResponseCode() != 202) {
//                            TODO: agregar marca para saber que no llegó al servidor
                            }
//                        TODO: Almacenar codigo "codificado" en un txt en la raspberry pi
                            output.write(1);
                            break;
                        }
                        break;
                    case 'd':
                        data = line.split("/");
//                    TODO: procesar los datos
                        break;
                    case 'z':
//                        TODO: Hacer algo cuando los sensores perciban cambios drasticos
                        System.out.println("Ha habido un cambio en la distancia");
                        System.out.println("La nueva distancia es: " + line.substring(1));
                        break;
                    default:
                        System.out.println("O.o Se recibió esto: " + line);
                }
            } catch (Exception e) {
                System.err.println(e.toString());
            }
        }
    }

    private int convertidor(int n) {
        switch (n) {
            case 10:
                return 1;
            case 12:
                return 2;
            case 13:
                return 3;
            case 14:
                return 4;
            case 15:
                return 5;
            default:
                return 1;
        }
    }

    //    TODO: Crear clase Main para esta parte:
    public static void main(String[] args) throws Exception {
        SerialReader main = new SerialReader();
//        main.initialize("COM7");
        main.initialize("/dev/ttyACM0");

        Thread rfid = new Thread(() -> {
            Interface.init("/home/pi/MFRC522-python/Read.py", "/home/pi/MFRC522-python/Write.py");

            System.out.println("Reader test:");
            try {
                RfidListener reader = new RfidListener();
                reader.read();

                System.out.println("Id: " + reader.getId());
                System.out.println("Content: " + reader.getContent());
            } catch (IOException ioe) {
                ioe.getStackTrace();
            }
        });
        rfid.start();
        Thread secondPort = new Thread(() -> {
            SerialReader secondary = new SerialReader();
//            secondary.initialize("COM4");
            secondary.initialize("/dev/ttyUSB0");
        });
//        secondPort.start();
        Thread thirdPort = new Thread(() -> {
            SerialReader third = new SerialReader();
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
*/

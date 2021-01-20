/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.SQLException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.sql.Time;
import java.time.LocalTime;

/**
 *
 * @author emili
 */
public class main {

    
  public final static int SERVICE_PORT=5005;
    public static void main(String[] args) throws IOException, SQLException {
        // TODO code application logic here
        timebd query = new timebd();
        
        try{
            // Instantiate a new DatagramSocket to receive responses from the client
            
            for(;;){
                DatagramSocket serverSocket = new DatagramSocket(SERVICE_PORT);
                /* Create buffers to hold sending and receiving data.
                It temporarily stores data in case of communication delays */
                byte[] receivingDataBuffer = new byte[1024];
                byte[] sendingDataBuffer = new byte[1024];
                /* Instantiate a UDP packet to store the 
                client data using the buffer for receiving data*/
                DatagramPacket inputPacket = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);
                // Receive data from the client and store in inputPacket
                serverSocket.receive(inputPacket);
                String receivedData = new String(inputPacket.getData());
                //System.out.println("ORDEN DEL CLIENTE: "+receivedData);
                InetAddress ip = inputPacket.getAddress();
                //System.out.println("LA DIRECCION ES: "+ ip.toString());
                // Printing out the client sent data
                receivedData = new String(inputPacket.getData());
                String hora = LocalDateTime.now(Clock.systemUTC()).toString(); // se obtiene la hora
                String horaUTC = hora.substring(11, hora.length()-1);
                // Create new UDP packet with data to send to the client
                System.out.println("Recibí: "+receivedData + " se envía: " + horaUTC + " --> " + inputPacket.getPort());
                DatagramPacket outputPacket = new DatagramPacket(
                  horaUTC.getBytes(), horaUTC.getBytes().length,
                  inputPacket.getAddress(),inputPacket.getPort()
                );
                // Send the created packet to client
                sendingDataBuffer = horaUTC.getBytes();
                serverSocket.send(outputPacket);
                inputPacket.getPort();
                // Close the socket connection
                serverSocket.close();
                //System.out.println(receivedData.toString());
                //System.out.println(horaUTC);
                query.saveHoraCentral(receivedData.substring(0, 8), horaUTC.substring(0, 8));
                // se almacena en la tabla hora_central
                String msprev = receivedData.substring(9, 14);
                String msref = horaUTC.substring(9, 14);
                int latencia = Integer.parseInt(msref)-Integer.parseInt(msprev);
                String strLatencia = String.valueOf(latencia);
                query.saveEquipos(inputPacket.getAddress().toString(), String.valueOf(inputPacket.getPort()), strLatencia);
                //se almacena en la tabla equipos
                String lastHoraCentral = query.getHoraCentral();
                //se consultan los datos de hora_central, obteniendo el id para almecenarlo en horaEquipos
                //System.out.println("Último registro: " + lastHoraCentral);
                String[] partsHoraCentral = lastHoraCentral.split(",");
                String lastLatencia = query.getEquipo(Integer.parseInt(partsHoraCentral[0]));
                //se consultan los datos de equipo, obteniendo el id y latencia para almecenarlo en horaEquipos
                String[] partsLatencia = lastLatencia.split(",");
                query.saveHoraEquipos(Integer.parseInt(partsHoraCentral[0]), Integer.parseInt(partsLatencia[0]), partsHoraCentral[1], partsHoraCentral[2]);
                //se almacenan los datos en la tabla horaEquipos
            }
          }
          catch (SocketException e){
              System.out.println("Error!");
              System.out.println(e);
          }
    }
    
}

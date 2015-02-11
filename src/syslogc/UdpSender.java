package syslogc;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

class UdpSender implements Runnable {
    private static final int FAILURE_TIMEOUT = 5000;

    private final SyslogClient log;
    private DatagramSocket socket;


    public UdpSender(SyslogClient log) {
        this.log = log;
    }

    @Override
    public void run() {
        while (log.open) {
            try {
                Message message = log.blockingQueue.take();
                send(message);
            } catch (InterruptedException ignored) {
            }
        }

        for (Message message : log.blockingQueue) {
            send(message);
        }
        release();
    }

    private void send(Message message) {
        try {
            if (socket == null) {
                socket = new DatagramSocket();
            }
            DatagramPacket packet = new DatagramPacket(message.getValue(), message.getValue().length, log.address, log.port);
            socket.send(packet);
        } catch (IOException t) {
            System.out.println("Syslog udpsender exception");
            t.printStackTrace();

            release();
            try {
                Thread.sleep(FAILURE_TIMEOUT);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private void release() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Throwable ignored) {
        }
        socket = null;
    }

}

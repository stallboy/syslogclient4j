package syslogc;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public final class SyslogClient {
    volatile boolean open = true;
    volatile Thread worker;
    volatile InetAddress address;
    volatile int port;
    BlockingQueue<Message> blockingQueue = new ArrayBlockingQueue<>(40960);

    public void open(String hostname, int port, Transport transport) throws UnknownHostException {
        this.address = InetAddress.getByName(hostname);
        this.port = port;
        open = true;

        switch(transport){
            case UDP:
                worker = new Thread(new UdpSender(this));
                break;
            case TCP:
                worker = new Thread(new TcpSender(this));
                break;
        }
        worker.start();

    }

    public boolean log(Message message) {
        return open && blockingQueue.offer(message);
    }

    public void close() {
        open = false;
        worker.interrupt(); //interrupt blockingqueue.take

        while (true) {
            try {
                worker.join();
                break;
            } catch ( Throwable ex ) {
                System.out.println("Syslog close. ignore ex=" + ex);
            }
        }
    }

}

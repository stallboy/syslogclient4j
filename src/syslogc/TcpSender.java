package syslogc;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;


class TcpSender implements Runnable {
    private static final int FAILURE_TIMEOUT = 5000;

    private final SyslogClient log;

    private Socket socket;
    private OutputStream os;

    public TcpSender(SyslogClient log) {
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

        for (Message message : log.blockingQueue){
            send(message);
        }
        release();
    }

    private void send(Message message)  {
        try {
            if (os == null) {
                socket = new Socket(log.address, log.port);
                os = socket.getOutputStream();
            }
            os.write(message.getValue());
            os.write('\n');
        }catch (IOException t) {
            System.out.println("Syslog tcpsender exception");
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
            if (os != null) {
                os.flush();
            }
        } catch (Throwable ignored) {
        }

        try {
            if (os != null) {
                os.close();
            }
        } catch (Throwable ignored) {
        }
        os = null;

        try {
            if (socket != null) {
                socket.close();
            }
        } catch (Throwable ignored) {
        }
        socket = null;
    }
}

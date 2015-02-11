package syslogc;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    private static final SimpleDateFormat syslogDateFormat = new SimpleDateFormat("MMM d HH:mm:ss");
    private static final String charset = "UTF-8";
    private static String hostname;
    private static String program;
    private static int pid;

    static {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        String name = runtime.getName();
        String[] names = name.split("@");
        if (names.length == 2) {
            pid = Integer.parseInt(names[0]);
            hostname = names[1];
        }

        if (hostname == null) { //lazy to get by connect
            try {
                hostname = InetAddress.getLocalHost().getHostName();
            } catch (UnknownHostException ignored) {
            }

            if (hostname == null) {
                try {
                    hostname = Inet4Address.getLocalHost().getHostAddress();
                } catch (UnknownHostException ignored) {
                }

                if (hostname == null){
                    hostname = "unknown";
                }
            }
        }

        String path = Message.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String[] paths = path.split("/");
        if (paths.length > 0) {
            program = paths[paths.length - 1];
            if (program.endsWith(".jar")) {
                program = program.substring(0, program.length() - 4);
            }
        }

        if (program == null) {
            program = "unknown";
        }
    }

    private byte[] value;
    public Message(Facility facility, Severity severity, String message, Throwable e) {
        String v = makePri(facility, severity) + ' ' + makeTimestamp() + ' ' + hostname + ' ' + program + '[' + pid + "]: " + message + makeThrow(e);
        try {
            value = v.getBytes(charset);
        } catch (UnsupportedEncodingException ignored) {
        }
    }

    public byte[] getValue() {
        return value;
    }

    public static String makePri(Facility facility, Severity severity) {
        return String.format("<%d>", (facility.getId() << 3) + severity.getLevel());
    }

    public static String makeTimestamp() {
        return syslogDateFormat.format(new Date());
    }

    public static String makeThrow(Throwable e) {
        if (e == null)
            return "";

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println();
        e.printStackTrace(pw);
        pw.close();
        return sw.toString();
    }

    @Override
    public String toString() {
        try {
            return new String(value, charset);
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
}

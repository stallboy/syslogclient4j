package syslogc;

/**
 * Enumeration of facilities according to RFC 3164.
 */
public enum Facility {
    KERN(0), // Kernel messages
    USER(1), // user-level messages
    MAIL(2), // mail system
    DAEMON(3), // system daemons
    AUTH(4), // security/authorization messages (note 1)
    SYSLOG(5), // messages generated internally by syslogd
    LPR(6), // line printer subsystem
    NEWS(7), // network news subsystem
    UUCP(8), // UUCP subsystem
    CRON(9), // clock daemon (note 2)
    SECURITY(10), // security/authorization messages (note 1)
    FTP(11), // FTP daemon
    NTP(12), // NTP subsystem
    LOGAUDIT(13), // log audit (note 1)
    LOGALERT(14), // log alert (note 1)
    CLOCK(15), // clock daemon (note 2)
    LOCAL0(16), // local use 0  (local0)
    LOCAL1(17), // local use 1  (local1)
    LOCAL2(18), // local use 2  (local2)
    LOCAL3(19), // local use 3  (local3)
    LOCAL4(20), // local use 4  (local4)
    LOCAL5(21), // local use 5  (local5)
    LOCAL6(22), // local use 6  (local6)
    LOCAL7(23); // local use 7  (local7)

    private int id;

    Facility(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

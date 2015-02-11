package syslogc;

/*
Enumeration for severity according to RFC 3164
 */
public enum Severity {

    // RFC 3164
    EMERGENCY(0), // System is unusable
    ALERT(1), // Action must be taken immediately
    CRITICAL(2), // Critical conditions
    ERROR(3), // Error conditions
    WARNING(4), // Warning conditions
    NOTICE(5), // Normal but significant condition
    INFO(6), // Informational messages
    DEBUG(7); // Debug-level messages

    private int level;

    Severity(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}

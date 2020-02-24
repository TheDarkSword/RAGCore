package it.revarmygaming.commonapi;

public enum LoggerMode {
    INFO("info"),
    WARNING("warning"),
    SEVERE("severe");

    private String mode;

    LoggerMode(String mode) {
        this.mode = mode;
    }

    public String get() {
        return mode;
    }
}

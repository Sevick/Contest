package com.fbytes.contest.Contest.Logger;

public interface ILogger {

    public enum Severity { err, warn, info, debug, trace};

    void logException(Exception e);
    void logException(String msg, Exception e);
    void log(Severity severity, String msg);
}

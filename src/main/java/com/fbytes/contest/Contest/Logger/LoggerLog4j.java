package com.fbytes.contest.Contest.Logger;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class LoggerLog4j implements ILogger {

    protected final static Logger logger = LogManager.getLogger(LoggerLog4j.class); //LoggerFactory.getLogger(LoggerLog4j.class);

    @Override
    public void logException(Exception e) {
        log(Severity.err, String.format("%s\n%s", e.getMessage(), ExceptionUtils.getStackTrace(e)));
    }

    @Override
    public void logException(String msg, Exception e) {
        log(Severity.err, String.format("%s\n%s", msg + "\n" + e.getMessage(), ExceptionUtils.getStackTrace(e)), 2);
    }

    @Override
    public void log(Severity severity, String msg) {
        log(severity, msg, 2);
    }


    private void log(Severity severity, String msg, int stackIndex) {

        String calledFrom = "";

        try {
            throw new RuntimeException();
        } catch (Exception e) {
            StackTraceElement[] stackTrace = e.getStackTrace();
            calledFrom = stackTrace[stackIndex].toString();
        }

        Logger logger = LogManager.getLogger(calledFrom);
        switch (severity) {
            case err:
                logger.error(String.format(msg));
                break;
            case warn:
                logger.warn(String.format(msg));
                break;
            case info:
                logger.info(String.format(msg));
                break;
            case debug:
                logger.debug(String.format(msg));
                break;
        }
    }
}

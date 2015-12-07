package com.mewa.utils.loggers;

import com.mewa.utils.i.IClock;
import com.mewa.utils.i.Logger;
import com.mewa.utils.i.OutputStream;

import java.util.Arrays;

public class DebugLogger implements Logger {
    private OutputStream stream;
    private IClock clock;
    private int level;

    public DebugLogger(OutputStream stream, IClock clock) {
        this.stream = stream;
        this.clock = clock;
        this.level = DEBUG;
    }

    @Override
    public void log(int level, String fmt, Object... msg) {
        if (this.level > level)
            return;
        char lvlChar;
        switch (level) {
            case INFO:
                lvlChar = 'I';
                break;
            case DEBUG:
                lvlChar = 'D';
                break;
            case WARN:
                lvlChar = 'W';
                break;
            case ERROR:
                lvlChar = 'E';
                break;
            default:
                lvlChar = 'V';
                break;
        }
        stream.writeToStream(
                String.format(
                        "%c/ %d\t%s", lvlChar, clock.time(),
                        String.format(fmt, msg)
                )
        );
    }

    @Override
    public void write(String msg) {
        log(VERBOSE, msg);
    }

    @Override
    public void setLogLevel(int level) {
        this.level = level;
    }

}

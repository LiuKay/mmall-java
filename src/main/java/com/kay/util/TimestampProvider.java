package com.kay.util;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.springframework.stereotype.Component;

@Component
public class TimestampProvider {

    public String getTimestampAsString() {
        return currentTimestamp().toString();
    }

    public ZonedDateTime getTimestampWithSecondsOffset(long secondsOffset) {
        return currentTimestamp().plusSeconds(secondsOffset);
    }

    private ZonedDateTime currentTimestamp() {
        return ZonedDateTime.now(ZoneOffset.systemDefault());
    }
}

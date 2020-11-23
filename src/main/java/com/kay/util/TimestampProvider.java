package com.kay.util;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

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

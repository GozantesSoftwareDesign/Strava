package org.gozantes.strava.server.data.domain.session;

import org.w3c.dom.ranges.RangeException;

import java.util.Arrays;

public enum SessionState {
    CANCELLED((short) 0),
    IN_PROGRESS((short) 1),
    COMPLETED((short) 2);

    public short value;

    private SessionState(short value) {
        this.value = value;
    }

    public static SessionState fromValue(short value) throws RangeException {
        SessionState[] v = SessionState.values();
        if (value < 0 || value > v.length - 1)
            throw new RangeException(RangeException.BAD_BOUNDARYPOINTS_ERR, Short.toString(value) + " not in " + Arrays.toString(new short[]{0, (short) (v.length - 1)}) + ".");

        return SessionState.values()[value];
    }

    public short toValue() {
        return this.value;
    }
}

package com.dynamicperception.nmxcommandline.helpers;

import java.awt.Color;

public final class Consts {

    // Motor constants
    public static final int    MOTOR_COUNT      = 3;
    public static final int    SLIDE            = 0;
    public static final int    PAN              = 1;
    public static final int    TILT             = 2;
    public static final int    COMPOSITE        = 3;

    // Distance constants
    public static final int    DEG_PER_ROT      = 360;
    public static final float  CM_PER_INCH      = 2.54f;
    public static final float  INCH_PER_CM      = (1f / CM_PER_INCH);

    // Timing constants
    public static final double MICROS_PER_SEC   = 1e6;
    public static final int    MILLIS_PER_SEC   = 1000;
    public static final int    MILLIS_PER_FRAME = 1000;
    public static final int    SEC_PER_MIN      = 60;
    public static final int    MIN_PER_HOUR     = 60;
    public static final int    MILLIS_PER_MIN   = MILLIS_PER_SEC * SEC_PER_MIN;
    public static final int    MILLIS_PER_HOUR  = MILLIS_PER_SEC * SEC_PER_MIN * MIN_PER_HOUR;

    // Microstepping constants
    public static final int    FINE             = 16;
    public static final int    MED              = 8;
    public static final int    COARSE           = 4;

    // Communication constants
    public static final int    BYTE_SIZE        = 1;
    public static final int    INT_SIZE         = 2;
    public static final int    LONG_SIZE        = 4;
    public static final int    FLOAT_SIZE       = 4;

    // Colors
    public static class Colors {
        public static final Color PURPLE       = new Color(130, 11, 227, 200);
        public static final Color CYAN         = new Color(0, 225, 255);
        public static final Color GRAFFIK_GRAY = new Color(45, 41, 38);
        public static final Color CLOSED_PORT  = new Color(220, 20, 60);
        public static final Color OPEN_PORT    = new Color(0, 128, 0);
    }

    // Run States
    public static class NMXState {
        public static final int INVALID = -1;
        public static final int STOPPED = 0;
        public static final int RUNNING = 1;
        public static final int PAUSED  = 2;
    }

    // Error codes
    public static final int   ERROR            = (int) -1e9;

    // NMX Conversion Factors
    public static final float FLOAT_CONVERSION = 100f;
}

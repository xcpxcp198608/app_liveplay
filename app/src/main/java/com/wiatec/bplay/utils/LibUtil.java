package com.wiatec.bplay.utils;


import org.videolan.libvlc.LibVLC;

import java.util.ArrayList;

/**
 *
 */
public class LibUtil {
    private static LibVLC libVLC = null;

    public synchronized static LibVLC getLibVLC(ArrayList<String> options) throws IllegalStateException {
        if (libVLC == null) {
            if (options == null) {
                libVLC = new LibVLC();
            } else {
                libVLC = new LibVLC(options);
            }
        }
        return libVLC;
    }
}
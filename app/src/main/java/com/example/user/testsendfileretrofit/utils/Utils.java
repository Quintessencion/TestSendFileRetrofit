package com.example.user.testsendfileretrofit.utils;

import android.content.ContentResolver;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {

    public static byte[] readBytesFromUri(Uri uri, ContentResolver contentResolver) {
        byte[] output = null;
        try {
            InputStream in = contentResolver.openInputStream(uri);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }

            output = out.toByteArray();

            out.close();
            in.close();
        } catch (IOException | NullPointerException io) {
            io.printStackTrace();
        }

        return output;
    }
}

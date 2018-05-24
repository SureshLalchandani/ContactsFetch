package com.citrix.citrixcontacts.Helper;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utility Class to load JSON file and extract data
 */
public class FileHelper {

    public static final int PICKFILE_RESULT_CODE = 100;

    public static String loadJSONFromAsset(Context context, Uri uri) {
        String json = null;

        InputStream is = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

}

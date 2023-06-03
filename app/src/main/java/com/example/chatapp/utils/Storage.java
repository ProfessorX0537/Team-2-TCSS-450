package com.example.chatapp.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Storage {

    /**
     * Write an Serializable Object to App-specific storage
     *
     * @param name         name of file
     * @param serializable object to save
     * @param ctx          the Activity
     * @return boolean of read success
     */
    public static boolean saveSerializable(String name, Object serializable, Context ctx) {
        try {
            FileOutputStream fOut = ctx.openFileOutput(name, Context.MODE_PRIVATE);
            ObjectOutputStream oOut = new ObjectOutputStream(fOut);
            oOut.writeObject(serializable);
            oOut.close();
            fOut.close();
            Log.d("Storage", "Written " + name);
            return true;
        } catch (Exception e) {
            Log.e("Storage", "ERROR: Failed saving " + name);
            return false;
        }
    }

    /**
     * Write an Object from App-specific storage
     *
     * @param name name of file
     * @param ctx  the Activity
     * @return Object that was deserialized
     */
    public static Object loadSerializable(String name, Context ctx) {
        try {
            File f = new File(ctx.getFilesDir(), name);
            FileInputStream fIn = new FileInputStream(f);
            Log.d("Storage", "loadSerializable: " + f.getAbsolutePath());
            ObjectInputStream oIn = new ObjectInputStream(fIn);
            Object result = oIn.readObject();
            oIn.close();
            Log.d("Storage", "loaded:" + name + " " + result.toString());
            return result;
        } catch (Exception e) {
            Log.e("Storage", "ERROR: Failed loading " + name);
            return null;
        }
    }

    public static boolean delete(String name, Context ctx) {
        try {
            File f = new File(ctx.getFilesDir(), name);
            if (f.isFile()) {
                f.delete();
            }
            Log.d("Storage", "Deleted (or did not exist):" + name);
            return true;
        } catch (Exception e) {
            Log.e("Storage", "ERROR: Failed deleting " + name);
            return false;
        }
    }
}

package com.schieferkoenigs.activitywecker.util;

import android.content.Context;

import com.schieferkoenigs.activitywecker.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MyFiles {

    /**
     * Liefert den Text mit der Weckzeit aus der Datei TIME_FILE_NAME
     * Falls nicht vorhanden erfolgt Aufforderung einen Alarm hinzuzufügen
     *
     * @param context
     * @return ausgelesene Weckzeit
     */
    public static String getTimeFromFile(Context context) {
        File file = new File(context.getFilesDir(), Constants.TIME_FILE_NAME);
        if (file.exists()) {
            int length = (int) file.length();

            byte[] bytes = new byte[length];

            try (FileInputStream in = new FileInputStream(file)) {
                in.read(bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return new String(bytes);
        }
        return Constants.TIME_DEFAULT_TEXT;
    }

    /**
     * Liefert den Klingelton zum Wecken aus der Datei RINGTONE_FILE_NAME
     * Falls nicht vorhanden wird ein Standardton geliefert
     *
     * @param context
     * @return Name des Klingeltons
     */
    public static String getRingtoneFromFile(Context context) {
        File file = new File(context.getFilesDir(), Constants.RINGTONE_FILE_NAME);
        if (file.exists()) {
            int length = (int) file.length();

            byte[] bytes = new byte[length];

            try (FileInputStream in = new FileInputStream(file)) {
                in.read(bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String ergebnis = new String(bytes);
            System.out.println("getRingtoneFromFile:" + ergebnis);
            return ergebnis;
        }

        return Constants.RINGTONE_DEFAULT; // Default-Ton
    }

    /**
     * Erstellt eine Datei mit dem ausgewählten Klingelton
     *
     * @param context
     * @param ringtone
     */
    public static void saveRingtone(Context context, String ringtone) {
        File file = new File(context.getFilesDir(), Constants.RINGTONE_FILE_NAME);
        try (FileOutputStream stream = new FileOutputStream(file)) {
            System.out.println("selectedItem = " + ringtone);
            stream.write(ringtone.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Erstellt eine Datei mit der Weckzeit(als String) als Inhalt
     *
     * @param context
     * @param time
     */
    public static void saveTime(Context context, String time) {
        File file = new File(context.getFilesDir(), Constants.TIME_FILE_NAME);
        try (FileOutputStream stream = new FileOutputStream(file)) {
            stream.write(time.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Liest automatisch die Dateien aus dem Ordner raw aus, und bildet einen
     * Array aus deren Namen als String
     *
     * @param context
     * @return
     */
    public static List<String> getRingtonesList(Context context) {
        List<String> tones = new ArrayList<>();
        Field[] fields = R.raw.class.getFields();
        for (Field f : fields)
            tones.add(f.getName());
        return tones;
    }
}

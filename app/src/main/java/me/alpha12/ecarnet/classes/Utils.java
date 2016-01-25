package me.alpha12.ecarnet.classes;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.Arrays;

public class Utils {
    public static final String[] ROMAN_DIGITS = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};

    public static String ucFirst(String str) {
        return str.substring(0, 1).toUpperCase()+ str.substring(1).toLowerCase();
    }

    public static String ucWords(String str) {
        String[] array = str.split(" ");

        for (int i = 0; i < array.length; i++) {
            if (!Arrays.asList(ROMAN_DIGITS).contains(array[i]))
                array[i] = ucFirst(array[i]);
        }

        return TextUtils.join(" ", array);
    }

    public static String toRoman(int value) {
        if (value > 0 && value < 10)
            return ROMAN_DIGITS[value];

        return String.valueOf(value);
    }

    public static String getNFCTagMessage(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
            String message = "";

            NdefMessage[] messages = null;
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                messages = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    messages[i] = (NdefMessage) rawMsgs[i];
                }
            }

            if (messages[0] != null) {
                byte[] payload = messages[0].getRecords()[0].getPayload();
                for (int b = 0; b < payload.length; b++) {
                    message += (char) payload[b];
                }
            }

            if (message.isEmpty())
                return null;
            else
                return message;
        } else {
            return null;
        }
    }
}

package com.toro.helper.utils;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import com.orhanobut.hawk.Hawk;

import static com.toro.helper.app.AppConfig.KEY_TONE_PHOTO;

public class RingToneUtil {

    public static void play(Context context){
        if (!Hawk.get(KEY_TONE_PHOTO,false)){
            return;
        }
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone rt = RingtoneManager.getRingtone(context,uri);
        if (!rt.isPlaying()){
            rt.play();
        }
    }
}

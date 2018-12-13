package kamen.ladysaga.com.versionmanager.main;

import android.content.DialogInterface;
import android.os.Bundle;

import kamen.ladysaga.com.versionmanager.callback.DialogDismissListener;
import kamen.ladysaga.com.versionmanager.core.VersionDialogActivity;

/**
 * Created by SK on 2017/10/10.
 */

public class CustomVersionDialogActivity extends VersionDialogActivity implements DialogDismissListener {
    public static boolean isForceUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDialogDimissListener(this);
    }

    @Override
    public void dialogDismiss(DialogInterface dialog) {
        if(isForceUpdate) {
            System.exit(0);
        }
    }
}

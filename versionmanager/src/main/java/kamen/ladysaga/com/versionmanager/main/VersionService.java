package kamen.ladysaga.com.versionmanager.main;

import kamen.ladysaga.com.versionmanager.R;
import kamen.ladysaga.com.versionmanager.core.AVersionService;

/**
 * Created by SK on 2017/10/10.
 */

public class VersionService extends AVersionService {
    @Override
    public void onResponses(AVersionService service, String response) {
        try{
            if(VersionManager.getInstance().getVersionInfo() != null) {
                showVersionDialog(VersionManager.getInstance().getVersionInfo().getDownloadUrl(),getResources().getString(R.string.check_new_version), VersionManager.getInstance().getVersionInfo().getDesc());
            }
        }catch (Exception e) {

        }

    }
}

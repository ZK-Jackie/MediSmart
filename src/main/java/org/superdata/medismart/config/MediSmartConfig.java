package org.superdata.medismart.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.FileSystems;

@Component
public class MediSmartConfig {
    public static String PROFILE;

    @Value("${settings.profile}:/upload")
    public void setProfile(String profile) {
        if(profile.charAt(profile.length() - 1) != FileSystems.getDefault().getSeparator().charAt(0)){
            profile += FileSystems.getDefault().getSeparator().charAt(0);
        }
        PROFILE = profile;
    }
}

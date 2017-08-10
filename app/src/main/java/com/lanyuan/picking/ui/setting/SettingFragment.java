package com.lanyuan.picking.ui.setting;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.lanyuan.picking.R;
import com.lanyuan.picking.config.AppConfig;
import com.lanyuan.picking.util.SPUtils;

import java.io.File;
import java.io.IOException;

public class SettingFragment extends PreferenceFragment {

    private File noMedia;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_screen);

        EditTextPreference downloadPath = (EditTextPreference) findPreference(getResources().getString(R.string.download_path));
        downloadPath.setSummary((String) SPUtils.get(getActivity(), AppConfig.download_path, AppConfig.DOWNLOAD_PATH));

        noMedia = new File((String) SPUtils.get(getActivity(), AppConfig.download_path, AppConfig.DOWNLOAD_PATH) + File.separatorChar + ".nomedia");
        SwitchPreference noMediaSwitch = (SwitchPreference) findPreference(AppConfig.hide_pic);
        if (noMedia.exists())
            noMediaSwitch.setChecked(true);
        else
            noMediaSwitch.setChecked(false);

        // Fresco.getImagePipelineFactory().getMainFileCache().trimToMinimum();
        float size = (float) Fresco.getImagePipelineFactory().getMainFileCache().getSize() / ByteConstants.MB;
        EditTextPreference cacheSize = (EditTextPreference) findPreference(getResources().getString(R.string.cache_size));
        cacheSize.setSummary(String.format("已使用 %.2f MB", size));
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        Log.e("SettingFragment", "onPreferenceTreeClick: " + preference.getKey());
        if (preference.getKey().equals(AppConfig.hide_pic)) {
            SwitchPreference switchPreference = (SwitchPreference) preference;
            if (switchPreference.isChecked()) {
                if (!noMedia.exists()) {
                    Log.e("SettingFragment", "onPreferenceTreeClick: exists");
                    try {
                        noMedia.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Log.e("SettingFragment", "onPreferenceTreeClick: no exists");
                if (noMedia.exists())
                    noMedia.delete();
            }
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}

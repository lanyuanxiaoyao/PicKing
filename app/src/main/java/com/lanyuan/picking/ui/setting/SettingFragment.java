package com.lanyuan.picking.ui.setting;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.lanyuan.picking.R;
import com.lanyuan.picking.config.AppConfig;
import com.lanyuan.picking.ui.detail.DetailActivity;
import com.lanyuan.picking.util.SPUtils;

public class SettingFragment extends PreferenceFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_screen);

        EditTextPreference downloadPath = (EditTextPreference) findPreference(getResources().getString(R.string.download_path));
        downloadPath.setSummary((String) SPUtils.get(getActivity(), AppConfig.download_path, AppConfig.DOWNLOAD_PATH));

        // Fresco.getImagePipelineFactory().getMainFileCache().trimToMinimum();
        float size = (float) Fresco.getImagePipelineFactory().getMainFileCache().getSize() / ByteConstants.MB;
        EditTextPreference cacheSize = (EditTextPreference) findPreference(getResources().getString(R.string.cache_size));
        cacheSize.setSummary(String.format("已使用 %.2f MB", size));
    }
}

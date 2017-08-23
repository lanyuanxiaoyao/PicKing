package com.lanyuan.picking.ui.setting;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.aigestudio.wheelpicker.WheelPicker;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.lanyuan.picking.R;
import com.lanyuan.picking.config.AppConfig;
import com.lanyuan.picking.util.SPUtils;
import com.lanyuan.picking.util.SnackbarUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class SettingFragment extends PreferenceFragment {

    private File noMedia;
    private EditTextPreference cacheSize;
    private int colsDetail;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference_screen);

        colsDetail = (int) SPUtils.get(getActivity(), AppConfig.cols_detail, 0);
        RadioPreference cols = (RadioPreference) findPreference(AppConfig.cols_detail);
        cols.setSummary("当前显示列数: " + (colsDetail + 1) + "\n图册页加载图片一般较大,同时显示多列对设备性能要求较高，请酌情选择");

        EditTextPreference downloadPath = (EditTextPreference) findPreference(getResources().getString(R.string.download_path));
        downloadPath.setSummary((String) SPUtils.get(getActivity(), AppConfig.download_path, AppConfig.DOWNLOAD_PATH));

        noMedia = new File((String) SPUtils.get(getActivity(), AppConfig.download_path, AppConfig.DOWNLOAD_PATH) + File.separatorChar + ".nomedia");
        SwitchPreference noMediaSwitch = (SwitchPreference) findPreference(AppConfig.hide_pic);
        if (noMedia.exists())
            noMediaSwitch.setChecked(true);
        else
            noMediaSwitch.setChecked(false);

        Fresco.getImagePipelineFactory().getMainFileCache().trimToMinimum();
        float size = (float) Fresco.getImagePipelineFactory().getMainFileCache().getSize() / ByteConstants.MB;
        cacheSize = (EditTextPreference) findPreference(getResources().getString(R.string.cache_size));
        cacheSize.setSummary(String.format("已使用 %.2f MB", size));
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, final Preference preference) {
        if (preference.getKey().equals(AppConfig.hide_pic)) {
            SwitchPreference switchPreference = (SwitchPreference) preference;
            if (switchPreference.isChecked()) {
                if (!noMedia.exists()) {
                    try {
                        noMedia.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (noMedia.exists())
                    noMedia.delete();
            }
        } else if (preference.getKey().equals(AppConfig.clean_cache)) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("注意")
                    .setMessage("清理缓存会导致所有图片重新加载，确定要清理缓存吗？")
                    .setPositiveButton("清理", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Fresco.getImagePipeline().clearCaches();
                            cacheSize.setSummary("已使用 0.00 MB");
                            SnackbarUtils.Short(getView(), "清理完成").info().show();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .create()
                    .show();
        } else if (preference.getKey().equals(AppConfig.cols_detail)) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_cols, null);
            final WheelPicker picker = ButterKnife.findById(view, R.id.cols_selector);
            List<String> cols = new ArrayList<String>() {{
                add("1");
                add("2");
                add("3");
                add("4");
            }};
            picker.setData(cols);
            picker.setSelectedItemPosition(colsDetail);
            new AlertDialog.Builder(getActivity())
                    .setTitle("选择列数")
                    .setView(view)
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            colsDetail = picker.getCurrentItemPosition();
                            preference.getEditor().putInt(AppConfig.cols_detail, colsDetail).commit();
                            preference.setSummary("当前显示列数: " + (colsDetail + 1) + "\n图册页加载图片一般较大,同时显示多列对设备性能要求较高，请酌情选择");
                        }
                    })
                    .create()
                    .show();
        }

        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
}

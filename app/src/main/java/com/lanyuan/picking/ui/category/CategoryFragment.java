package com.lanyuan.picking.ui.category;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lanyuan.picking.R;
import com.lanyuan.picking.config.AppConfig;
import com.lanyuan.picking.ui.contents.ContentsActivity;
import com.lanyuan.picking.pattern.BasePattern;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryFragment extends Fragment {

    @BindView(R.id.category_content)
    LinearLayout linearLayout;

    private View fragmentView;

    private String patternListName;

    private List<BasePattern> patternList;

    public CategoryFragment init(String patternListName) {
        this.patternListName = patternListName;
        return this;
    }

    @Override
    public void onResume() {
        if (linearLayout.getChildCount() == 0) {
            patternList = (List<BasePattern>) AppConfig.categoryList.get(patternListName);
            for (BasePattern pattern : patternList)
                linearLayout.addView(createImageView(pattern));
        }
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.category_fragment, container, false);
        ButterKnife.bind(this, fragmentView);
        return fragmentView;
    }

    private View createImageView(final BasePattern pattern) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.item_category, null);
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.category_layout);
        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.item_image);
        simpleDraweeView.setImageURI(Uri.parse(pattern.getCategoryCoverUrl()));
        relativeLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("跳转到")
                        .setMessage(pattern.getBaseUrl(null, 0))
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(pattern.getBaseUrl(null, 0)));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create()
                        .show();
                return true;
            }
        });
        relativeLayout.setBackgroundColor(pattern.getBackgroundColor());
        relativeLayout.setOnClickListener((v) -> {
            startActivity(pattern);
        });
        return view;
    }

    private void startActivity(BasePattern pattern) {
        Intent intent = new Intent(getContext(), ContentsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("pattern", pattern);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}

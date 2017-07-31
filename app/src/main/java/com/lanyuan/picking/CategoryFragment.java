package com.lanyuan.picking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lanyuan.picking.common.ContentsActivity;
import com.lanyuan.picking.pattern.custom.BasePattern;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryFragment extends Fragment {

    @BindView(R.id.category_content)
    LinearLayout linearLayout;

    private View fragmentView;

    private List<BasePattern> patternList;

    public CategoryFragment init(List<BasePattern> patternList) {
        Log.e("CategoryFragment", "init: 初始化");
        this.patternList = patternList;
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.category_fragment, container, false);
        ButterKnife.bind(this, fragmentView);

        for (BasePattern pattern : patternList)
            linearLayout.addView(createImageView(pattern));

        return fragmentView;
    }

    private View createImageView(final BasePattern pattern) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.item_category, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.item_image);
        imageView.setImageResource(pattern.getResourceId());
        imageView.setBackgroundColor(pattern.getBackgroundColor());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(pattern);
            }
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

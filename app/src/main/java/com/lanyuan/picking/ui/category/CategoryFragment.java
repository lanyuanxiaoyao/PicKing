package com.lanyuan.picking.ui.category;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lanyuan.picking.R;
import com.lanyuan.picking.ui.contents.ContentsActivity;
import com.lanyuan.picking.pattern.BasePattern;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoryFragment extends Fragment {

    @BindView(R.id.category_content)
    LinearLayout linearLayout;

    private View fragmentView;

    private List<BasePattern> patternList;

    public CategoryFragment init(List<BasePattern> patternList) {
        this.patternList = patternList;
        return this;
    }


    @Override
    public void onStart() {
        super.onStart();
        if (linearLayout.getChildCount() == 0)
            for (BasePattern pattern : patternList)
                linearLayout.addView(createImageView(pattern));
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
        relativeLayout.setBackgroundColor(pattern.getBackgroundColor());
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(pattern);
                // getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
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

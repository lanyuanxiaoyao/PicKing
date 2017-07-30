package com.lanyuan.picking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.lanyuan.picking.common.ContentsActivity;
import com.lanyuan.picking.pattern.BasePattern;
import com.lanyuan.picking.pattern.MM131.MM131Pattern;
import com.lanyuan.picking.pattern.RoseMM.RosiMMPattern;
import com.lanyuan.picking.pattern.XiuMM.XiuMMPattern;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindViews({R.id.mm131, R.id.xiumm, R.id.rosimm})
    List<ImageView> imageViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        for (ImageView imageView : imageViews)
            imageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mm131:
                startActivity(new MM131Pattern());
                break;
            case R.id.xiumm:
                startActivity(new XiuMMPattern());
                break;
            case R.id.rosimm:
                startActivity(new RosiMMPattern());
                break;
        }
    }

    private void startActivity(BasePattern pattern) {
        Intent intent = new Intent(MainActivity.this, ContentsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("pattern", pattern);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}

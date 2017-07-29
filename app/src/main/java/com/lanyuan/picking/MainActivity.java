package com.lanyuan.picking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.lanyuan.picking.pattern.MM131.MM131Activity;
import com.lanyuan.picking.pattern.RoseMM.RosiMMActivity;
import com.lanyuan.picking.pattern.XiuMM.XiuMMActivity;

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
                startActivity(new Intent(MainActivity.this, MM131Activity.class));
                break;
            case R.id.xiumm:
                startActivity(new Intent(MainActivity.this, XiuMMActivity.class));
                break;
            case R.id.rosimm:
                startActivity(new Intent(MainActivity.this, RosiMMActivity.class));
        }
    }
}

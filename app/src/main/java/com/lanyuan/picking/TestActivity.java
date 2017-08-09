package com.lanyuan.picking;

import android.net.Uri;
import android.os.Bundle;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lanyuan.picking.ui.BaseActivity;

public class TestActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        SimpleDraweeView simpleDraweeView = (SimpleDraweeView) findViewById(R.id.test_image);
        simpleDraweeView.setImageURI(Uri.parse("https://files.yande.re/sample/9cd8c78440eff4041045ea913b2aeb8b/yande.re%20404363%20sample%20breasts%20cum%20kantai_collection%20mitsuki_ponzu%20open_shirt%20pantsu%20prinz_eugen_%28kancolle%29%20thighhighs.jpg"));
    }
}

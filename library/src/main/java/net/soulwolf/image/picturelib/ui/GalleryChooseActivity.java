package net.soulwolf.image.picturelib.ui;


import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import net.soulwolf.image.picturelib.R;

public class GalleryChooseActivity extends BaseActivity {

    ListView mGalleryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_choose);
        mGalleryList = (ListView) findViewById(R.id.pi_gallery_choose_list);
        setTitleText(R.string.ps_gallery_choose);
        setRightText(R.string.ps_cancel);
    }


    @Override
    protected void onRightClick(View view) {
        super.onRightClick(view);
    }
}

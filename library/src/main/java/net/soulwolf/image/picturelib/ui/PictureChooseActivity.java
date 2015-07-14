package net.soulwolf.image.picturelib.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;

import net.soulwolf.image.picturelib.R;
import net.soulwolf.image.picturelib.adapter.PictureChooseAdapter;
import net.soulwolf.image.picturelib.rx.SimpleCookedCircular;
import net.soulwolf.image.picturelib.task.PictureTask;
import net.soulwolf.image.picturelib.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class PictureChooseActivity extends BaseActivity {

    public static final int RESULT_OK            = 200;

    public static final int RESULT_CANCEL        = 1022;

    public static final int GALLERY_REQUEST_CODE = 1023;

    GridView mPictureGrid;

    ArrayList<String> mPictureList;

    PictureChooseAdapter mPictureChooseAdapter;

    int mMaxPictureCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_choose);
        mPictureGrid = (GridView) findViewById(R.id.pi_picture_choose_grid);
        setTitleText(R.string.ps_picture_choose);
        setLeftText(R.string.ps_gallery);
        setRightText(R.string.ps_complete);

        mPictureList = new ArrayList<>();
        mPictureChooseAdapter = new PictureChooseAdapter(this,mPictureList,mMaxPictureCount);
        mPictureGrid.setAdapter(mPictureChooseAdapter);

        getRecentlyPicture();
    }

    private void updatePictureList(List<String> paths) {
        mPictureList.clear();
        if(paths != null){
            mPictureList.addAll(paths);
            mPictureChooseAdapter.notifyDataSetChanged();
        }
    }

    private void getRecentlyPicture() {
        PictureTask.getRecentlyPicture(getContentResolver(),30)
                .execute(new SimpleCookedCircular<List<String>>() {
                    @Override
                    public void onCooked(List<String> strings) {
                        updatePictureList(strings);
                    }
                });
    }

    private void getPictureForGallery(String folderPath) {
        PictureTask.getPictureForGallery(folderPath)
                .execute(new SimpleCookedCircular<List<String>>() {
                    @Override
                    public void onCooked(List<String> strings) {
                        updatePictureList(strings);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST_CODE
                && resultCode == GalleryChooseActivity.RESULT_OK
                && data != null){
            String path = data.getStringExtra(Constants.GALLERY_CHOOSE_PATH);
            if(!TextUtils.isEmpty(path)){
                getPictureForGallery(path);
            }
        }
    }

    @Override
    public void finish() {
        super.finish();

    }

    @Override
    protected void onLeftClick(View view) {
        super.onLeftClick(view);
        startActivityForResult(new Intent(this,GalleryChooseActivity.class),GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onRightClick(View view) {
        super.onRightClick(view);
    }
}

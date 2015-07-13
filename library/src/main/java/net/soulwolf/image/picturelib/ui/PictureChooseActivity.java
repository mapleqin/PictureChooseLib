package net.soulwolf.image.picturelib.ui;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.GridView;

import net.soulwolf.image.picturelib.R;
import net.soulwolf.image.picturelib.adapter.PictureChooseAdapter;
import net.soulwolf.image.picturelib.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PictureChooseActivity extends BaseActivity {

    public static final int RESULT_OK = 200;

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

        updatePictureList(getLatestImagePaths(50));
    }

    private void updatePictureList(ArrayList<String> paths) {
        mPictureList.clear();
        if(paths != null){
            mPictureList.addAll(paths);
            mPictureChooseAdapter.notifyDataSetChanged();
        }
    }

    private ArrayList<String> getLatestImagePaths(int maxCount) {
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String key_MIME_TYPE = MediaStore.Images.Media.MIME_TYPE;
        String key_DATA = MediaStore.Images.Media.DATA;
        ContentResolver mContentResolver = getContentResolver();
        Cursor cursor = mContentResolver.query(mImageUri, new String[]{key_DATA},
                key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=? or " + key_MIME_TYPE + "=?",
                new String[]{"image/jpg", "image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED);
        ArrayList<String> latestImagePaths = new ArrayList<String>();
        if (cursor != null) {
            if (cursor.moveToLast()) {
                while (true) {
                    String path = cursor.getString(0);
                    latestImagePaths.add(path);
                    if (latestImagePaths.size() >= maxCount || !cursor.moveToPrevious()) {
                        break;
                    }
                }
            }
            cursor.close();
        }
        return latestImagePaths;
    }

    private ArrayList<String> getAllImagePathsByFolder(String folderPath) {
        File folder = new File(folderPath);
        String[] allFileNames = folder.list();
        if (allFileNames == null || allFileNames.length == 0) {
            return null;
        }
        ArrayList<String> imageFilePaths = new ArrayList<String>();
        for (int i = allFileNames.length - 1; i >= 0; i--) {
            if (Utils.isPicture(allFileNames[i])) {
                imageFilePaths.add(folderPath + File.separator + allFileNames[i]);
            }
        }
        return imageFilePaths;
    }


    @Override
    protected void onLeftClick(View view) {
        super.onLeftClick(view);
    }

    @Override
    protected void onRightClick(View view) {
        super.onRightClick(view);
    }
}

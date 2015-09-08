package net.soulwolf.image.picturelib.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import net.soulwolf.image.picturelib.R;
import net.soulwolf.image.picturelib.adapter.PictureChooseAdapter;
import net.soulwolf.image.picturelib.rx.ResponseHandler;
import net.soulwolf.image.picturelib.task.PictureTask;
import net.soulwolf.image.picturelib.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class PictureChooseActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    public static final int RESULT_OK            = 200;

    public static final int RESULT_CANCEL        = 1022;

    public static final int GALLERY_REQUEST_CODE = 1023;

    GridView mPictureGrid;

    ArrayList<String> mPictureList;

    PictureChooseAdapter mPictureChooseAdapter;

    int mMaxPictureCount;

    int mTitleBarBackground = 0xFF16C2DD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_choose);
        if(getIntent() != null){
            mMaxPictureCount = getIntent().getIntExtra(Constants.MAX_PICTURE_COUNT,1);
            mTitleBarBackground = getIntent().getIntExtra(Constants.TITLE_BAR_BACKGROUND,mTitleBarBackground);
        }
        mPictureGrid = (GridView) findViewById(R.id.pi_picture_choose_grid);
        setTitleBarBackground(mTitleBarBackground);
        setTitleText(R.string.ps_picture_choose);
        setLeftText(R.string.ps_gallery);
        setRightText(R.string.ps_complete);

        mPictureList = new ArrayList<>();
        mPictureChooseAdapter = new PictureChooseAdapter(this,mPictureList,mMaxPictureCount);
        mPictureGrid.setAdapter(mPictureChooseAdapter);
        mPictureGrid.setOnItemClickListener(this);

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
                .subscribe(new ResponseHandler<List<String>>() {
                    @Override
                    public void onSuccess(List<String> strings) throws Exception {
                        updatePictureList(strings);
                    }

                    @Override
                    public void onFailure(Throwable error) {
                        super.onFailure(error);
                        Toast.makeText(getApplicationContext(),R.string.ps_load_image_error,Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getPictureForGallery(String folderPath) {
        PictureTask.getPictureForGallery(folderPath)
                .subscribe(new ResponseHandler<List<String>>() {
                    @Override
                    public void onSuccess(List<String> strings) throws Exception {
                        updatePictureList(strings);
                    }

                    @Override
                    public void onFailure(Throwable error) {
                        super.onFailure(error);
                        Toast.makeText(getApplicationContext(),R.string.ps_load_image_error,Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(this, GalleryChooseActivity.class);
        intent.putExtra(Constants.TITLE_BAR_BACKGROUND,mTitleBarBackground);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onRightClick(View view) {
        super.onRightClick(view);
        ArrayList<String> list = mPictureChooseAdapter.getPictureChoosePath();
        Intent data = new Intent();
        data.putStringArrayListExtra(Constants.PICTURE_CHOOSE_LIST,list);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            setResult(RESULT_CANCEL);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(mPictureChooseAdapter.contains(position)){
            mPictureChooseAdapter.removePictureChoose(view,position);
        }else {
            if(mPictureChooseAdapter.pictureChooseSize() >= mMaxPictureCount){
                Toast.makeText(this,
                        getString(R.string.ps_select_up_count,mMaxPictureCount),Toast.LENGTH_LONG).show();
                return;
            }
            mPictureChooseAdapter.addPictureChoose(view,position);
        }
        //mPictureChooseAdapter.notifyDataSetChanged();
        if(mPictureChooseAdapter.pictureChooseSize() == 0){
            setTitleText(getString(R.string.ps_picture_choose));
        }else {
            setTitleText(getString(R.string.ps_picture_choose_count
                    ,mPictureChooseAdapter.pictureChooseSize()));
        }
    }
}

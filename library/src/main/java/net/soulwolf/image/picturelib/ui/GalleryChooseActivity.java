package net.soulwolf.image.picturelib.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import net.soulwolf.image.picturelib.R;
import net.soulwolf.image.picturelib.adapter.GalleryChooseAdapter;
import net.soulwolf.image.picturelib.model.GalleryListModel;
import net.soulwolf.image.picturelib.rx.ResponseHandler;
import net.soulwolf.image.picturelib.task.GalleryTask;
import net.soulwolf.image.picturelib.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class GalleryChooseActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    static final int RESULT_OK     = 50011;

    static final int RESULT_CANCEL = 50012;

    GridView mGalleryGrid;

    List<GalleryListModel> mGalleryList;

    GalleryChooseAdapter mGalleryChooseAdapter;

    int mTitleBarBackground = 0xFF16C2DD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_choose);
        mGalleryGrid = (GridView) findViewById(R.id.pi_gallery_choose_grid);
        if(getIntent() != null){
            mTitleBarBackground = getIntent().getIntExtra(Constants.TITLE_BAR_BACKGROUND,mTitleBarBackground);
        }
        setTitleBarBackground(mTitleBarBackground);
        setTitleText(R.string.ps_gallery_choose);
        setRightText(R.string.ps_cancel);
        mGalleryList = new ArrayList<>();
        mGalleryChooseAdapter = new GalleryChooseAdapter(this,mGalleryList);
        mGalleryGrid.setAdapter(mGalleryChooseAdapter);
        mGalleryGrid.setOnItemClickListener(this);
        getGalleryList();
    }

    private void updateGalleryList(List<GalleryListModel> imagePathsByContentProvider) {
        if(imagePathsByContentProvider != null){
            mGalleryList.addAll(imagePathsByContentProvider);
            mGalleryChooseAdapter.notifyDataSetChanged();
        }
    }

    private void getGalleryList() {
        GalleryTask.getGalleryList(getContentResolver())
                .subscribe(new ResponseHandler<List<GalleryListModel>>() {
                    @Override
                    public void onSuccess(List<GalleryListModel> galleryListModels) throws Exception {
                        updateGalleryList(galleryListModels);
                    }

                    @Override
                    public void onFailure(Throwable error) {
                        super.onFailure(error);
                        Toast.makeText(getApplicationContext(), R.string.ps_load_image_error, Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            setResult(RESULT_CANCEL);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onRightClick(View view) {
        super.onRightClick(view);
        setResult(GalleryChooseActivity.RESULT_CANCEL);
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        GalleryListModel item = mGalleryChooseAdapter.getItem(position);
        if(item != null){
            Intent data = new Intent();
            data.putExtra(Constants.GALLERY_CHOOSE_PATH,item.mGalleryPath);
            setResult(RESULT_OK,data);
            finish();
        }
    }
}

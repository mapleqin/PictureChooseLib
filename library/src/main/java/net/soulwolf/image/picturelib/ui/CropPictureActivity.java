/**
 * <pre>
 * Copyright (C) 2015  校导网(武汉)科技有限责任公司 Inc！
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </pre>
 */
package net.soulwolf.image.picturelib.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


import net.soulwolf.image.picturelib.R;
import net.soulwolf.image.picturelib.rx.ResponseHandler;
import net.soulwolf.image.picturelib.task.ImageLoadTask;
import net.soulwolf.image.picturelib.view.CropImageView;

import rx.Observer;

/**
 * author : Soulwolf Create by 2015/9/8 15:47
 * email  : ToakerQin@gmail.com.
 */
public class CropPictureActivity extends BaseActivity {

    public static final int RESULT_OK = 3001;
    public static final int RESULT_CANCEL = 3002;
    public static final int RESULT_ERROR = 3003;

    public static final String PARAMS_ASPECT_RATIO_X = "params_aspect_ratio_x";
    public static final String PARAMS_ASPECT_RATIO_Y = "params_aspect_ratio_y";

    ProgressBar mProgressBar;
    CropImageView mCropImageView;
    private int mAspectRatioX = 10;
    private int mAspectRatioY = 10;

    private Uri mFromUri;
    private Bitmap mFromBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop_picture);

        if(getIntent() != null){
            mAspectRatioX = getIntent().getIntExtra(PARAMS_ASPECT_RATIO_X,mAspectRatioX);
            mAspectRatioY = getIntent().getIntExtra(PARAMS_ASPECT_RATIO_Y,mAspectRatioY);
            mFromUri = getIntent().getData();
        }

        mCropImageView = (CropImageView) findViewById(R.id.pi_crop_image_view);
        mProgressBar = (ProgressBar) findViewById(R.id.pi_crop_image_pro);
        mCropImageView.setFixedAspectRatio(true);
        //mCropImageView.setAspectRatio(mAspectRatioX, mAspectRatioY);

        setLeftText(R.string.ps_cancel);
        setRightText(R.string.ps_complete);
        setTitleText(R.string.ps_picture_crop);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialize();
    }

    private void initialize() {
        System.out.println("------->:" + mCropImageView.getHeight());
        if(mFromUri != null){
            if(mFromBitmap == null){
                ImageLoadTask.getInstance().load(mFromUri,mCropImageView.getWidth(),mCropImageView.getHeight())
                        .subscribe(getBitmapSubscriber());
            }else {
                mCropImageView.setImageBitmap(mFromBitmap);
            }
        }
    }


    @Override
    protected void onLeftClick(View view) {
        super.onLeftClick(view);
        setResult(RESULT_CANCEL);
        finish();
    }

    private void showLoading(){
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading(){
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onRightClick(View view) {
        super.onRightClick(view);
        Bitmap bitmap = mCropImageView.getCroppedImage();
        if(bitmap != null){
            compress(bitmap);
        }else {
            setResult(RESULT_ERROR);
        }
        finish();
    }

    private void compress(Bitmap bitmap){
        showLoading();
        Intent data = new Intent();
        data.putExtra("data", bitmap);
        setResult(RESULT_OK, data);
    }

    public Observer<? super Bitmap> getBitmapSubscriber() {
        return new ResponseHandler<Bitmap>() {

            @Override
            public void onStart() {
                super.onStart();
                showLoading();
            }

            @Override
            public void onSuccess(Bitmap bitmap) throws Exception {
                hideLoading();
                mFromBitmap = bitmap;
                mCropImageView.setImageBitmap(bitmap);
            }

            @Override
            public void onFailure(Throwable error) {
                super.onFailure(error);
                hideLoading();
                Toast.makeText(getApplicationContext(),R.string.ps_load_image_error,Toast.LENGTH_SHORT).show();
            }
        };
    }
}

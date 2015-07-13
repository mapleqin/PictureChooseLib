/**
 * <pre>
 * Copyright 2015 Soulwolf Ching
 * Copyright 2015 The Android Open Source Project for PictureLib
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
package net.soulwolf.image.picturelib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.toaker.common.tlog.TLog;

import net.soulwolf.image.picturelib.exception.FileCreateException;
import net.soulwolf.image.picturelib.exception.PhotographException;
import net.soulwolf.image.picturelib.exception.PictureCropException;
import net.soulwolf.image.picturelib.listener.OnPicturePickListener;
import net.soulwolf.image.picturelib.ui.PictureChooseActivity;
import net.soulwolf.image.picturelib.utils.Constants;
import net.soulwolf.image.picturelib.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * author: Soulwolf Created on 2015/7/13 20:55.
 * email : Ching.Soulwolf@gmail.com
 */
public class PictureProcess {

    static final boolean DEBUG = true;

    static final String LOG_TAG = "PictureProcess:";

    static final int CAMERA_REQUEST_CODE = 1105;

    static final int CLIP_REQUEST_CODE   = 1106;

    static final String TEMP_FILE_SUFFIX = ".temp";

    static final String CAMERA_TEMP_NAME = "5976cee021f24c51";

    static final String CLIP_TEMP_NAME   = "4613a93e70ae56b5";

    protected Activity mContext;

    protected int mMaxPictureCount = 1;

    protected int mClipWidth = 350;

    protected int mClipHeight = 350;

    protected boolean isClip = false;

    protected OnPicturePickListener mOnPicturePickListener;

    protected PictureFrom mPictureFrom = PictureFrom.GALLERY;

    protected File mCameraPath;

    protected File mCropPath;

    protected File mCacheDir;

    public PictureProcess(Activity context,String cacheDir){
        this.mContext = context;
        if(TextUtils.isEmpty(cacheDir)){
            if(android.os.Environment.getExternalStorageState().equals(
                    android.os.Environment.MEDIA_MOUNTED)){
                this.mCacheDir = mContext.getExternalCacheDir();
            }else {
                this.mCacheDir = mContext.getCacheDir();
            }
        }else {
            this.mCacheDir = new File(cacheDir);
            if(!mCacheDir.mkdirs()){
                TLog.e(LOG_TAG,"CacheDir mkdirs failure!");
            }
        }
    }

    public PictureProcess(Activity context){
        this(context, null);
    }


    public void onProcessResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PictureProcess.CAMERA_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK && mCameraPath != null){
                if(isClip){
                    corpPicture(mCameraPath);
                }else {
                    onSuccess(mCameraPath);
                }
            }else {
                if(mOnPicturePickListener != null){
                    mOnPicturePickListener.onError(
                            new PhotographException("resultCode != Activity.RESULT_OK || mCameraPath == null"));
                }
            }
        }else if(requestCode == PictureProcess.CLIP_REQUEST_CODE){
            if(resultCode == Activity.RESULT_OK && data != null){
                Bitmap bitmap = data.getParcelableExtra("data");
                if(bitmap != null && Utils.saveBitmap(bitmap,mCropPath)){
                    onSuccess(mCropPath);
                }else {
                    if(mOnPicturePickListener != null){
                        mOnPicturePickListener.onError(new PictureCropException("data.getParcelableExtra(\"data\") == NULL"));
                    }
                }
            }else {
                if(mOnPicturePickListener != null){
                    mOnPicturePickListener.onError(new PictureCropException("resultCode != Activity.RESULT_OK || data == null"));
                }
            }
        }
    }

    protected void onSuccess(File ... files){
        List<String> pictures = new ArrayList<>();
        for (File file:files){
            pictures.add(file.getAbsolutePath());
        }
        if(mOnPicturePickListener != null){
            mOnPicturePickListener.onSuccess(pictures);
        }
    }

    protected void corpPicture(File path) {
        this.mCropPath = getCropPath();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(Uri.fromFile(path), "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", mClipWidth);
        intent.putExtra("outputY", mClipHeight);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        mContext.startActivityForResult(intent, CLIP_REQUEST_CODE);
    }

    protected File getCropPath() {
        File file = new File(mCacheDir,String.format("%s%S",CLIP_TEMP_NAME,TEMP_FILE_SUFFIX));
        if(file.exists()){
            if(!file.delete()){
                TLog.e(LOG_TAG,"CropPath delete failure!");
            }
        }
        try {
            if(!file.createNewFile()){
                TLog.e(LOG_TAG,"CropPath create failure!");
            }
        }catch (Exception e){
            if(mOnPicturePickListener != null){
                mOnPicturePickListener.onError(new PictureCropException(e));
            }
        }
        return file;
    }

    public void execute(OnPicturePickListener listener){
        if(mMaxPictureCount <= 0){
            TLog.e(LOG_TAG,"execute MaxPictureCount <= 0!");
            return;
        }
        if(listener == null){
            throw new NullPointerException("OnPicturePickListener == NULL");
        }
        this.mOnPicturePickListener = listener;
        if(isClip && mMaxPictureCount > 1){
            throw new IllegalStateException("The Image clip can only select a picture!");
        }
        if(mPictureFrom == PictureFrom.GALLERY){
            executeGallery();
        }else {
            this.setMaxPictureCount(1);
            executeCamera();
        }
    }

    protected void executeGallery() {
        Intent intent = new Intent(mContext, PictureChooseActivity.class);
        intent.putExtra(Constants.MAX_PICTURE_COUNT,mMaxPictureCount);
        mContext.startActivity(intent);
    }

    protected void executeCamera() {
        this.mCameraPath = new File(mCacheDir,String.format("%s%s", CAMERA_TEMP_NAME,TEMP_FILE_SUFFIX));
        if(mCameraPath.exists()){
            if(!mCameraPath.delete()){
                TLog.e(LOG_TAG,"CameraPath delete failure!");
            }
        }
        try {
            if(!mCameraPath.createNewFile()){
                TLog.e(LOG_TAG,"CameraPath create failure!");
            }
        }catch (Exception e){
            if(mOnPicturePickListener != null){
                mOnPicturePickListener.onError(new FileCreateException(e));
            }
        }
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCameraPath));
        mContext.startActivityForResult(intentFromCapture, PictureProcess.CAMERA_REQUEST_CODE);
    }

    public void reset(){
        this.setMaxPictureCount(1);
        this.setClip(false);
        this.setPictureFrom(PictureFrom.GALLERY);
        this.mClipWidth = 350;
        this.mClipHeight = 350;
    }

    public void setMaxPictureCount(int maxPictureCount) {
        this.mMaxPictureCount = maxPictureCount;
    }

    public void setClip(boolean isClip,int clipWidth,int clipHeight) {
        this.isClip = isClip;
        this.mClipWidth = clipWidth;
        this.mClipHeight = clipHeight;
    }

    public void setClip(boolean isClip) {
        setClip(isClip,mClipWidth,mClipHeight);
    }

    public void setPictureFrom(PictureFrom pictureFrom) {
        this.mPictureFrom = pictureFrom;
    }
}

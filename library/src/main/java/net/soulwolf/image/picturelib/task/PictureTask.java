/**
 * <pre>
 * Copyright (C) 2015  Soulwolf PictureChooseLib
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
package net.soulwolf.image.picturelib.task;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.toaker.common.tlog.TLog;

import net.soulwolf.image.picturelib.rx.ObservableWrapper;
import net.soulwolf.image.picturelib.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * author : Soulwolf Create by 2015/7/14 17:15
 * email  : ToakerQin@gmail.com.
 */
public class PictureTask {

    private static final boolean DEBUG = false;

    private static final String LOG_TAG = "PictureTask:";

    public static Observable<List<String>> getRecentlyPicture(final ContentResolver resolver,final int maxCount){
        return ObservableWrapper.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                try{
                    subscriber.onStart();
                    List<String> picturePath = getRecentlyPicturePath(resolver, maxCount);
                    if(DEBUG){
                        TLog.i(LOG_TAG,"getRecentlyPicture :perform:%s",picturePath);
                    }
                    subscriber.onNext(picturePath);
                    subscriber.onCompleted();
                }catch (Exception e){
                    subscriber.onError(e);
                }
            }
        });
    }

    public static Observable<List<String>> getPictureForGallery(final String galleryDir){
        return ObservableWrapper.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                try{
                    subscriber.onStart();
                    List<String> picturePath = getPictureListForGallery(galleryDir);
                    if(DEBUG){
                        TLog.i(LOG_TAG,"getPictureForGallery :perform:%s",picturePath);
                    }
                    subscriber.onNext(picturePath);
                    subscriber.onCompleted();
                }catch (Exception e){
                    subscriber.onError(e);
                }
            }
        });
    }

    private static List<String> getPictureListForGallery(String galleryPath) {
        String[] pictureNames = new File(galleryPath).list();
        if (pictureNames == null || pictureNames.length == 0) {
            return null;
        }
        ArrayList<String> pictures = new ArrayList<String>();
        for (String name:pictureNames){
            if(Utils.isPicture(name) && !TextUtils.isEmpty(name)){
                pictures.add(Utils.and(galleryPath,File.separator,name));
            }
        }
        return pictures;
    }

    private static List<String> getRecentlyPicturePath(ContentResolver mContentResolver,int maxCount) {
        Uri EXTERNAL_CONTENT_URI = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String MIME_TYPE = MediaStore.Images.Media.MIME_TYPE;
        String DATA = MediaStore.Images.Media.DATA;
        Cursor cursor = mContentResolver.query(EXTERNAL_CONTENT_URI, new String[]{DATA},
                MIME_TYPE + "=? or " + MIME_TYPE + "=? or " + MIME_TYPE + "=?",
                new String[]{"image/jpg", "image/jpeg", "image/png"},
                MediaStore.Images.Media.DATE_MODIFIED);
        if(cursor == null){
            return null;
        }
        List<String> recentlyPictures = new ArrayList<String>();
        if (cursor.moveToLast()) {
            while (true) {
                String path = cursor.getString(0);
                if(!TextUtils.isEmpty(path)){
                    recentlyPictures.add(path);
                }
                if (recentlyPictures.size() >= maxCount || !cursor.moveToPrevious()) {
                    break;
                }
            }
        }
        Utils.closeQuietly(cursor);
        return recentlyPictures;
    }
}

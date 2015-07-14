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

import net.soulwolf.image.picturelib.model.GalleryListModel;
import net.soulwolf.image.picturelib.rx.CookKitchen;
import net.soulwolf.image.picturelib.rx.OnCookPotable;
import net.soulwolf.image.picturelib.rx.Spicypotable;
import net.soulwolf.image.picturelib.rx.ThreadDispatch;
import net.soulwolf.image.picturelib.utils.PictureFilter;
import net.soulwolf.image.picturelib.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * author : Soulwolf Create by 2015/7/14 17:58
 * email  : ToakerQin@gmail.com.
 */
public class GalleryTask {

    public static Spicypotable<List<GalleryListModel>> getGalleryList(final ContentResolver resolver){
        return Spicypotable.create(new OnCookPotable<List<GalleryListModel>>() {
            @Override
            public void perform(CookKitchen<? super List<GalleryListModel>> kitchen) {
                try{
                    kitchen.onStart();
                    List<GalleryListModel> models = getGalleryListPath(resolver);
                    kitchen.onSuccess(models);
                }catch (Exception e){
                    kitchen.onError(e);
                }
            }
        }).cookPotableOn(ThreadDispatch.THREAD)
        .cookedCircularOn(ThreadDispatch.MAIN_THREAD);
    }

    private static List<GalleryListModel> getGalleryListPath(ContentResolver mContentResolver) {
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
        ArrayList<GalleryListModel> list = new ArrayList<>();
        HashSet<String> cachePath = new HashSet<String>();
        if (cursor.moveToLast()) {
            while (true) {
                String picturePath = cursor.getString(0);
                if(!Utils.isPicture(picturePath)){
                    continue;
                }
                File parentFile = new File(picturePath).getParentFile();
                String parentPath = parentFile.getAbsolutePath();
                if (!cachePath.contains(parentPath)) {
                    list.add(new GalleryListModel(parentPath, getPictureCount(parentFile),
                            getFrontPicture(parentFile)));
                    cachePath.add(parentPath);
                }
                if (!cursor.moveToPrevious()) {
                    break;
                }
            }
        }
        cachePath.clear();
        Utils.closeQuietly(cursor);
        return list;
    }

    private static int getPictureCount(File folder) {
        return Utils.arraySize(folder.listFiles(new PictureFilter()));
    }

    private static String getFrontPicture(File folder) {
        File[] files = folder.listFiles(/*new PictureFilter()*/);
        if(files != null && files.length > 0){
            File file = files[files.length -1];
            return file != null ? file.getAbsolutePath() : null;
        }
        return null;
    }
}

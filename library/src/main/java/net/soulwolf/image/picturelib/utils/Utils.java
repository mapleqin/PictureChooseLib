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
package net.soulwolf.image.picturelib.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collection;

/**
 * author: Soulwolf Created on 2015/7/13 22:56.
 * email : Ching.Soulwolf@gmail.com
 */
public class Utils {

    public static boolean saveBitmap(Bitmap bitmap,File file){
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,os);
            return true;
        }catch (Exception e){
            return false;
        }finally {
            if(os != null){
              closeQuietly(os);
            }
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }

    public static boolean isPicture(String fileName) {
        return fileName.endsWith(".jpg")
                || fileName.endsWith(".jpeg")
                || fileName.endsWith(".bmp")
                || fileName.endsWith(".png");
    }

    public static String folderName(String path){
        if(TextUtils.isEmpty(path)){
            return path;
        }
        int lastIndexOf = path.lastIndexOf('/');
        if(lastIndexOf != -1){
            return path.substring(lastIndexOf + 1,path.length());
        }
        return path;
    }

    public static String and(String ... value){
        StringBuilder builder = new StringBuilder();
        for (String str:value){
            builder.append(str);
        }
        return builder.toString();
    }

    public static <T> int arraySize(T [] arr){
        return arr == null ? 0 : arr.length;
    }

    public static int collectionSize(Collection collection){
        return collection == null ? 0 : collection.size();
    }
}

/**
 * <pre>
 * Copyright 2015 Soulwolf Ching
 * Copyright 2015 The Android Open Source Project for PictureChooseLib
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
package net.soulwolf.image.picturelib.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;

import net.soulwolf.image.picturelib.utils.Utils;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author: Soulwolf Created on 2015/8/22 16:00.
 * email : Ching.Soulwolf@gmail.com
 */
public final class ImageLoadTask {

    static ImageLoadTask mInstance;

    public static void init(Context context){
        synchronized (ImageLoadTask.class){
            mInstance = new ImageLoadTask(context);
        }
    }

    public static ImageLoadTask getInstance(){
        if(mInstance == null){
            throw new ExceptionInInitializerError("ImageLoadTask is not initialize!");
        }
        return mInstance;
    }

    ImageLoadHandler mImageLoadHandler;

    ImageLoadTask(Context context) {
        mImageLoadHandler = new DefaultImageLoadHandler(context);
    }

    public void setImageLoadHandler(ImageLoadHandler handler){
        this.mImageLoadHandler = handler;
    }

    public <TARGET extends ImageView> void display(TARGET target,String url) {
        mImageLoadHandler.display(target,url);
    }

    public Observable<Bitmap> load(final Uri uri,final int width,final int height){
        return Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                try {
                    subscriber.onStart();
                    Bitmap bitmap = mImageLoadHandler.loadSync(uri, width, height);
                    Utils.checkNullPointer(bitmap);
                    subscriber.onNext(bitmap);
                    subscriber.onCompleted();
                }catch (Exception e){
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
    }

    public void shutdown() {
        mImageLoadHandler.shutdown();
        mInstance = null;
    }
}

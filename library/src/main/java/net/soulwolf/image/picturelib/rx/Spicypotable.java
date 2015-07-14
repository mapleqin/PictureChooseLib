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
package net.soulwolf.image.picturelib.rx;

import android.os.Handler;
import android.os.Looper;

import com.toaker.common.tlog.TLog;

/**
 * author : Soulwolf Create by 2015/7/14 15:27
 * email  : ToakerQin@gmail.com.
 */
public class Spicypotable<ENTRY> {

    private static final boolean DEBUG = false;

    private static final String LOG_TAG = "Spicypotable:";

    public static <ENTRY> Spicypotable<ENTRY> create(OnCookPotable<ENTRY> onCookPotable){
        return new Spicypotable<ENTRY>(onCookPotable);
    }

    OnCookPotable<ENTRY> mOnCookPotable;

    ThreadDispatch mCookPotableThread = ThreadDispatch.THREAD;

    ThreadDispatch mCookedCircularThread = ThreadDispatch.MAIN_THREAD;

    final ExecutorDelivery mMainDelivery;

    final ExecutorDelivery mTreadDelivery;

    protected Spicypotable(OnCookPotable<ENTRY> onCookPotable){
        this.mOnCookPotable = onCookPotable;
        this.mMainDelivery = new ExecutorDelivery(new Handler(Looper.getMainLooper()));
        this.mTreadDelivery = new ExecutorDelivery(new Handler(Looper.myLooper()));
    }

    public void execute(CookedCircular<? super ENTRY> cookedCircular){
        CookKitchen<ENTRY> cookKitchen = new CookKitchen<>(cookedCircular
                ,getExecutorDelivery(mCookedCircularThread));
        getExecutorDelivery(mCookedCircularThread)
                .execute(new CookPotableRunnable<ENTRY>(mOnCookPotable,cookKitchen));
        if(DEBUG){
            TLog.i(LOG_TAG,"execute");
        }
    }

    ExecutorDelivery getExecutorDelivery(ThreadDispatch dispatch){
        if(dispatch == ThreadDispatch.MAIN_THREAD){
            return mMainDelivery;
        }
        return mTreadDelivery;
    }

    public Spicypotable<ENTRY> cookPotableOn(ThreadDispatch dispatch){
        this.mCookPotableThread = dispatch;
        return this;
    }

    public Spicypotable<ENTRY> cookedCircularOn(ThreadDispatch dispatch){
        this.mCookedCircularThread = dispatch;
        return this;
    }
}

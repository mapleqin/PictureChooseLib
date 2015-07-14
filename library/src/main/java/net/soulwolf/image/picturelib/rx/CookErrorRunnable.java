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

/**
 * author : Soulwolf Create by 2015/7/14 16:46
 * email  : ToakerQin@gmail.com.
 */
public class CookErrorRunnable implements Runnable {

    CookedCircular<?> mCookedCircular;

    Throwable error;

    public CookErrorRunnable(CookedCircular<?> circular, Throwable error) {
        this.mCookedCircular = circular;
        this.error = error;
    }

    @Override
    public void run() {
        mCookedCircular.onError(error);
    }
}

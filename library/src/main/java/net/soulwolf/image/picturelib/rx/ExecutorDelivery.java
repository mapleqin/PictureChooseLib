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

import java.util.concurrent.Executor;

/**
 * author : Soulwolf Create by 2015/7/14 16:38
 * email  : ToakerQin@gmail.com.
 */
public class ExecutorDelivery {

    /** Used for posting responses, typically to the main thread. */
    private final Executor mResponsePoster;

    /**
     * Creates a new response delivery interface.
     */
    public ExecutorDelivery() {
        this(null);
    }

    /**
     * Creates a new response delivery interface.
     * @param handler {@link Handler} to post responses on
     */
    public ExecutorDelivery(final Handler handler) {
        // Make an Executor that just wraps the handler.
        mResponsePoster = new Executor() {
            @Override
            public void execute(Runnable command) {
                if(handler == null){
                    new Thread(command).start();
                }else {
                    handler.post(command);
                }
            }
        };
    }

    public void execute(Runnable runnable){
        this.mResponsePoster.execute(runnable);
    }
}

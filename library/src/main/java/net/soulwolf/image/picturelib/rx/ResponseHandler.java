/**
 * <pre>
 * Copyright (C) 2015  Soulwolf XiaoDaoW3.0
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

import rx.Subscriber;

/**
 * author : Soulwolf Create by 2015/6/29 10:41
 * email  : ToakerQin@gmail.com.
 */
public abstract class ResponseHandler<RESPONSE> extends Subscriber<RESPONSE>{

    public void onFailure(Throwable error){

    }

    public abstract void onSuccess(RESPONSE response) throws Exception;

    @Override
    public void onCompleted() {

    }

    public final void onError(Throwable throwable){
        onFailure(throwable);
        unsubscribe();
    }

    @Override
    public void onNext(RESPONSE response) {
        try {
            onSuccess(response);
        }catch (Exception e){
            onFailure(e);
        }
        unsubscribe();
    }
}

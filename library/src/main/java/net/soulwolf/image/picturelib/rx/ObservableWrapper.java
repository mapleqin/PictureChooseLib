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

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * author : Soulwolf Create by 2015/6/26 10:32
 * email  : ToakerQin@gmail.com.
 */
public final class ObservableWrapper {

   public static <T> Observable<T> create(Observable.OnSubscribe<T> subscribe){
       return Observable.create(subscribe)
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread());
   }
}

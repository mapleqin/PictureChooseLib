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
package net.soulwolf.image.picturelib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.soulwolf.image.picturelib.R;
import net.soulwolf.image.picturelib.model.GalleryListModel;
import net.soulwolf.image.picturelib.task.ImageLoadTask;
import net.soulwolf.image.picturelib.utils.Utils;

import java.io.File;
import java.util.List;

/**
 * author : Soulwolf Create by 2015/7/14 9:21
 * email  : ToakerQin@gmail.com.
 */
public class GalleryChooseAdapter extends BaseAdapter {

    Context mContext;

    List<GalleryListModel> mGalleryList;

    public GalleryChooseAdapter(Context context,List<GalleryListModel> data){
        this.mContext = context;
        this.mGalleryList = data;
    }

    @Override
    public int getCount() {
        return mGalleryList == null ? 0 : mGalleryList.size();
    }

    @Override
    public GalleryListModel getItem(int position) {
        return mGalleryList == null ? null : mGalleryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.listview_gallery_choose_item,null);
            holder.mGalleryName = (TextView) convertView.findViewById(R.id.pi_gallery_choose_item_name);
            holder.mPictureView = (ImageView) convertView.findViewById(R.id.pi_gallery_choose_item_pic);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        GalleryListModel item = getItem(position);
        holder.mGalleryName.setText(String.format("%s(%s)",
                Utils.folderName(item.mGalleryPath),item.mPictureCount));
        ImageLoadTask.getInstance().display(holder.mPictureView, Utils.urlFromFile(item.mFrontPath));
        return convertView;
    }

    static class ViewHolder{
        public ImageView mPictureView;

        public TextView  mGalleryName;
    }
}

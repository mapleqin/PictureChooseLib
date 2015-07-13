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
package net.soulwolf.image.picturelib.ui;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.soulwolf.image.picturelib.R;

/**
 * author: Soulwolf Created on 2015/7/13 23:24.
 * email : Ching.Soulwolf@gmail.com
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected TextView mTitleText;

    protected Button   mActionLeft;

    protected Button   mActionRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void setLeftText(@StringRes int redId){
        if(mActionLeft == null){
            this.mActionLeft = (Button) findViewById(R.id.pi_title_bar_left);
            this.mActionLeft.setOnClickListener(this);
        }
        this.mActionLeft.setText(redId);
    }

    protected void setRightText(@StringRes int redId){
        if(mActionRight == null){
            this.mActionRight = (Button) findViewById(R.id.pi_title_bar_right);
            this.mActionRight.setOnClickListener(this);
        }
        this.mActionRight.setText(redId);
    }

    protected void setTitleText(@StringRes int redId){
        if(mTitleText == null){
            this.mTitleText = (TextView) findViewById(R.id.pi_title_bar_title);
        }
        this.mTitleText.setText(redId);
    }

    protected void onLeftClick(View view){

    }

    protected void onRightClick(View view){

    }

    @Override
    public void onClick(View v) {
        if(v == mActionLeft){
            onLeftClick(v);
        }else if(v == mActionRight){
            onRightClick(v);
        }
    }
}

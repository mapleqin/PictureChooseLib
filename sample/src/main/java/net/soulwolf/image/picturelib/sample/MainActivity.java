package net.soulwolf.image.picturelib.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;

import com.toaker.common.tlog.TLog;

import net.soulwolf.image.picturelib.PictureFrom;
import net.soulwolf.image.picturelib.PictureProcess;
import net.soulwolf.image.picturelib.listener.OnPicturePickListener;
import net.soulwolf.image.picturelib.sample.adapter.PictureAdapter;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnPicturePickListener {

    static final boolean DEBUG = true;

    static final String  LOG_TAG = "MainActivity:";

    PictureProcess mPictureProcess;

    Toolbar mToolbar;

    GridView mPictureGrid;

    List<String> mPictureList;

    PictureAdapter mPictureAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.pi_toolbar);
        setSupportActionBar(mToolbar);
        mPictureGrid = (GridView) findViewById(R.id.pi_grid);
        mPictureProcess = new PictureProcess(this);

        mPictureList = new ArrayList<>();
        mPictureAdapter = new PictureAdapter(this,mPictureList);
        mPictureGrid.setAdapter(mPictureAdapter);
    }

    protected void updatePictureList(List<String> list){
        mPictureList.clear();
        if(list != null){
            mPictureList.addAll(list);
        }
        mPictureAdapter.notifyDataSetChanged();
    }

    public void onGallery(View view){
        mPictureProcess.setPictureFrom(PictureFrom.GALLERY);
        mPictureProcess.setClip(true);
        mPictureProcess.setMaxPictureCount(1);
        mPictureProcess.execute(this);
    }

    public void onCamera(View view){
        mPictureProcess.setPictureFrom(PictureFrom.CAMERA);
        mPictureProcess.setClip(true);
        mPictureProcess.setMaxPictureCount(1);
        mPictureProcess.execute(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPictureProcess.onProcessResult(requestCode, resultCode, data);
    }

    @Override
    public void onSuccess(List<String> pictures) {
        if(DEBUG){
            TLog.i(LOG_TAG,"OnSuccess:%s",pictures);
        }
        updatePictureList(pictures);
    }

    @Override
    public void onError(Exception e) {
        TLog.e(LOG_TAG,"onError",e);
    }
}

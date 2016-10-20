package com.kingja.raphael;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.HandlerThread;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.bean.ImageItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Description：TODO
 * Create Time：2016/9/5 15:54
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class HomeActivity extends AppCompatActivity implements SelectPicAdapter.OnAddPicListener{
    private final String TAG = getClass().getSimpleName();
    private GridView gv;
    private List<ImageItem> itemList=new ArrayList<>();
    private SelectPicAdapter selectPicAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        gv = (GridView) findViewById(R.id.gv);
        selectPicAdapter = new SelectPicAdapter(this, itemList);
        selectPicAdapter.setOnAddPicListener(this);
        gv.setAdapter(selectPicAdapter);

        HandlerThread workerThread = new HandlerThread("global_worker_thread");
        workerThread.start();
        initImageLoader(this);

    }

    public void onSelect(View view) {

    }


    public static void initImageLoader(Context context){
        if(!ImageLoader.getInstance().isInited()){
            ImageLoaderConfiguration config = null;
            if(BuildConfig.DEBUG){
                config = new ImageLoaderConfiguration.Builder(context)
                        .threadPriority(Thread.NORM_PRIORITY - 1)
                        .tasksProcessingOrder(QueueProcessingType.LIFO)
                        .memoryCacheSizePercentage(13)
                        .diskCacheSize(500 * 1024 * 1024)
                        .build();
            }else{
                config = new ImageLoaderConfiguration.Builder(context)
                        .threadPriority(Thread.NORM_PRIORITY - 2)
                        .diskCacheSize(500 * 1024 * 1024)
                        .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                        .tasksProcessingOrder(QueueProcessingType.LIFO).build();
            }
            ImageLoader.getInstance().init(config);
        }

    }

    @Override
    public void onAddPic() {
        AndroidImagePicker.getInstance().pickMulti(this,AndroidImagePicker.MAX_PIC_NUM-itemList.size(), true, new AndroidImagePicker.OnImagePickCompleteListener() {
            @Override
            public void onImagePickComplete(List<ImageItem> items) {
                itemList.addAll(items);
                selectPicAdapter.setRefresh(itemList);
                for (ImageItem bean :items) {
                    Bitmap bitmap = ImageUtil.compressScaleFromF2B(bean.path);
                    boolean raphael = ImageUtil.saveBitmap2file(HomeActivity.this,bitmap, "Raphael", new Date().toString());
                    Log.e(TAG, "raphael: "+raphael);

                }
            }
        });
    }
}

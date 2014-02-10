package com.hiibox.houseshelter.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import com.hiibox.houseshelter.ShaerlocActivity;
import com.hiibox.houseshelter.R;
import com.hiibox.houseshelter.core.MianActivity;
import com.hiibox.houseshelter.core.GlobalUtil;
import com.hiibox.houseshelter.util.MessageUtil;

    
  
  
  
  
  
  
  
public class AddPictureActivity extends ShaerlocActivity {

    @ViewInject(id = R.id.take_photos_tv, click = "onClick") TextView takePhotosTV;
    @ViewInject(id = R.id.local_photos_tv, click = "onClick") TextView localPhotosTV;
    @ViewInject(id = R.id.cancel_tv, click = "onClick") TextView cancelTV;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_picture_layout);
    }
    
    public void onClick(View v) {
        if (v == takePhotosTV) {
            Intent takePhotosIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePhotosIntent, 0x101);
        } else if (v == localPhotosTV) {
            Intent localPictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
            localPictureIntent.setType("image/*");
            startActivityForResult(localPictureIntent, 0x102);
        } else if (v == cancelTV) {
            MianActivity.getScreenManager().exitActivity(mActivity);
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 0x101) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");                           
                File file = new File(GlobalUtil.IMAGE_PATH);
                if (!file.exists()){
                    file.mkdirs();
                }
                long dateTaken = System.currentTimeMillis();
                String pictureName = GlobalUtil.IMAGE_PATH + dateTaken + ".jpg";
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(pictureName);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);          
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fos.flush();
                        fos.close();
                        Intent intent = new Intent();
                        intent.putExtra("picturePath", pictureName);
                        this.setResult(RESULT_OK, intent);
                        MianActivity.getScreenManager().exitActivity(mActivity);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == 0x102) {
                Uri uri = data.getData();
                String[] projection = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(uri, projection, null, null, null);
                if (null != cursor){
                      




  
                          
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String path = cursor.getString(columnIndex);
                    if (path.endsWith("png") || path.endsWith("jpg") || path.endsWith("JPG")){
                        Intent intent = new Intent();
                        intent.putExtra("picturePath", path);
                        this.setResult(RESULT_OK, intent);
                        MianActivity.getScreenManager().exitActivity(mActivity);
                    } else {
                        MessageUtil.alertMessage(mContext, R.string.it_is_not_a_valid_picture);
                    }
                } else {
                    MessageUtil.alertMessage(mContext, R.string.the_picture_is_not_exist);
                }
            }
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    
}

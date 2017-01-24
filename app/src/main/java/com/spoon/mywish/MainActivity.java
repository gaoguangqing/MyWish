package com.spoon.mywish;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


public class MainActivity extends Activity {


    //679ab26698d5ab8accaa76f919345330
    private ImageView mPhoto;
    private EditText mWord;
    private Button share;
    private IWXAPI iwxapi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iwxapi= WXAPIFactory.createWXAPI(this,"wxc14534cba4a6eafe");
        iwxapi.registerApp("wxc14534cba4a6eafe");


        mPhoto= (ImageView) findViewById(R.id.photo);
        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK,null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                startActivityForResult(intent,100);
            }
        });
        mWord= (EditText) findViewById(R.id.word);
        mWord.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/test.ttf"));
        share= (Button) findViewById(R.id.share);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weChatShare();
                share.setVisibility(View.VISIBLE);
            }
        });
    }




    private void weChatShare() {
        if (!iwxapi.isWXAppInstalled()) {
            //提醒用户没有安装微信
            Toast.makeText(getApplicationContext(),"没安装微信",Toast.LENGTH_SHORT).show();
            return;
        }
//        WXImageObject image = new WXImageObject();
//        WXMediaMessage msg = new WXMediaMessage(image);
//        msg.mediaObject = new WXImageObject(generateWishCard());

        WXWebpageObject webpage = new WXWebpageObject();

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "WebPage Title";
        msg.description = "WebPage Description";
        msg.mediaObject = new WXImageObject(generateWishCard());


        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        iwxapi.sendReq(req);


    }

    private Bitmap generateWishCard() {
        share.setVisibility(View.INVISIBLE);
        View view=getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        return view.getDrawingCache();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK&&requestCode==100)
        {
            if (data!=null)
            {
                mPhoto.setImageURI(data.getData());
            }
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK)
        {
            finish();

        }
        return super.onKeyDown(keyCode, event);

    }
}

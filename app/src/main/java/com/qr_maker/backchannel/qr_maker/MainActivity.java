package com.qr_maker.backchannel.qr_maker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    //My code
    Button sButton;
    Button pButton;
    EditText mEdit;
    Bitmap imgBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sButton = (Button) findViewById(R.id.button);
        mEdit = (EditText) findViewById(R.id.editText);
        pButton = (Button) findViewById(R.id.button2);
        sButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        Log.v("EditText", mEdit.getText().toString());
                        Intent intent = new Intent(MainActivity.this, DisplayCodeActivity.class);
                        EditText editText = (EditText)findViewById(R.id.editText);
                        if (imgBitmap.getWidth() > 0 || imgBitmap.getHeight() > 0) {
                            intent.putExtra("UserPhoto", imgBitmap);
                        }
                        startActivity(intent);

                    }
                }
        );
        pButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        TakePic();
                    }
                }
        );
    }

    final int REQ_IMG_CAP = 1;

    private void TakePic(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, REQ_IMG_CAP);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQ_IMG_CAP && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            imgBitmap = (Bitmap) extras.get("data");

        }
    }

}

package com.qr_maker.backchannel.qr_maker;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class DisplayCodeActivity extends AppCompatActivity {

    private static int SIDE = 500;
    ImageView qrImage;
    Button restart;
    private static int QRSIZE = 1000;
    int TIME = 500;
    int numOfQRCodes;
    ArrayList<String> chunks;
    ArrayList<Bitmap> codes = new ArrayList<>();

    ProgressDialog encoding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_code);

        //Inflate qr Image
        qrImage = (ImageView) findViewById(R.id.qrImage);
        restart = (Button) findViewById(R.id.restart);


        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restart.setClickable(false);
                Restart();
            }
        });
    }

    void Restart(){


        if(codes.size() == 0) {

            // Convert image to bitmap and bitmap to string
            Bitmap moonBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.mipmap.icon);
            String moonStr = BitMapToString(moonBitmap);

            // Get Number of characters in string
            int moonStrLen = moonStr.length();

            // Calculate Number of QR Codes Necessary
            numOfQRCodes = (int) Math.ceil(moonStrLen/ ((double) QRSIZE));

            // Split
            chunks = Lists.newArrayList(Splitter.fixedLength(QRSIZE).split(moonStr));

            codes.add(encodeToQrCode(String.format("%02d", 0) + (numOfQRCodes), SIDE, SIDE));

            for (int i = 1; i < numOfQRCodes+1; i++) {
                codes.add(encodeToQrCode(String.format("%02d", i) + " " + chunks.get(i-1), SIDE, SIDE));
                Log.d("bitmap encoded", String.valueOf(i));
            }
        }

        new CountDownTimer((numOfQRCodes + 2) * TIME, TIME){
            int index = 0;
            @Override
            public void onTick(long millisUntilFinished) {

                if(index < numOfQRCodes+1) {
                    qrImage.setImageBitmap(codes.get(index));
                    Log.d("index value", String.valueOf(index));
                    index++;
                } else {
                    index = 0;
                }
            }

            @Override
            public void onFinish() {
                Restart();
            }
        }.start();
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            for (int i = 0; i < numOfQRCodes; i++){
                codes.add(encodeToQrCode( String.format("%02d", i) + " " + chunks.get(i), SIDE, SIDE));
                Log.d("bitmap encoded", String.valueOf(i));
            }

            runOnUiThread(new Runnable(){
                @Override
                public void run(){
                    encoding.dismiss();
                }
            });
        }
    };



    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream ByteStream=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, ByteStream);
        byte [] b=ByteStream.toByteArray();
        String temp=Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public static Bitmap encodeToQrCode(String text, int width, int height) {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = null;
        try {
            matrix = writer.encode(text, BarcodeFormat.QR_CODE, SIDE, SIDE);
        } catch (WriterException ex) {
            ex.printStackTrace();
        }
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                bmp.setPixel(x, y, matrix.get(x, y) ? Color.BLACK : Color.WHITE);
            }
        }
        return bmp;
    }





}
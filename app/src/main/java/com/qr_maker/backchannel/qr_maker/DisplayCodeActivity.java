package com.qr_maker.backchannel.qr_maker;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class DisplayCodeActivity extends AppCompatActivity {

    final int REQ_IMG_CAP = 1;
    private static int SIDE = 700;
    private static int QRSIZE = 2000;
    int TIME = 800;

    CountDownTimer timer;

    ImageView qrImage;
    Button restart;
    Button takePhoto;
    AppCompatButton symantec;
    AppCompatButton polymail;
    AppCompatButton kitten;
    Bitmap photo;
    int numOfQRCodes;

    ArrayList<String> chunks;
    ArrayList<Bitmap> codes = new ArrayList<>();


    Intent pastIntent = getIntent();

    //byte[] byteArray = pastIntent.getByteArrayExtra("UserPhoto");
    //Bitmap UserPhoto = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

    ProgressDialog encoding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_code);

        getSupportActionBar().setElevation(0);

        //Inflate qr Image
        qrImage = (ImageView) findViewById(R.id.qrImage);
        restart = (Button) findViewById(R.id.restart);
        takePhoto = (Button) findViewById(R.id.photo);

        symantec = (AppCompatButton) findViewById(R.id.symantec);
        polymail = (AppCompatButton) findViewById(R.id.polymail);
        kitten = (AppCompatButton) findViewById(R.id.kitten);


        ColorStateList colorStateList = new ColorStateList(new int[][] {{0}}, new int[] {0xFF27ae60}); // 0xAARRGGBB
        symantec.setSupportBackgroundTintList(colorStateList);
        polymail.setSupportBackgroundTintList(colorStateList);
        kitten.setSupportBackgroundTintList(colorStateList);



        symantec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codes.clear();
                Restart(R.drawable.symantec);
            }
        });

        polymail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codes.clear();
                Restart(R.mipmap.icon);
            }
        });

        kitten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codes.clear();
                Restart(R.mipmap.icon);
            }
        });


        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restart.setClickable(false);

            }
        });

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQ_IMG_CAP);
                }
            }
        });
    }


    void Restart(final int icon) {


        if (codes.size() == 0) {

            String imgStr;
            Bitmap moonBitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), icon);
            imgStr = BitMapToString(moonBitmap);

            // Get Number of characters in string
            int imgStrLen = imgStr.length();

            // Calculate Number of QR Codes Necessary
            numOfQRCodes = (int) Math.ceil(imgStrLen / ((double) QRSIZE));

            // Split
            chunks = Lists.newArrayList(Splitter.fixedLength(QRSIZE).split(imgStr));

            codes.add(encodeToQrCode(String.format("%02d", 0) + (numOfQRCodes), SIDE, SIDE));

            for (int i = 1; i < numOfQRCodes + 1; i++) {
                codes.add(encodeToQrCode(String.format("%02d", i) + " " + chunks.get(i - 1), SIDE, SIDE));
                Log.d("bitmap encoded", String.valueOf(i));
            }
        }

        if(timer != null){
            timer.cancel();
        }

        timer = new CountDownTimer((numOfQRCodes + 2) * TIME, TIME) {
            int index = 0;

            @Override
            public void onTick(long millisUntilFinished) {

                if (index < numOfQRCodes + 1) {
                    qrImage.setImageBitmap(codes.get(index));
                    Log.d("index value", String.valueOf(index));
                    index++;
                } else {
                    index = 0;
                }
            }

            @Override
            public void onFinish() {
                Restart(icon);
            }
        };

        timer.start();
    }


    @Override
    protected void onPause() {
        super.onPause();

        timer.cancel();
    }


    @Override
    protected void onStop() {
        super.onStop();

        timer.cancel();
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream ByteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, ByteStream);
        byte[] b = ByteStream.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_IMG_CAP && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            photo = cropCenter((Bitmap) extras.get("data"));

        }
    }


    public static Bitmap cropCenter(Bitmap bmp) {
        int dimension = Math.min(bmp.getWidth(), bmp.getHeight());
        return ThumbnailUtils.extractThumbnail(bmp, dimension, dimension);
    }
}
package com.qr_maker.backchannel.qr_maker;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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

public class MainActivity extends AppCompatActivity {

    //My code
    Button sButton;
    EditText mEdit;
   // public final static String MAPoBITS = "com.backchannel.qr_maker.MAP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sButton = (Button)findViewById(R.id.button);
        mEdit = (EditText)findViewById(R.id.editText);

        sButton.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {
                        Log.v("EditText", mEdit.getText().toString());
                        Intent intent = new Intent(MainActivity.this, DisplayCodeActivity.class);
                        EditText editText = (EditText)findViewById(R.id.editText);
                       // Bitmap map = ;
                       // intent.putExtra(MAPoBITS, map);
                        startActivity(intent);

                    }
                }
        );
    }

}

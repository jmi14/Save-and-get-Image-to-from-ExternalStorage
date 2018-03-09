package com.example.intagpc.saveimagetoexternalstorage;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Button btnSave, btnGet;
    private ImageView imageView;
    private File imageName = null;
    private File dir;
    private String myPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
        initWidgets();
        callListener();
    }

    public void initWidgets() {
        btnSave = (Button) findViewById(R.id.saveImg);
        btnGet = (Button) findViewById(R.id.getImg);
        imageView = (ImageView) findViewById(R.id.imageSave);
    }

    public void callListener() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {


                } else {

                    Drawable imageDrawable = getResources().getDrawable(R.drawable.ic_launcher_background);
                    Bitmap bitmap = drawableToBitmap(imageDrawable);
                    saveImage(bitmap);
                }
            }
        });
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                getImageFromStorage();
            }


        });

    }

    public void saveImage(Bitmap bitmap) {
        myPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
        dir = new File(myPath + "/mynewDirectory");
        dir.mkdir();
        imageName = new File(dir + "/img" + ".png");
        try {
            FileOutputStream fout = new FileOutputStream(imageName);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
            fout.flush();
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MediaScannerConnection.scanFile(MainActivity.this, new String[]{imageName.toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
            @Override
            public void onScanCompleted(String s, Uri uri) {

            }
        });


    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public void getImageFromStorage() {
        try {
            myPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            dir = new File(myPath + "/mynewDirectory");
            imageName = new File(dir + "/img" + ".png");

            FileInputStream fileInputStream = new FileInputStream(imageName);
            Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
            imageView.setImageBitmap(bitmap);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}


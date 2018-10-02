package com.example.user.testsendfileretrofit.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.testsendfileretrofit.R;
import com.example.user.testsendfileretrofit.net.PhotoPayRecognizer;
import com.example.user.testsendfileretrofit.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import lombok.val;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    private static final int PERMISSIONS_REQUEST_CAMERA = 1;
    private static final int PERMISSIONS_WRITE_STORAGE = 2;
    public static final int PICK_CAMERA_IMAGE = 102;

    private PhotoPayRecognizer photoPayRecognizer;
    private Uri tempFile;

    private View content;
    private TextView path;
    private ImageView imageView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        photoPayRecognizer = new PhotoPayRecognizer();

        content = findViewById(R.id.content);
        path = findViewById(R.id.textView);
        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);
        findViewById(R.id.button).setOnClickListener(view -> {
            path.setText("Текст ответа...");
            pickPhotoCamera();
        });
    }

    public void pickPhotoCamera() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_WRITE_STORAGE);
        } else {
            checkPermissionCamera();
        }
    }

    private void checkPermissionCamera() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CAMERA);
        } else {
            successOpenCameraPermission();
        }
    }

    private void successOpenCameraPermission() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                String imageFileName = "JPEG_" + new SimpleDateFormat("yyyyMMdd_HHmmss", new Locale("ru")).format(new Date()) + "_";
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                photoFile = File.createTempFile(imageFileName, ".jpg", storageDir);
            } catch (IOException ex) {
                Log.d(TAG, ex.getMessage());
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        getPackageName() + ".fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, PICK_CAMERA_IMAGE);
                tempFile = photoURI;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_WRITE_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissionCamera();
                }
                break;
            case PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    successOpenCameraPermission();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PICK_CAMERA_IMAGE:
                onSelected(tempFile);
                break;
        }
    }

    public void onSelected(Uri uri) {
        val d = photoPayRecognizer.uploadFile(Utils.readBytesFromUri(uri, getContentResolver()))
                .doOnSubscribe(disposable -> progressBar.setVisibility(View.VISIBLE))
                .doFinally(() -> progressBar.setVisibility(View.GONE))
                .subscribe(responseBody -> {
                    Glide
                            .with(this)
                            .load("https://vtb.itfb.group:10012/recognition-service" + responseBody.getData().getResultRecognizeImage().getUrl())
                            .into(imageView);
                    path.setText(formatString(responseBody.toString()));
                }, t -> path.setText(t.getMessage()));
    }

    public static String formatString(String text) {
        StringBuilder json = new StringBuilder();
        String indentString = "";

        for (int i = 0; i < text.length(); i++) {
            char letter = text.charAt(i);
            switch (letter) {
                case '{':
                case '[':
                    json.append("\n").append(indentString).append(letter).append("\n");
                    indentString = indentString + "\t";
                    json.append(indentString);
                    break;
                case '}':
                case ']':
                    indentString = indentString.replaceFirst("\t", "");
                    json.append("\n").append(indentString).append(letter);
                    break;
                case ',':
                    json.append(letter).append("\n").append(indentString);
                    break;

                default:
                    json.append(letter);
                    break;
            }
        }

        return json.toString();
    }
}

package com.example.myaccountbook;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.myaccountbook.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity3 extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PICK_IMAGE = 2;
    private static final int REQUEST_PERMISSION_CODE = 101;
    private String currentPhotoPath;
    private Uri photoUri;

    ImageView 이미지1,이미지2,이미지3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        Button pickImageBtn = findViewById(R.id.button22);

        pickImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromGallery();
            }
        });

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_CODE);
        }

        photoUri = Uri.parse("file:///storage/emulated/0/Android/data/com.example.myaccountbook/files/Pictures/JPEG_20240103_175935_832471153044161471.jpg");
//        이미지1.setImageURI(photoUri);
//        이미지2.setImageURI(photoUri);
//        이미지3.setImageURI(photoUri);

        LinearLayout layout = findViewById(R.id.linearLayout9);

        for(int i=0;i<3;i++){
            View view = LayoutInflater.from(layout.getContext()).inflate(R.layout.image_item, layout, false);
            ImageButton button = view.findViewById(R.id.imageButton8);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layout.removeView(view);
                }
            });
            ImageView 이미지 = view.findViewById(R.id.imageView4);
            이미지.setImageURI(photoUri);

            layout.addView(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(MainActivity3.this,ImagePageActivity.class);
                    startActivity(intent);
                }
            });
        }



    }

    private void pickImageFromGallery() {
        Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (pickImageIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pickImageIntent, REQUEST_PICK_IMAGE);
        } else {
            Toast.makeText(this, "갤러리 앱을 열 수 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    saveImage(selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void saveImage(Uri selectedImageUri) throws IOException {
        File imageFile = createImageFile();
        photoUri = Uri.fromFile(imageFile);

        // 이미지 복사 및 저장
        ContentResolver resolver = getContentResolver();
        InputStream inputStream = resolver.openInputStream(selectedImageUri);
        OutputStream outputStream = new FileOutputStream(imageFile);

        byte[] buf = new byte[1024];
        int len;
        while ((len = inputStream.read(buf)) > 0) {
            outputStream.write(buf, 0, len);
        }

        inputStream.close();
        outputStream.close();
        이미지1.setImageURI(photoUri);
        이미지2.setImageURI(photoUri);
        이미지3.setImageURI(photoUri);
//        file:///storage/emulated/0/Android/data/com.example.myaccountbook/files/Pictures/JPEG_20240103_175935_832471153044161471.jpg
        Log.i("",photoUri.toString());


        Toast.makeText(this, "이미지가 성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }
}

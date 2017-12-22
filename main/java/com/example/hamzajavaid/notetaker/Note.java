package com.example.hamzajavaid.notetaker;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

    public class Note extends AppCompatActivity {
        protected static final int REQ_CODE_SPEECH_INPUT1 = 1;
        private final int REQ_CODE_SPEECH_INPUT2 = 102;
        private static final int CAMERA_REQUEST = 1888;
        private ImageView imageView;
        EditText et,et2;
        MyDbHandler mydb;
        ImageView btnspeak,btnspeak1;
        Bitmap photo;
        ImageConversion imageconversion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        et = (EditText)findViewById(R.id.title);
        et2 = (EditText)findViewById(R.id.description);
        btnspeak = (ImageView)findViewById(R.id.titlespeak);
        btnspeak1 = (ImageView)findViewById(R.id.descriptionspeak);
        imageView = (ImageView)findViewById(R.id.imageView);

        if(getIntent().getExtras() != null){
            String title = (String) getIntent().getExtras().get("TITLE");
            String description = (String) getIntent().getExtras().get("DESCRIPTION");
            et.setText(title);
            et2.setText(description);
            if(getIntent().getExtras().get("IMAGE")!=null){
                imageView.setImageBitmap(ImageConversion.bytesToBitmap((byte[]) getIntent().getExtras().get("IMAGE")));
            }
        }
        mydb = new MyDbHandler(this, null, null, 1);
        imageconversion=  new ImageConversion();
        btnspeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {
                    startActivityForResult(intent, REQ_CODE_SPEECH_INPUT1);
                    et.setText("");
                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(getApplicationContext(),
                            "Opps! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });
        btnspeak1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(
                        RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");

                try {
                    startActivityForResult(intent, REQ_CODE_SPEECH_INPUT2);
                    et2.setText("");
                } catch (ActivityNotFoundException a) {
                    Toast t = Toast.makeText(getApplicationContext(),
                            "Opps! Your device doesn't support Speech to Text",
                            Toast.LENGTH_SHORT);
                    t.show();
                }
            }
        });

    }

        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            switch (requestCode) {
                case REQ_CODE_SPEECH_INPUT1: {
                    if (resultCode == RESULT_OK && null != data) {

                        ArrayList<String> text = data
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                        et.setText(text.get(0));
                    }
                    break;
                }
                case REQ_CODE_SPEECH_INPUT2: {
                    if (resultCode == RESULT_OK && null != data) {

                        ArrayList<String> result = data
                                .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        et2.setText(result.get(0));
                    }
                    break;
                }
                case CAMERA_REQUEST: {
                    if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                        photo = (Bitmap) data.getExtras().get("data");
                        imageView.setImageBitmap(photo);
                    }
                }
            }
        }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_note,menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_main_new_save:
                final String titlee = et.getText().toString();
                final String descr = et2.getText().toString();
                if(titlee.toString().trim().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Title field is required",Toast.LENGTH_SHORT).show();
                    break;
                }
                if(getIntent().getExtras() != null && getIntent().getExtras().getBoolean("UPDATE")){

                      mydb.updateNote(titlee, descr,photo, getIntent().getExtras().get("TITLE").toString(),
                                getIntent().getExtras().get("DESCRIPTION").toString(),
                                getIntent().getExtras().getByteArray("IMAGE"));

                }
                else {
                    mydb.addnote(titlee,descr,photo);
                     }
                Intent i = new Intent(Note.this,MainActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.action_camera:
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
                break;
        }
        return true;
    }
}

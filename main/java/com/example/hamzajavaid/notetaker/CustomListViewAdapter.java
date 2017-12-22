package com.example.hamzajavaid.notetaker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.google.gson.internal.UnsafeAllocator.create;


public class CustomListViewAdapter extends BaseAdapter{

    private ArrayList<NoteItem> noteItemArrayList;
    private Activity activity;
    private ImageView imageView;
    //private int pos;
    public CustomListViewAdapter(ArrayList<NoteItem> noteItemArrayList, Activity activity){
        this.noteItemArrayList = noteItemArrayList;
        this.activity = activity;
    }
    @Override
    public int getCount() {
        return noteItemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return noteItemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        LayoutInflater inflater = ( LayoutInflater )activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.note_item, null);

        TextView tv = view.findViewById(R.id.noteTitle);
        TextView tv2 = view.findViewById(R.id.noteDescription);
        imageView = view.findViewById(R.id.imageView2);
        if(noteItemArrayList.get(position).getImage()==null){
            imageView.setVisibility(View.GONE);
        }else{
            imageView.setImageBitmap(noteItemArrayList.get(position).getImage());
        }
        tv.setText(noteItemArrayList.get(position).getTitle());
        tv2.setText(noteItemArrayList.get(position).getDescription());
        ImageButton button1 = view.findViewById(R.id.imageButton2);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(parent.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_box);
                TextView text = (TextView) dialog.findViewById(R.id.textView);
                Button delete = (Button) dialog.findViewById(R.id.delete);
                Button cancel = (Button) dialog.findViewById(R.id.cancel);
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v){
                        String data = noteItemArrayList.get(position).getTitle();
                        noteItemArrayList.remove(position);
                        notifyDataSetChanged();
                        Thread thread = new Thread(new RemoveItemThread(data));
                        thread.start();
                        dialog.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }

        });

        ImageButton button2 = view.findViewById(R.id.imageButton3);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = noteItemArrayList.get(position).getTitle();
                String description = noteItemArrayList.get(position).getDescription();
                Bitmap image = noteItemArrayList.get(position).getImage();
                Intent intent = new Intent(activity, Note.class);
                intent.putExtra("TITLE", title);
                intent.putExtra("DESCRIPTION", description);
                intent.putExtra("UPDATE", true);
                if(image!=null){
                    intent.putExtra("IMAGE",ImageConversion.getBitmapAsByteArray(image));
                }
                activity.startActivity(intent);
                activity.finish();
            }
        });
        return view;
    }
    public class RemoveItemThread implements Runnable{
        private MyDbHandler myDbHandler;
        private String data;

        public RemoveItemThread(String data){
            this.data = data;
        }

        @Override
        public void run() {
            myDbHandler = new MyDbHandler(activity.getApplicationContext(), null, null, 1);
            myDbHandler.deletenote(data);
        }
    }
}

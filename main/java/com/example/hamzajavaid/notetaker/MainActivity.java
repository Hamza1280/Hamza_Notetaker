package com.example.hamzajavaid.notetaker;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //ListView mlistview;
    MyDbHandler mydb;
    CustomListViewAdapter customListViewAdapter;
    private ArrayList<NoteItem> noteItemArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mydb = new MyDbHandler(this, null, null, 1);
        //mlistview = (ListView) findViewById(R.id.mainlistview);
        final ListFragment listFragment = new ListFragment();
        final FragmentManager fm = getFragmentManager();
        FragmentTransaction ft  = fm.beginTransaction();
        ft.replace(R.id.frameLayout,listFragment);
        ft.commit();
//        ft = fm.beginTransaction();
//        ft.replace(R.id.frameLayout,listFragment);
//        ft.commit();
////        ArrayAdapter<String> arrayAdapter  =new ArrayAdapter<String>(this,android.R.layout.simple_list_item_2,mydb.getData());
           noteItemArrayList = mydb.getData();
           customListViewAdapter = new CustomListViewAdapter(noteItemArrayList, this);
//        mlistview.setAdapter(customListViewAdapter);
//        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(MainActivity.this, noteItemArrayList.get(position).getTitle(), Toast.LENGTH_LONG).show();
//                Toast.makeText(MainActivity.this, noteItemArrayList.get(position).getDescription(), Toast.LENGTH_LONG).show();
//            }
//        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note,menu);
        return true;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_main_new_note:
                Toast.makeText(MainActivity.this,"Create new Note",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(MainActivity.this,Note.class);
                startActivity(i);
                finish();
                break;
            case R.id.action_main_new_delete:
                Log.i("show","show");
                Toast.makeText(MainActivity.this,"delete",Toast.LENGTH_SHORT).show();
                Log.i("bad","badeeeeee");
                mydb.deleteAll();
                final ListFragment listFragment = new ListFragment();
                final FragmentManager fm = getFragmentManager();
                FragmentTransaction ft  = fm.beginTransaction();
                ft.replace(R.id.frameLayout,listFragment);
                ft.commit();
                break;
            case R.id.action_main_new_exit:
                //Toast.makeText(MainActivity.this,"exit",Toast.LENGTH_SHORT).show();
                System.exit(0);
                break;
        }
        return true;
    }
}

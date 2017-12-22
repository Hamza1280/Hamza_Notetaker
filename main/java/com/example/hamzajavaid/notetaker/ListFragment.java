package com.example.hamzajavaid.notetaker;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static com.example.hamzajavaid.notetaker.R.attr.title;

/**
 * Created by Hamza Javaid on 12/4/2017.
 */

public class ListFragment extends Fragment{
    CustomListViewAdapter customListViewAdapter;
    private ArrayList<NoteItem> noteItemArrayList;
    MyDbHandler mydb;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        final ListView listview =(ListView)view.findViewById(R.id.mainlistview);
        mydb = new MyDbHandler(getActivity(), null, null, 1);
        noteItemArrayList = mydb.getData();
        final ImageConversion imageConversion = new ImageConversion();
        customListViewAdapter = new CustomListViewAdapter(noteItemArrayList,getActivity());
        listview.setAdapter(customListViewAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 //ListFragment listFragment = new ListFragment();
                final DetailFragment detailFragment = new DetailFragment();
                final FragmentManager fm = getFragmentManager();
                FragmentTransaction ft  = fm.beginTransaction();
                ft.replace(R.id.frameLayout,detailFragment);
                ft.addToBackStack(null);
                ft.commit();
                Bundle bundle = new Bundle();
                String title = noteItemArrayList.get(position).getTitle();
                String desc = noteItemArrayList.get(position).getDescription();
                Bitmap image = noteItemArrayList.get(position).getImage();
                byte[] imagesend = null;
                if(image!=null){
                    imagesend = imageConversion.getBitmapAsByteArray(image);
                    bundle.putString("title", title);
                    bundle.putString("descc", desc);
                    bundle.putByteArray("image",imagesend);
                    detailFragment.setArguments(bundle);
                }
                else
                {
                    bundle.putString("title", title);
                    bundle.putString("descc", desc);
                    detailFragment.setArguments(bundle);
                }
            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String titlesend = noteItemArrayList.get(position).getTitle();
                String descriptionsend = noteItemArrayList.get(position).getDescription();
                sendIntent.putExtra(Intent.EXTRA_TEXT,"Title: "+titlesend+"\n"+"Description: "+descriptionsend);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;
            }
        });
        return view;
    }
}


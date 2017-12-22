package com.example.hamzajavaid.notetaker;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Hamza Javaid on 12/14/2017.
 */

public class DetailFragment extends Fragment {
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment, container, false);
          TextView titlee = (TextView)view.findViewById(R.id.textView2);
          TextView desccc = (TextView)view.findViewById(R.id.textView4);
          ImageView imageView = (ImageView)view.findViewById(R.id.imageView3);
        ImageConversion imageConversion = new ImageConversion();
          String titleee = getArguments().getString("title");
          String description = getArguments().getString("descc");
          byte[] result = getArguments().getByteArray("image");
          Bitmap bitmap = imageConversion.bytesToBitmap(result);
          titlee.setText(titleee);
          desccc.setText(description);
          imageView.setImageBitmap(bitmap);
        return view;
    }
    }


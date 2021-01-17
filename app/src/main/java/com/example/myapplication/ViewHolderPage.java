package com.example.myapplication;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.Map;
import java.util.Set;

public class ViewHolderPage extends RecyclerView.ViewHolder {
    private ImageView imageView;
    private TextView textView;
    private TextView tvHidden;
    Map<String, String> data;
    public ViewHolderPage(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.imageView);
        textView = itemView.findViewById(R.id.tvViewPage);
        tvHidden = itemView.findViewById(R.id.tvHiddenId);


    }

    public void onBind(Map<String, String> data){
        this.data = data;

        textView.setText(data.get("f_nick"));

        Set set = data.keySet();

        for (Object o : set) {
            Log.d("ddd", o.toString());
        }



        switch(data.get("f_shape")){
            case "pot1":
                imageView.setImageResource(R.drawable.pot1);

                break;
            case "pot2":
                imageView.setImageResource(R.drawable.pot2);
                break;
            case "pot3":
                imageView.setImageResource(R.drawable.pot3);
                break;
            case "pot4":
                imageView.setImageResource(R.drawable.pot4);
                break;
            case "pot5":
                imageView.setImageResource(R.drawable.pot5);
                break;
            case "pot6":
                imageView.setImageResource(R.drawable.pot6);
                break;
            case "pot7":
                imageView.setImageResource(R.drawable.pot7);
                break;


        }
    }
}
package com.example.dispmov.Chat;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dispmov.R;

public class HolderMsg  extends RecyclerView.ViewHolder{

    private ImageView img;
    private TextView name;
    private TextView msg;
    private TextView hour;

    public HolderMsg(@NonNull View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.msgName);
        msg = (TextView) itemView.findViewById(R.id.msgText);
        hour = (TextView) itemView.findViewById(R.id.msgHour);
        img = (ImageView) itemView.findViewById(R.id.imageView);
    }


    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public TextView getMsg() {
        return msg;
    }

    public void setMsg(TextView msg) {
        this.msg = msg;
    }

    public TextView getHour() {
        return hour;
    }

    public void setHour(TextView hour) {
        this.hour = hour;
    }
    public ImageView getImg() {
        return img;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }

}

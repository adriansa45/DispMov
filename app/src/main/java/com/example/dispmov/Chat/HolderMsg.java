package com.example.dispmov.Chat;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dispmov.R;

public class HolderMsg  extends RecyclerView.ViewHolder{

    private TextView name;
    private TextView msg;
    private TextView hour;

    public HolderMsg(@NonNull View itemView) {
        super(itemView);
        name = (TextView) itemView.findViewById(R.id.msgName);
        msg = (TextView) itemView.findViewById(R.id.msgText);
        hour = (TextView) itemView.findViewById(R.id.msgHour);
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

    public TextView getHora() {
        return hour;
    }

    public void setHora(TextView hour) {
        this.hour = hour;
    }


}
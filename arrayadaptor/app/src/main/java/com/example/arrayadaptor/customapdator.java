package com.example.arrayadaptor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class customapdator extends ArrayAdapter {
    private List arr = new ArrayList<String>();

    public customapdator(@NonNull Context context, int resource, List arr) {
        super(context, resource,arr);
        this.arr=arr;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return  arr.get(position).toString();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_customapdator,parent,false);
        CheckBox t = convertView.findViewById(R.id.checkBox);
        t.setText(getItem(position));
        return convertView;
    }
}
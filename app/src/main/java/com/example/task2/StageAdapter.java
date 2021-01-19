package com.example.task2;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class StageAdapter extends ArrayAdapter<Pair<String, Integer>>
{
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<Pair<String, Integer>> items;
    public StageAdapter(Context context, int resource, ArrayList<Pair<String, Integer>> items)
    {
        super(context, resource, items);
        this.inflater = LayoutInflater.from(context);
        this.layout = resource;
        this.items = items;
    }

    @SuppressLint("SetTextI18n")
    public View getView(int pos, View convertView, ViewGroup parent)
    {
        View view=inflater.inflate(this.layout, parent, false);
        TextView stageItem = (TextView) view.findViewById(R.id.stageItem);
        Pair<String, Integer> item = items.get(pos);
        stageItem.setText(" "+(pos+1)+". "+item.first + " - " + item.second);
        return view;
    }
}

package com.example.task2;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;

public class StageAdapter extends ArrayAdapter<Pair<String, Integer>>
{
    private LayoutInflater inflater;
    private int layout;
    private ArrayList<Pair<String, Integer>> items;
    private Context context;
    public StageAdapter(Context context, int resource, ArrayList<Pair<String, Integer>> items)
    {
        super(context, resource, items);
        this.inflater = LayoutInflater.from(context);
        this.layout = resource;
        this.items = items;
        this.context = context;
    }
    @SuppressLint("SetTextI18n")
    public View getView(int pos, View convertView, ViewGroup parent)
    {
        @SuppressLint("ViewHolder") View view=inflater.inflate(this.layout, parent, false);
        TextView stageItem = (TextView) view.findViewById(R.id.stageItem);
        Pair<String, Integer> item = items.get(pos);
        stageItem.setText(" "+(pos+1)+". "+item.first + " - " + item.second);
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this.context);
        if (prefs.getBoolean(context.getString(R.string.pref_dark_theme), false))
        {
            stageItem.setBackgroundResource(R.drawable.night_theme);
            stageItem.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        }
        try
        {
            float fontSize = Float.parseFloat(prefs.getString(
                    context.getString(R.string.pref_font_size), "0"));
            stageItem.setTextSize(14+fontSize);

        }
        catch(Exception ignored){

        }
        return view;
    }
}

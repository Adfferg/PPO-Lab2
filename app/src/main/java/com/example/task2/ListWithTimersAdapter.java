package com.example.task2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;

import androidx.preference.PreferenceManager;

import java.util.List;

public class ListWithTimersAdapter extends ArrayAdapter<TimerSequence> {
    private LayoutInflater inflater;
    private int layout;
    private List<TimerSequence> sequenceList;
    private Context context;
    private DbItemDeleteHelper dbItemDeleteHelper;

    ListWithTimersAdapter(Context context, int resource, List<TimerSequence> sequences)
    {
        super(context, resource, sequences);
        this.sequenceList = sequences;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        dbItemDeleteHelper = new DbItemDeleteHelper(context);
    }
    public View getView(int pos, View convertView, ViewGroup parent)
    {

        final ItemInListView itemInListView;
        if(convertView==null)
        {
            convertView = inflater.inflate(this.layout, parent, false);
            itemInListView = new ItemInListView(convertView);
            convertView.setTag(itemInListView);
        }
        else{
            itemInListView = (ItemInListView) convertView.getTag();
        }
        final TimerSequence sequence = sequenceList.get(pos);

        itemInListView.itemTextView.setText(sequence.name);

        itemInListView.itemWithTimersLinearLayout.setBackgroundColor(sequence.color);
        itemInListView.itemWithTimersLinearLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                    Intent intent = new Intent(context, TimerActivity.class);
                    intent.putExtra("id", sequence.id);
                    context.startActivity(intent);
            }
        });
        itemInListView.deleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbItemDeleteHelper.deleteItem(sequence.id);
                context.startActivity(new Intent(context, MainActivity.class));
            }
        });
        itemInListView.changeItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddTimer.class);
                intent.putExtra("id", sequence.id);
                context.startActivity(intent);
            }
        });
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        try
        {
            float fontSize = Float.parseFloat(prefs.getString(
                    context.getString(R.string.pref_font_size), "0"));
            itemInListView.itemTextView.setTextSize(18+fontSize);
        }
        catch (Exception ignored)
        {

        }
        return convertView;
    }
}

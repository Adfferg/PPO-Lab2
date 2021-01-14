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

import java.util.List;

public class ListWithTimersAdapter extends ArrayAdapter<TimerSequence> {
    private LayoutInflater inflater;
    private int layout;
    private List<TimerSequence> sequenceList;
    private Context context;
    private DbAdapter adapter;

    ListWithTimersAdapter(Context context, int resource, List<TimerSequence> sequences)
    {
        super(context, resource, sequences);
        this.sequenceList = sequences;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        adapter = new DbAdapter(context);
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
        itemInListView.deleteItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.Open();
                adapter.DeleteItem(sequence.id);
                adapter.Close();
                context.startActivity(new Intent(context, MainActivity.class));
            }
        });
        itemInListView.changeItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddTimer.class);
                intent.putExtra("id", sequence.id);
                intent.putExtra("click", 25);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}

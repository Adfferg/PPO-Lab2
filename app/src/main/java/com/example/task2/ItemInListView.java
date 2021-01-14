package com.example.task2;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ItemInListView {
    final TextView itemTextView;
    final LinearLayout itemWithTimersLinearLayout;
    final Button changeItemButton,deleteItemButton;
    ItemInListView(View view)
    {
        itemTextView = view.findViewById(R.id.itemTextView);
        itemWithTimersLinearLayout = view.findViewById(R.id.itemWithTimersLinearLayout);
        changeItemButton =  view.findViewById(R.id.changeItemButton);
        deleteItemButton =  view.findViewById(R.id.deleteItemButton);
    }
}

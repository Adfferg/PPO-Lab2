package com.example.task2;

import android.content.Context;

public class DbItemDeleteHelper {
    private DbAdapter dbAdapter;
    DbItemDeleteHelper(Context context){
        dbAdapter = new DbAdapter(context);
    }
    public void deleteItem(int id){
        dbAdapter.Open();
        dbAdapter.DeleteItem(id);
        dbAdapter.Close();
    }
}

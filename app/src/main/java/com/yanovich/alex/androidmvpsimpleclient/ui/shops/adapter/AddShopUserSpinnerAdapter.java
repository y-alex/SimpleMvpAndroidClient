package com.yanovich.alex.androidmvpsimpleclient.ui.shops.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.yanovich.alex.androidmvpsimpleclient.R;
import com.yanovich.alex.androidmvpsimpleclient.data.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 15.05.2016.
 */
public class AddShopUserSpinnerAdapter extends ArrayAdapter<User>{
    private List<User> mListUsers = new ArrayList<>();
    private Context mContext;
    public AddShopUserSpinnerAdapter(Context context, int resource, List<User> objects) {
        super(context, resource, objects);
        mListUsers = objects;
        mContext = context;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getUserView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {

       return getUserView(position,convertView,parent);
    }

    private View getUserView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.item_user, parent,
                false);
        TextView userName = (TextView) row.findViewById(R.id.text_name);
        TextView userLastName = (TextView) row.findViewById(R.id.text_last_name);
        userName.setText(mListUsers.get(position).mUserName);
        userLastName.setText(mListUsers.get(position).mUserLastName);
        return row;
    }

    public void updateUserList(List<User> list){
        mListUsers = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mListUsers.size();
    }

    @Override
    public User getItem(int position) {
        return mListUsers.get(position);
    }
}

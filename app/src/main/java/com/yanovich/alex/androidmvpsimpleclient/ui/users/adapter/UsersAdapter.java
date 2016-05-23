package com.yanovich.alex.androidmvpsimpleclient.ui.users.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Bus;
import com.yanovich.alex.androidmvpsimpleclient.R;
import com.yanovich.alex.androidmvpsimpleclient.data.model.Shop;
import com.yanovich.alex.androidmvpsimpleclient.data.model.User;
import com.yanovich.alex.androidmvpsimpleclient.injection.ApplicationContext;

import java.util.ArrayList;
import java.util.List;


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by Alex on 27.04.2016.
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {
    private List<User> mUsers = new ArrayList<>();
    private Bus mBus;
    @Inject
    public UsersAdapter(@ApplicationContext Context context, Bus eventBus) {
      mBus = eventBus;
    }

    public void setUsers(List<User> users) {
        mUsers = users;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User user = mUsers.get(position);
        holder.nameTextView.setText(String.format("%s %s",
               user.mUserName, user.mUserLastName));

        StringBuilder shopsNames = new StringBuilder("");
        for(Shop s :user.mShops) {
            shopsNames.append(s.mShopName);
            shopsNames.append('\n');
        }
        holder.shopsNamesTextView.setText(shopsNames);
        holder.lastNameTextView.setText(user.mUserAdress);

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.view_hex_color) View hexColorView;
        @BindView(R.id.text_name) TextView nameTextView;
        @BindView(R.id.text_last_name) TextView lastNameTextView;
        @BindView(R.id.text_shops_names) TextView shopsNamesTextView;

        public UserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Timber.i("User item was clicked pos:+" + getAdapterPosition());
            mBus.post(mUsers.get(getAdapterPosition()));
        }
    }
}

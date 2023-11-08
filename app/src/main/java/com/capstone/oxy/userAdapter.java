package com.capstone.oxy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.List;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class userAdapter extends RecyclerView.Adapter<userAdapter.MyViewHolder> {


    private Context context;
    private List<QueryDocumentSnapshot> list;

    public userAdapter (Context context, List<QueryDocumentSnapshot> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.items_userslist, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        QueryDocumentSnapshot document = list.get(position);
        String userName = document.getString("Username");
        String email = document.getString("Email");
        holder.userName.setText(userName);
        holder.eMail.setText(email);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView userName, eMail;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = itemView.findViewById(R.id.userName);
            eMail = itemView.findViewById(R.id.email);
        }
    }


}

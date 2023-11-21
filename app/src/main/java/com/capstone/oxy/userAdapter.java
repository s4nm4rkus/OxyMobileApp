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

public class userAdapter extends RecyclerView.Adapter<userAdapter.UserViewHolder> {

    private Context context;
    private List<QueryDocumentSnapshot> userList;

    public userAdapter(Context context, List<QueryDocumentSnapshot> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.items_userslist, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        if (position >= 0 && position < userList.size()) {
            QueryDocumentSnapshot userSnapshot = userList.get(position);

            String username = userSnapshot.getString("Username");
            String email = userSnapshot.getString("Email");

            // Check for null before setting values
            if (username != null) {
                holder.userName.setText(username);
            }

            if (email != null) {
                holder.email.setText(email);
            }
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userName, email;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            email = itemView.findViewById(R.id.email);
        }
    }
}

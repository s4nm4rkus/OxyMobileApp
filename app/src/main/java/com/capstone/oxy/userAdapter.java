package com.capstone.oxy;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
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
    public void onBindViewHolder(@NonNull UserViewHolder holder, @SuppressLint("RecyclerView") int position) {
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


            // Set OnClickListener for the delete button
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Delete User")
                            .setMessage("Would you like to delete this User Account?")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Get the document ID of the user to delete
                                    String documentId = userSnapshot.getId();

                                    // Delete user from Firestore
                                    FirebaseFirestore.getInstance().collection("users")
                                            .document(documentId)
                                            .delete()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Remove user from the list and update RecyclerView
                                                    userList.remove(position);
                                                    notifyItemRemoved(position);
                                                    notifyItemRangeChanged(position, userList.size());
                                                    Toast.makeText(context, "User account has been deleted successfully!    ", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Handle failure
                                                }
                                            });
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userName, email;
        ImageButton deleteButton;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            email = itemView.findViewById(R.id.email);
            deleteButton = itemView.findViewById(R.id.deleteBtn);
        }
    }
}


package com.example.projecttestconnection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRecylerViewMonitos extends RecyclerView.Adapter<MyRecylerViewMonitos.ViewHolder> {
    private List<User> monitorsList;
    private Context context;

    public MyRecylerViewMonitos(List<User> monitorsList, Context context) {
        this.monitorsList = monitorsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyleview_monitors, parent, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = monitorsList.get(position);
        holder.txtName.setText(user.getFirstName() + " " +user.getLastName());
        holder.txtPhone.setText(user.getPhone());
    }

    @Override
    public int getItemCount() {
        return monitorsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtName, txtPhone;

        public ViewHolder(View itemView){
            super(itemView);
            txtName = itemView.findViewById(R.id.name_monitor);
            txtPhone = itemView.findViewById(R.id.phone_monitor);
        }

    }
}

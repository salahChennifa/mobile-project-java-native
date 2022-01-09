package com.example.projecttestconnection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRecyclerViewAdapterSeanceSlr extends RecyclerView.Adapter<MyRecyclerViewAdapterSeanceSlr.ViewHolder>{
    private List<User>  monitorsList;
    private Context context;

    public MyRecyclerViewAdapterSeanceSlr(List<User> monitorList, Context context ){
        this.monitorsList = monitorList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyRecyclerViewAdapterSeanceSlr.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_list_seances_cls, parent, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewAdapterSeanceSlr.ViewHolder holder, int position) {
        User user = monitorsList.get(position);
        holder.txtName.setText(user.getFirstName() + " " +user.getLastName());
        holder.txtPhone.setText(user.getPhone());
        holder.txtEmail.setText(user.getEmail());
        holder.date_time.setText(user.getStartDate());
        if (user.getIsDone().equals("1")){
            holder.presance.setText("Present");
        }
        else {
            holder.presance.setText("Absent");
        }
        // TODO : how we can slove  the probleme of date;
    }

    @Override
    public int getItemCount() {
        return monitorsList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtName, txtPhone, txtEmail, date_time, presance;

        public ViewHolder(View itemView){
            super(itemView);
            txtName = itemView.findViewById(R.id.monitorName);
            txtPhone = itemView.findViewById(R.id.phoneMonSc);
            txtEmail = itemView.findViewById(R.id.emailMonSc);
            date_time = itemView.findViewById(R.id.DateMonSc);
            presance = itemView.findViewById(R.id.presence);
        }

    }

}

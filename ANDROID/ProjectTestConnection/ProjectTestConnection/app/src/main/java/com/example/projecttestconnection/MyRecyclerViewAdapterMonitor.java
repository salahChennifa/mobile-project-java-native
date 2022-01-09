package com.example.projecttestconnection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRecyclerViewAdapterMonitor extends RecyclerView.Adapter<MyRecyclerViewAdapterMonitor.ViewHolder> {
    static List<User> monitorsList;
    private Context context;

    public MyRecyclerViewAdapterMonitor(List<User> monitorsList, Context context ){
        this.context = context;
        this.monitorsList = monitorsList;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyle_list_pr_cl_mon,parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Client client = clientList.get(position);
        User monitor = monitorsList.get(position);
        holder.txtName.setText(monitor.getFirstName() + " " + monitor.getLastName());
        holder.txtPhone.setText(monitor.getPhone());
    }

    @Override
    public int getItemCount() {
        return monitorsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtName, txtPhone;

        public ViewHolder(View itemView){
            super(itemView);
            txtName = itemView.findViewById(R.id.nameCltMonitor);
            txtPhone = itemView.findViewById(R.id.phoneCltMonitor);
        }
    }
}

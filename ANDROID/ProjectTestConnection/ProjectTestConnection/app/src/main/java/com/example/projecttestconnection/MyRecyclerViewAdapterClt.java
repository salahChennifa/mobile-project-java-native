package com.example.projecttestconnection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRecyclerViewAdapterClt extends RecyclerView.Adapter<MyRecyclerViewAdapterClt.ViewHolder> {
    static public List<Client> clientList;
    private Context context;

    public MyRecyclerViewAdapterClt(List<Client> clientList, Context context) {
        this.clientList = clientList;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_list_clt, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( MyRecyclerViewAdapterClt.ViewHolder holder, int position) {
        Client client = clientList.get(position);
        holder.txtName.setText(client.getfName() + " " + client.getlName());
        holder.txtphone.setText(client.getClientPhone());

    }

    @Override
    public int getItemCount() {
        return clientList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView txtName, txtphone;


        public ViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.name1);
            txtphone = itemView.findViewById(R.id.phone1);
        }
    }

}

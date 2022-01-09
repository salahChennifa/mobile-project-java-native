package com.example.projecttestconnection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapterTaskSeance extends RecyclerView.Adapter<RecyclerViewAdapterTaskSeance.ViewHolder> {

    private List<Tache> tacheList;
    private Context context;

    public RecyclerViewAdapterTaskSeance(List<Tache> listTach, Context context) {
        tacheList = listTach;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_list_tasck_monitor, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tache  tache = tacheList.get(position);
        holder.titre.setText(tache.getTitre());
        holder.date.setText(tache.getDate_time());
    }

    @Override
    public int getItemCount() {
        return tacheList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView titre, date;
        public ViewHolder(View itemView){
            super(itemView);
            titre = itemView.findViewById(R.id.titretasksMonitor);
            date = itemView.findViewById(R.id.datetasksMonitor);
        }
    }
}

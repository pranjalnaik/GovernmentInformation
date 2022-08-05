package com.pjnaik.knowyourgovernment;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView;


public class GovernmentAdapter extends RecyclerView.Adapter<ViewHolder> {
    private MainActivity mainActivity;
    private ArrayList<com.pjnaik.knowyourgovernment.Government> govtarraylist;

    public GovernmentAdapter(MainActivity mainActivity, ArrayList<com.pjnaik.knowyourgovernment.Government> govtarraylist){
        this.mainActivity = mainActivity;
        this.govtarraylist = govtarraylist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewadapter = LayoutInflater.from(parent.getContext()).inflate(R.layout.government_list,parent,false);
        viewadapter.setOnClickListener(mainActivity);
        return new ViewHolder(viewadapter);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        com.pjnaik.knowyourgovernment.Government govtvar = govtarraylist.get(position);
        holder.jobrole.setText(govtvar.getJob());
        holder.name.setText(govtvar.getName() + " (" + govtvar.getParty() + ")");
    }

    @Override
    public int getItemCount() {
        return govtarraylist.size();
    }
}

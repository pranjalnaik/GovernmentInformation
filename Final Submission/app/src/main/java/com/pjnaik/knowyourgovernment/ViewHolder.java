package com.pjnaik.knowyourgovernment;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

class ViewHolder extends RecyclerView.ViewHolder {
     TextView jobrole;
     TextView name;
     ViewHolder(View view){
        super(view);
        jobrole = view.findViewById(R.id.jobrolemaintitleid);
        name = view.findViewById(R.id.jobrolenameid);
    }
}









































































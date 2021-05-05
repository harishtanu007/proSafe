package com.harish.prosafe.data.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harish.prosafe.R;
import com.harish.prosafe.data.model.Incident;

import java.util.List;

public class IncidentAdapter extends RecyclerView.Adapter<IncidentAdapter.ViewHolder> {

    private List<Incident> listData;

    public IncidentAdapter(List<Incident> listData) {
        this.listData = listData;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.incident,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Incident ld=listData.get(position);
        holder.title.setText(ld.getTitle());
        holder.description.setText(ld.getDescription());
        holder.postedBy.setText(ld.getPostedBy());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title,description,postedBy;
        public ViewHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.title);
            description=(TextView)itemView.findViewById(R.id.description);
            postedBy=(TextView)itemView.findViewById(R.id.posted_by);
        }
    }
}

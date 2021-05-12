package com.harish.prosafe.data.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.harish.prosafe.R;
import com.harish.prosafe.data.model.IncidentCategory;
import com.harish.prosafe.ui.newincident.NewIncidentActivity;

import java.util.List;

public class IncidentCategoryAdapter extends RecyclerView.Adapter<IncidentCategoryAdapter.ViewHolder> {

    private List<IncidentCategory> listData;
    private Context context;

    public IncidentCategoryAdapter(List<IncidentCategory> listData) {
        this.listData = listData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IncidentCategory ld = listData.get(position);
        holder.title.setText(ld.getName());
        holder.description.setText(ld.getDescription());
        holder.incidentCategoryView.setOnClickListener(v -> {
            IncidentCategory item = listData.get(position);
            Intent intent = new Intent(v.getContext(), NewIncidentActivity.class);
            intent.putExtra("INCIDENT_CATEGORY",item.getName());
            context.startActivity(intent);
            ((Activity)context).finish();
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, description;
        private CardView incidentCategoryView;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            incidentCategoryView=(CardView) itemView.findViewById(R.id.incident_category_view);
        }

    }
}

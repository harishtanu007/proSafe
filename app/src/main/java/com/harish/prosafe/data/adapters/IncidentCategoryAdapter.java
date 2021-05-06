package com.harish.prosafe.data.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harish.prosafe.R;
import com.harish.prosafe.data.model.Incident;
import com.harish.prosafe.data.model.IncidentCategory;
import com.harish.prosafe.ui.postincident.NewIncidentActivity;

import java.util.List;

public class IncidentCategoryAdapter extends RecyclerView.Adapter<IncidentCategoryAdapter.ViewHolder> {

    private List<IncidentCategory> listData;

    public IncidentCategoryAdapter(List<IncidentCategory> listData) {
        this.listData = listData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IncidentCategory ld = listData.get(position);
        holder.title.setText(ld.getName());
        holder.description.setText(ld.getDescription());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, description;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            itemView.setOnClickListener(v -> {
                int itemPosition = getAdapterPosition();
                IncidentCategory item = listData.get(itemPosition);
                Toast.makeText(v.getContext(), item.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), NewIncidentActivity.class);
                intent.putExtra("INCIDENT_CATEGORY",item.getName());
                v.getContext().startActivity(intent);
            });
        }
    }
}

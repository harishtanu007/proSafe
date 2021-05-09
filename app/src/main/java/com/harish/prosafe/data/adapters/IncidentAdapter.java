package com.harish.prosafe.data.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.harish.prosafe.R;
import com.harish.prosafe.data.model.Incident;
import com.harish.prosafe.data.model.IncidentCategory;
import com.harish.prosafe.ui.incidentdetails.IncidentDetailsActivity;
import com.harish.prosafe.ui.postincident.NewIncidentActivity;
import com.harish.prosafe.util.Constants;
import com.harish.prosafe.util.Helper;

import java.io.Serializable;
import java.util.List;

public class IncidentAdapter extends RecyclerView.Adapter<IncidentAdapter.ViewHolder> {

    private List<Incident> listData;

    public IncidentAdapter(List<Incident> listData) {
        this.listData = listData;
    }

    private Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.incident, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Incident ld = listData.get(position);
        holder.title.setText(ld.getTitle());
        holder.description.setText(ld.getDescription());
        holder.postedBy.setText(ld.getPostedBy());
        holder.category.setText(ld.getIncidentCategory());
        holder.time.setText(Helper.getTimeAgo(ld.getPostTime()));
        holder.incidentView.setOnClickListener(v -> {
            Incident incident = listData.get(position);
            Intent intent = new Intent(v.getContext(), IncidentDetailsActivity.class);
            intent.putExtra(Constants.INCIDENT_DATA_EXTRA, incident);
            context.startActivity(intent);
        });
        holder.options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v);
            }
        });
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.actions, popup.getMenu());
        popup.show();
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, description, postedBy, time, category;
        private CardView incidentView;
        private ImageView options;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            postedBy = itemView.findViewById(R.id.posted_by);
            category = itemView.findViewById(R.id.incident_category);
            time = itemView.findViewById(R.id.time);
            incidentView = itemView.findViewById(R.id.incident_view);
            options=itemView.findViewById(R.id.imageButton);
        }
    }
}

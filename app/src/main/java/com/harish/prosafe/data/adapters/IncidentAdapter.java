package com.harish.prosafe.data.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.harish.prosafe.R;
import com.harish.prosafe.data.model.Coordinates;
import com.harish.prosafe.data.model.Incident;
import com.harish.prosafe.ui.incidentdetails.IncidentDetailsActivity;
import com.harish.prosafe.util.Constants;
import com.harish.prosafe.util.Helper;
import com.harish.prosafe.util.IBackendProvider;

import java.text.DecimalFormat;
import java.util.List;

public class IncidentAdapter extends RecyclerView.Adapter<IncidentAdapter.ViewHolder> {

    private final List<Incident> listData;

    public IncidentAdapter(List<Incident> listData) {
        this.listData = listData;
    }
    IBackendProvider backendProvider;
    private Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.incident, parent, false);
        backendProvider =IBackendProvider.getBackendProvider();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Incident ld = listData.get(position);
        holder.title.setText(ld.getTitle());
        holder.description.setText(ld.getDescription());
        holder.postedBy.setText("Neighbor");
        backendProvider.getAddressValueEventListener(new AddressValueChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(Coordinates userCurrentCoordinates) {
                DecimalFormat df = new DecimalFormat("###.#");
                holder.distance.setText(df.format(Helper.distance(ld.getCoordinates(),userCurrentCoordinates))+Helper.getDistanceUnit());
            }
            @Override
            public void onFailed() {
                Toast.makeText(context, "Error while adding the location", Toast.LENGTH_SHORT).show();
            }
        });
        holder.category.setText(ld.getIncidentCategory());
        holder.time.setText(Helper.getTimeAgo(ld.getPostTime()));
        holder.incidentView.setOnClickListener(v -> {
            Incident incident = listData.get(position);
            Intent intent = new Intent(v.getContext(), IncidentDetailsActivity.class);
            intent.putExtra(Constants.INCIDENT_DATA_EXTRA, incident);
            context.startActivity(intent);
        });

        holder.options.setOnClickListener(this::showPopup);
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView description;
        private final TextView postedBy;
        private final TextView time;
        private final TextView category;
        private final TextView distance;
        private final CardView incidentView;
        private final ImageView options;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            postedBy = itemView.findViewById(R.id.posted_by);
            category = itemView.findViewById(R.id.incident_category);
            time = itemView.findViewById(R.id.time);
            incidentView = itemView.findViewById(R.id.incident_view);
            options=itemView.findViewById(R.id.imageButton);
            distance=itemView.findViewById(R.id.incident_distance);
        }
    }
}

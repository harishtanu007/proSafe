package com.harish.prosafe.data.adapters;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.text.TextUtils;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class IncidentAdapter extends RecyclerView.Adapter<IncidentAdapter.ViewHolder> {

    private List<Incident> listData;

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
        holder.postedBy.setText(ld.getPostedBy());
        backendProvider.getAddressValueEventListener(new AddressValueChangeListener() {
            @Override
            public void onSuccess(Coordinates userCurrentCoordinates) {
                holder.distance.setText(String.valueOf(distance(ld.getCoordinates(),userCurrentCoordinates)));
            }
            @Override
            public void onFailed() {

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

    private double distance(Coordinates coordinates1,Coordinates coordinates2) {
        double lat1=coordinates1.getLatitude();
        double lat2 =coordinates2.getLatitude();
        double lon1=coordinates1.getLongitude();
        double lon2=coordinates2.getLongitude();
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title, description, postedBy, time, category,distance;
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
            distance=itemView.findViewById(R.id.incident_distance);
        }
    }
}

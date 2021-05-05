package com.harish.prosafe.data.adapters;

import com.harish.prosafe.data.adapters.IncidentAdapter;

public interface IncidentValueChangeListener {
    void onSuccess(IncidentAdapter adapter);
    void onFailed();
}

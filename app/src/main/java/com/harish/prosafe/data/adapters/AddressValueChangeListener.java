package com.harish.prosafe.data.adapters;

import com.harish.prosafe.data.model.Coordinates;

public interface AddressValueChangeListener {
        void onSuccess(Coordinates coordinates);
        void onFailed();
}

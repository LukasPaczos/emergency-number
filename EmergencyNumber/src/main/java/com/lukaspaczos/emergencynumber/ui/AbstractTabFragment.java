package com.lukaspaczos.emergencynumber.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.lukaspaczos.emergencynumber.ActivityEventListener;
import com.lukaspaczos.emergencynumber.MainActivity;
import com.lukaspaczos.emergencynumber.ui.messages.database.DatabaseHelper;

public abstract class AbstractTabFragment extends AppFragment implements ActivityEventListener {
    protected OnFragmentInteractionListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        if (context instanceof MainActivity) {
            ((MainActivity) context).addEventListener(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.onDetach(this);
        mListener = null;
    }

    @Override
    public void onNewData(Bundle bundle) {

    }

    public interface OnFragmentInteractionListener {
        void onDetach(AbstractTabFragment fragment);

        DatabaseHelper getDatabase();

        TabLayout getTabLayout();

        void notifyMessageSent();

        boolean isWaitingForMessageDelivery();

        View getMainContentView();
    }
}

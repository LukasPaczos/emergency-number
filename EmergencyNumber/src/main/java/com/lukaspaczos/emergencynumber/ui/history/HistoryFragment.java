package com.lukaspaczos.emergencynumber.ui.history;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lukaspaczos.emergencynumber.R;
import com.lukaspaczos.emergencynumber.ui.AbstractTabFragment;
import com.lukaspaczos.emergencynumber.ui.history.list.HistoryAdapter;
import com.lukaspaczos.emergencynumber.ui.history.list.HistoryElementClickListener;
import com.lukaspaczos.emergencynumber.ui.history.list.HistoryEntryModel;
import com.lukaspaczos.emergencynumber.ui.messages.data.MessageModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryFragment extends AbstractTabFragment {
    public static final String TAG = "HISTORY_FRAGMENT";

    private RecyclerView mRecyclerView;
    private HistoryAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private TextView tvNoHistory;

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.history_recycler);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        final List<MessageModel> messageModelList = mListener.getDatabase().getAllMessages();
        final List<HistoryEntryModel> historyEntryModels = new ArrayList<>();
        for (MessageModel model : messageModelList) {
            if (model.isFirstMessage()) {
                historyEntryModels.add(new HistoryEntryModel(
                        model.getTimestamp(),
                        model.getReport().getCategories(),
                        model.getReport().getLocation(),
                        model.getReport()
                ));
            }
        }

        Collections.reverse(historyEntryModels);

        mAdapter = new HistoryAdapter(historyEntryModels, new HistoryElementClickListener() {
            @Override
            public void onRemoveClicked(final int position) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle(R.string.history_remove_title);
                dialog.setMessage(R.string.history_remove_message);
                dialog.setPositiveButton(R.string.history_remove_confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (position == 0)
                            mListener.getDatabase().removeEntry(historyEntryModels.get(position).getTimestamp(), Long.MAX_VALUE);
                        else
                            mListener.getDatabase().removeEntry(historyEntryModels.get(position).getTimestamp(), historyEntryModels.get(position - 1).getTimestamp());

                        historyEntryModels.remove(position);
                        mAdapter.notifyItemRemoved(position);
                        tvNoHistory.setVisibility(mAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                    }
                });
                dialog.setNegativeButton(R.string.history_remove_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                dialog.show();

            }

            @Override
            public void onDetailsClicked(int position) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                DetailsDialogFragment newFragment = DetailsDialogFragment.newInstance(mAdapter.getHistoryEntryModelList().get(position).getReport(), mAdapter.getHistoryEntryModelList().get(position).getTimestamp());
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.add(android.R.id.content, newFragment).addToBackStack(null).commit();
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        tvNoHistory = (TextView) view.findViewById(R.id.history_empty);
        tvNoHistory.setVisibility(mAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);

        return view;
    }

    @Override
    protected boolean onBackPressed() {
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getActivity().getSupportFragmentManager().popBackStackImmediate();
            return true;
        }

        return false;
    }
}

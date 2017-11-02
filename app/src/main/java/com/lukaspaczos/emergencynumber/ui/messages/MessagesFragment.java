package com.lukaspaczos.emergencynumber.ui.messages;

import android.graphics.PorterDuff;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.lukaspaczos.emergencynumber.MainActivity;
import com.lukaspaczos.emergencynumber.R;
import com.lukaspaczos.emergencynumber.location.MyLocationManager;
import com.lukaspaczos.emergencynumber.ui.AbstractTabFragment;
import com.lukaspaczos.emergencynumber.ui.messages.chat.ChatAdapter;
import com.lukaspaczos.emergencynumber.ui.messages.chat.ChatElementModel;
import com.lukaspaczos.emergencynumber.ui.messages.data.MessageModel;
import com.lukaspaczos.emergencynumber.ui.report.Report;
import com.lukaspaczos.emergencynumber.util.StringUtils;
import com.lukaspaczos.emergencynumber.util.UserInterfaceUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MessagesFragment extends AbstractTabFragment implements MyLocationManager.LocationManagerCallbacks {
    public static final String TAG = "MESSAGES_FRAGMENT";
    private static final String IS_HISTORY_ARG = "is_history_arg";
    private static final String TIMESTAMP_ARG = "timestamp_arg";

    private MyLocationManager locationManager;
    private boolean connectionLocationError = false;

    private boolean isHistory;
    private long timestamp;

    private View rootView;
    private RecyclerView messagesList;
    private final List<ChatElementModel> chatElementModels = new ArrayList<>();
    private LinearLayoutManager mLayoutManager;
    private ChatAdapter chatAdapter;
    private View respondLayout;
    private EditText respondInput;
    private ImageView respondSendButton;
    private ProgressBar respondProgress;
    private TextView tvNoMessages;

    public MessagesFragment() {
        // Required empty public constructor
    }

    public static MessagesFragment newInstance() {
        return newInstance(false, 0);
    }

    public static MessagesFragment newInstance(boolean isHistory, long timestamp) {
        MessagesFragment fragment = new MessagesFragment();
        Bundle args = new Bundle();
        args.putBoolean(IS_HISTORY_ARG, isHistory);
        args.putLong(TIMESTAMP_ARG, timestamp);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isHistory = getArguments().getBoolean(IS_HISTORY_ARG);
            timestamp = getArguments().getLong(TIMESTAMP_ARG);
        }

        locationManager = new MyLocationManager(getActivity(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_messages, container, false);
        respondLayout = rootView.findViewById(R.id.messages_respond_layout);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        messagesList = (RecyclerView) view.findViewById(R.id.messages_list);
        chatAdapter = new ChatAdapter(chatElementModels, getActivity());
        messagesList.setAdapter(chatAdapter);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setReverseLayout(true);
        messagesList.setLayoutManager(mLayoutManager);

        /*SlideInUpAnimator animator = new SlideInUpAnimator();
        messagesList.setItemAnimator(animator);*/

        //messagesList.scrollToPosition(chatAdapter.getItemCount() - 1);
        if (isHistory)
            messagesList.setNestedScrollingEnabled(false);

        respondInput = (EditText) view.findViewById(R.id.messages_respond_input);
        respondSendButton = (ImageView) view.findViewById(R.id.messages_send_button);
        respondSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = respondInput.getText().toString().trim();
                if (!input.isEmpty()) {
                    respondSendButton.setVisibility(View.GONE);
                    respondProgress.setVisibility(View.VISIBLE);
                    respondInput.setEnabled(false);
                    UserInterfaceUtils.hideSoftKeyboard(rootView);
                    Toast.makeText(getActivity(), R.string.report_send_try,
                            Toast.LENGTH_SHORT).show();
                    locationManager.requestLocation(getActivity(), false);
                }
            }
        });
        respondProgress = (ProgressBar) view.findViewById(R.id.messages_send_progress);

        respondSendButton.setVisibility(View.GONE);
        respondProgress.setVisibility(View.VISIBLE);
        respondInput.setEnabled(false);

        tvNoMessages = (TextView) view.findViewById(R.id.messages_empty);
    }

    @Override
    public void onStart() {
        super.onStart();
        locationManager.initialize();
        checkForNewMessages();
    }

    @Override
    public void onStop() {
        super.onStop();
        locationManager.uninitialize();
        connectionLocationError = false;
    }

    @Override
    public void onNewData(Bundle bundle) {
        handleNewData(bundle);
    }

    private void handleNewData(Bundle bundle) {
        if (bundle != null) {
            String action = bundle.getString(MainActivity.ACTION_ARG);
            if (action != null) {
                switch (action) {
                    case MainActivity.ACTION_NEW_SMS: {
                        checkForNewMessages();
                        break;
                    }

                    case MainActivity.ACTION_MESSAGE_SEND: {
                        if (!bundle.getBoolean(MainActivity.MESSAGE_SEND_RESULT)) {
                            Toast.makeText(getActivity(), R.string.error_unknown, Toast.LENGTH_LONG).show();
                            respondSendButton.setVisibility(View.VISIBLE);
                            respondProgress.setVisibility(View.GONE);
                            respondInput.setEnabled(true);
                        }

                        break;
                    }

                    case MainActivity.ACTION_MESSAGE_DELIVERY: {
                        if (bundle.getBoolean(MainActivity.MESSAGE_DELIVERY_RESULT)) {
                            respondInput.setText("");
                            checkForNewMessages();
                        }

                        break;
                    }
                }
            }
        }
    }

    private void checkForNewMessages() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!connectionLocationError) {
                    if (mListener.isWaitingForMessageDelivery()) {
                        respondSendButton.setVisibility(View.GONE);
                        respondProgress.setVisibility(View.VISIBLE);
                        respondInput.setEnabled(false);
                    } else {
                        respondSendButton.setVisibility(View.VISIBLE);
                        respondProgress.setVisibility(View.GONE);
                        respondInput.setEnabled(true);
                    }
                }

                List<ChatElementModel> newModels = getMessages();
                for (int i = newModels.size() - 1; i >= 0; i--) {
                    if (!chatElementModels.contains(newModels.get(i))) {
                        chatElementModels.add(0, newModels.get(i));
                        mLayoutManager.scrollToPositionWithOffset(0, 0);
                        chatAdapter.notifyItemInserted(0);
                    }
                }

                tvNoMessages.setVisibility(chatAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
                respondLayout.setVisibility(chatAdapter.getItemCount() == 0 || isHistory ? View.GONE : View.VISIBLE);
            }
        });
    }

    private List<ChatElementModel> getMessages() {
        List<ChatElementModel> databaseModels = new ArrayList<>();

        List<MessageModel> modelList = mListener.getDatabase().getAllMessages();
        int i;

        if (isHistory) {
            for (i = modelList.size() - 1; i >= 0; i--) {
                if (modelList.get(i).isFirstMessage() && modelList.get(i).getTimestamp() == timestamp) {
                    databaseModels.add(ChatElementModel.fromMessageModel(modelList.get(i)));
                    break;
                }
            }
            for (int j = i + 1; j < modelList.size(); j++) {
                if (modelList.get(j).isFirstMessage())
                    break;

                databaseModels.add(ChatElementModel.fromMessageModel(modelList.get(j)));
            }

        } else {
            for (i = modelList.size() - 1; i >= 0; i--) {
                if (modelList.get(i).isFirstMessage()) {
                    if (i == modelList.size() - 1) {
                        int size = chatElementModels.size();
                        chatElementModels.clear();
                        chatAdapter.notifyItemRangeRemoved(0, size);
                    } else if (modelList.get(i).getTimestamp() < System.currentTimeMillis() - TimeUnit.HOURS.toMillis(24))
                        i = -1;
                    break;
                }
            }

            if (i > -1) { //list has last item
                for (int j = i; j < modelList.size(); j++) {
                    databaseModels.add(ChatElementModel.fromMessageModel(modelList.get(j)));
                }
            }
        }

        Collections.reverse(databaseModels);

        return databaseModels;
    }

    @Override
    public void onNewLocation(final Location location) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String input = StringUtils.deAccent(respondInput.getText().toString().trim());

                if (location != null)
                    input = String.format(input + "\ngeo:%.6f, %.6f", location.getLatitude(), location.getLongitude());

                if (Report.sendMessage(getActivity(), input, null)) {
                    mListener.notifyMessageSent();
                } else {
                    Toast.makeText(getActivity(), R.string.error_sending_report,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onGpsUnavailable() {
//        Toast.makeText(getActivity(), R.string.gps_not_found_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getActivity(), R.string.error_unknown, Toast.LENGTH_LONG).show();
        respondProgress.setVisibility(View.GONE);
        respondInput.setEnabled(false);
        respondSendButton.setColorFilter(ContextCompat.getColor(getActivity(), R.color.secondary_text), PorterDuff.Mode.DST_ATOP);
        connectionLocationError = true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (!mListener.isWaitingForMessageDelivery()) {
            respondSendButton.setVisibility(View.VISIBLE);
            respondProgress.setVisibility(View.GONE);
            respondInput.setEnabled(true);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getActivity(), R.string.error_unknown, Toast.LENGTH_LONG).show();
        respondProgress.setVisibility(View.GONE);
        respondInput.setEnabled(false);
        respondSendButton.setColorFilter(ContextCompat.getColor(getActivity(), R.color.secondary_text), PorterDuff.Mode.DST_ATOP);
        connectionLocationError = true;
    }
}

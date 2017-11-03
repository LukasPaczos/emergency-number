package com.lukaspaczos.emergencynumber;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.lukaspaczos.emergencynumber.ui.AbstractTabFragment;
import com.lukaspaczos.emergencynumber.ui.firstaid.FirstAidFragment;
import com.lukaspaczos.emergencynumber.ui.history.HistoryFragment;
import com.lukaspaczos.emergencynumber.ui.messages.MessagesFragment;
import com.lukaspaczos.emergencynumber.ui.messages.database.DatabaseHelper;
import com.lukaspaczos.emergencynumber.ui.report.ReportFragment;
import com.lukaspaczos.emergencynumber.ui.report.fragment.AbstractCategoryFragment;
import com.transitionseverywhere.Slide;
import com.transitionseverywhere.TransitionManager;

import java.util.ArrayList;
import java.util.List;

import me.grantland.widget.AutofitTextView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements AbstractTabFragment.OnFragmentInteractionListener {

    public static final String ACTION_ARG = "action";
    public static final String ACTION_NEW_SMS = "message_new";
    public static final String ACTION_MESSAGE_SEND = "message_send";
    public static final String ACTION_MESSAGE_DELIVERY = "message_delivery";
    public static final String MESSAGE_SEND_RESULT = "message_send_result";
    public static final String MESSAGE_DELIVERY_RESULT = "message_delivery_result";
    public static final String MESSAGE_CONTENT_ARG = "message_content";
    public static final String MESSAGE_REPORT_ARG = "message_report";
    public static final int NEW_MESSAGE_SENT_REQUSET_CODE = 1;
    public static final int NEW_MESSAGE_DELIVERED_REQUSET_CODE = 2;
    private static final int MIN_KEYBOARD_HEIGHT_PX = 150;

    public static final String WAITING_FOR_DELIVERY_ARG = "waiting_for_delivery";
    private boolean waitingForMessageDelivery = false;

    private static final int[] tabTitles = new int[]{R.string.tab_report, R.string.tab_messages, R.string.tab_history, R.string.tab_first_aid};

    private final DatabaseHelper database = DatabaseHelper.getInstance();

    private final List<ActivityEventListener> activityEventListeners = new ArrayList<>();
    private TabLayout tabLayout;
    private final MyOnTabSelectedListener myOnTabSelectedListener = new MyOnTabSelectedListener();
    private View mainContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ((App) App.getContext()).setLoggedIn(true);

        if (savedInstanceState != null) {
            waitingForMessageDelivery = savedInstanceState.getBoolean(WAITING_FOR_DELIVERY_ARG);
        }

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        LayoutInflater inflater = getLayoutInflater();
        for (int i = 0; i < 3; i++) {
            View view = inflater.inflate(R.layout.view_custom_tab, tabLayout, false);
            AutofitTextView title = (AutofitTextView) view.findViewById(R.id.tab_title);
            title.setText(getString(tabTitles[i]));
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setCustomView(view);
            tabLayout.addTab(tab);
        }
        View view = inflater.inflate(R.layout.view_custom_tab, tabLayout, false);
        AutofitTextView title = (AutofitTextView) view.findViewById(R.id.tab_title);
        title.setMaxLines(2);
        title.setText(getString(tabTitles[3]));
        TabLayout.Tab tab = tabLayout.newTab();
        tab.setCustomView(view);
        tabLayout.addTab(tab);

        tabLayout.addOnTabSelectedListener(myOnTabSelectedListener);
        myOnTabSelectedListener.myOnTabSelected(tabLayout.getTabAt(0), null);

        mainContentView = findViewById(R.id.main_content);

        database.initialize();

        handleIntent(getIntent());
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (intent != null) {
            if (intent.getAction() == null) {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    intent.setAction(extras.getString(ACTION_ARG));
                } else
                    return;
            }

            Bundle bundle = intent.getExtras();
            if (bundle == null)
                bundle = new Bundle();

            bundle.putString(ACTION_ARG, intent.getAction());
            switch (intent.getAction()) {
                case ACTION_MESSAGE_SEND:

                    if (tabLayout.getSelectedTabPosition() == 1) {
                        for (ActivityEventListener listener : activityEventListeners) {
                            listener.onNewData(bundle);
                        }
                    } else
                        tabLayout.getTabAt(1).select();
                    //myOnTabSelectedListener.myOnTabSelected(tabLayout.getTabAt(1), bundle);

                    break;

                case ACTION_MESSAGE_DELIVERY:

                    waitingForMessageDelivery = false;

                    if (tabLayout.getSelectedTabPosition() == 1) {
                        for (ActivityEventListener listener : activityEventListeners) {
                            listener.onNewData(bundle);
                        }
                    } else
                        tabLayout.getTabAt(1).select();
                    //myOnTabSelectedListener.myOnTabSelected(tabLayout.getTabAt(1), bundle);
                    break;

                case ACTION_NEW_SMS:
                    if (tabLayout.getSelectedTabPosition() == 1) {
                        for (ActivityEventListener listener : activityEventListeners) {
                            listener.onNewData(bundle);
                        }
                    } else
                        tabLayout.getTabAt(1).select();
                    //myOnTabSelectedListener.myOnTabSelected(tabLayout.getTabAt(1), bundle);

                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.uninitialize();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mainContentView != null) {
            mainContentView.getViewTreeObserver().addOnGlobalLayoutListener(keyboardListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mainContentView != null) {
            mainContentView.getViewTreeObserver().removeOnGlobalLayoutListener(keyboardListener);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(WAITING_FOR_DELIVERY_ARG, waitingForMessageDelivery);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!AbstractCategoryFragment.handleBackPressed(getSupportFragmentManager())) {

            if (tabLayout.getSelectedTabPosition() != 0) {

                tabLayout.getTabAt(0).select();

            } else {
                super.onBackPressed();
            }

        }
    }

    class MyOnTabSelectedListener implements TabLayout.OnTabSelectedListener {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            myOnTabSelected(tab, null);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            myOnTabUnselected(tab, null);
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            myOnTabReselected(tab, null);
        }

        public void myOnTabSelected(@NonNull TabLayout.Tab tab, @Nullable Bundle bundle) {
            AutofitTextView title = (AutofitTextView) tab.getCustomView().findViewById(R.id.tab_title);
            title.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.primary_text));
            AbstractTabFragment fragment;
            switch (tab.getPosition()) {
                case 0:
                    fragment = ReportFragment.newInstance();
                    fragment.setArguments(bundle);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_content, fragment, ReportFragment.TAG)
                            .commit();
                    break;
                case 1:
                    fragment = MessagesFragment.newInstance();
                    fragment.setArguments(bundle);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_content, fragment, MessagesFragment.TAG)
                            .commit();
                    break;
                case 2:
                    fragment = HistoryFragment.newInstance();
                    fragment.setArguments(bundle);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_content, fragment, HistoryFragment.TAG)
                            .commit();
                    break;
                case 3:
                    fragment = FirstAidFragment.newInstance();
                    fragment.setArguments(bundle);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_content, fragment, FirstAidFragment.TAG)
                            .commit();
                    break;
            }
        }

        public void myOnTabUnselected(@NonNull TabLayout.Tab tab, @Nullable Bundle bundle) {
            AutofitTextView title = (AutofitTextView) tab.getCustomView().findViewById(R.id.tab_title);
            title.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.secondary_text));
        }

        public void myOnTabReselected(@NonNull TabLayout.Tab tab, @Nullable Bundle bundle) {

        }
    }


    public void addEventListener(ActivityEventListener listener) {
        activityEventListeners.add(listener);
    }

    public void removeEventListener(ActivityEventListener listener) {
        activityEventListeners.remove(listener);
    }

    @Override
    public void onDetach(AbstractTabFragment fragment) {
        removeEventListener(fragment);
    }

    @Override
    public DatabaseHelper getDatabase() {
        return database;
    }

    @Override
    public TabLayout getTabLayout() {
        return tabLayout;
    }

    @Override
    public void notifyMessageSent() {
        waitingForMessageDelivery = true;
        getTabLayout().getTabAt(1).select();
    }

    @Override
    public boolean isWaitingForMessageDelivery() {
        return waitingForMessageDelivery;
    }

    @Override
    public View getMainContentView() {
        return mainContentView;
    }

    private ViewTreeObserver.OnGlobalLayoutListener keyboardListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        private final Rect windowVisibleDisplayFrame = new Rect();
        private int lastVisibleDecorViewHeight;

        @Override
        public void onGlobalLayout() {
            mainContentView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame);
            final int visibleDecorViewHeight = windowVisibleDisplayFrame.height();

            if (lastVisibleDecorViewHeight != 0) {
                if (lastVisibleDecorViewHeight + MIN_KEYBOARD_HEIGHT_PX < visibleDecorViewHeight) {
                    onKeyboardHidden();
                } else if (lastVisibleDecorViewHeight != visibleDecorViewHeight) {
                    onKeyboardShown();
                }
            }
            lastVisibleDecorViewHeight = visibleDecorViewHeight;
        }
    };

    public void onKeyboardShown() {
        try {
            TransitionManager.beginDelayedTransition(
                    (ViewGroup) mainContentView,
                    new Slide(Gravity.BOTTOM)
            );
            getTabLayout().setVisibility(View.GONE);/*
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) getMainContentView().getLayoutParams();
            lp.setMargins(0, 0, 0, 0);
            getMainContentView().setLayoutParams(lp);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onKeyboardHidden() {
        try {
            TransitionManager.beginDelayedTransition(
                    (ViewGroup) mainContentView,
                    new Slide(Gravity.BOTTOM)
            );
            getTabLayout().setVisibility(View.VISIBLE);/*
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) getMainContentView().getLayoutParams();
            lp.setMargins(0, 0, 0, (int) App.getContext().getResources().getDimension(R.dimen.navigation_tab_layout_height));
            getMainContentView().setLayoutParams(lp);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

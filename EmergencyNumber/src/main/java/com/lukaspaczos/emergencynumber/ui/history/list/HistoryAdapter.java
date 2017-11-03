package com.lukaspaczos.emergencynumber.ui.history.list;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.lukaspaczos.emergencynumber.App;
import com.lukaspaczos.emergencynumber.R;
import com.lukaspaczos.emergencynumber.ui.report.category.ReportCategoriesFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Lukas Paczos on 06-Apr-17
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private HistoryElementClickListener outsideListener;
    private List<HistoryEntryModel> historyEntryModelList;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
    private final SimpleDateFormat dayTimeFormat = new SimpleDateFormat("EEEE, HH:mm");

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View entryView;
        private TextView tvDate;
        private TextView tvDayTime;
        private ImageView iconImageView;
        private TextView tvCategoryName;
        private ImageButton removeImageButton;
        private Button detailsButton;

        private HistoryElementClickListener listener;

        public ViewHolder(View v, HistoryElementClickListener listener) {
            super(v);
            entryView = v;
            tvDate = (TextView) entryView.findViewById(R.id.history_date);
            tvDayTime = (TextView) entryView.findViewById(R.id.history_day_time);
            iconImageView = (ImageView) entryView.findViewById(R.id.history_icon);
            tvCategoryName = (TextView) entryView.findViewById(R.id.history_category_name);
            removeImageButton = (ImageButton) entryView.findViewById(R.id.history_delete_button);
            detailsButton = (Button) entryView.findViewById(R.id.history_more_button);

            removeImageButton.setOnClickListener(this);
            detailsButton.setOnClickListener(this);

            this.listener = listener;
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == removeImageButton.getId()) {
                listener.onRemoveClicked(this.getAdapterPosition());
            } else if (view.getId() == detailsButton.getId()) {
                listener.onDetailsClicked(this.getAdapterPosition());
            }
        }
    }

    public HistoryAdapter(List<HistoryEntryModel> historyEntryModelList, HistoryElementClickListener listener) {
        this.historyEntryModelList = historyEntryModelList;
        outsideListener = listener;
    }

    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_entry_view, parent, false);

        return new ViewHolder(v, outsideListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HistoryEntryModel model = historyEntryModelList.get(position);

        Date date = new Date(model.getTimestamp());
        String dateString = dateFormat.format(date);
        String dayTimeString = dayTimeFormat.format(date);

        holder.tvDate.setText(dateString);
        holder.tvDayTime.setText(dayTimeString);
        holder.iconImageView.setImageDrawable(ContextCompat.getDrawable(App.getContext(), ReportCategoriesFactory.getIconDrawableRes(model.getCategories().get(0))));
        holder.tvCategoryName.setText(model.getCategories().get(0));
    }

    @Override
    public int getItemCount() {
        return historyEntryModelList.size();
    }

    public List<HistoryEntryModel> getHistoryEntryModelList() {
        return historyEntryModelList;
    }
}

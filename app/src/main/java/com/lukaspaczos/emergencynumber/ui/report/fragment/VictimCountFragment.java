package com.lukaspaczos.emergencynumber.ui.report.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.lukaspaczos.emergencynumber.R;
import com.lukaspaczos.emergencynumber.ui.report.Report;
import com.shawnlin.numberpicker.NumberPicker;

public class VictimCountFragment extends AbstractCategoryFragment {

    public VictimCountFragment() {
        // Required empty public constructor
    }

    public static VictimCountFragment newInstance(Report report) {
        VictimCountFragment fragment = new VictimCountFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_REPORT, report);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_victim_count, container, false);
        final NumberPicker numberPicker = (NumberPicker) view.findViewById(R.id.victim_count_picker);
        numberPicker.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                if (value == numberPicker.getMaxValue()) {
                    return numberPicker.getMaxValue() + "+";
                }

                return String.valueOf(value);
            }
        });


        Button confirmButton = (Button) view.findViewById(R.id.victim_count_confirm_btn);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox1 = (CheckBox) view.findViewById(R.id.victim_count_heavily_injured_chk);
                report.addVictimType(checkBox1.getText().toString(), checkBox1.isChecked());

                CheckBox checkBox2 = (CheckBox) view.findViewById(R.id.victim_count_burned_chk);
                report.addVictimType(checkBox2.getText().toString(), checkBox2.isChecked());

                CheckBox checkBox3 = (CheckBox) view.findViewById(R.id.victim_count_trapped_chk);
                report.addVictimType(checkBox3.getText().toString(), checkBox3.isChecked());

                report.setVictimCount(String.valueOf(numberPicker.getValue()) +
                        (numberPicker.getValue() == numberPicker.getMaxValue() ? "+" : "")
                );

                mListener.loadLocationCategory();
            }
        });
        return view;
    }
}

package com.example.vibecoding10list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;

public class fragment_start extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_start, container, false);

        Button startButton = view.findViewById(R.id.btn_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 설문조사 시작
                fragment_qustion questionFragment = new fragment_qustion();
                ((MainActivity) getActivity()).replaceFragment(questionFragment);
            }
        });

        return view;
    }
}
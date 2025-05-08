package com.example.vibecoding10list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.widget.LinearLayout;

public class fragment_result extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_result, container, false);

        TextView resultText = view.findViewById(R.id.tv_result);
        TextView scoreText = view.findViewById(R.id.tv_score);
        LinearLayout barGraph = view.findViewById(R.id.bar_graph);
        LinearLayout eBar = view.findViewById(R.id.e_bar);
        LinearLayout iBar = view.findViewById(R.id.i_bar);
        Button restartButton = view.findViewById(R.id.btn_restart);

        // 결과 계산
        int[] answers = getArguments().getIntArray("answers");
        int eScore = 0;
        int iScore = 0;

        // 문항 1-5는 E 점수에 기여, 6-10은 I 점수에 기여
        for (int i = 0; i < answers.length; i++) {
            if (i < 5) {
                eScore += answers[i];
            } else {
                iScore += answers[i];
            }
        }

        // 총점 계산 및 백분율
        int totalScore = eScore + iScore;
        int ePercentage = (eScore * 100) / totalScore;
        int iPercentage = (iScore * 100) / totalScore;

        // 결과 표시
        String personalityType = eScore > iScore ? "외향형(E)" : "내향형(I)";
        resultText.setText("당신은 " + personalityType + " 입니다.");
        scoreText.setText("E: " + eScore + "점 (" + ePercentage + "%) / I: " + iScore + "점 (" + iPercentage + "%)");

        // 그래프 업데이트 - onViewCreated에서 처리

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 처음 화면으로 돌아가기
                getActivity().getSupportFragmentManager().popBackStack(null,
                        androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
                ((MainActivity) getActivity()).replaceFragment(new fragment_start());
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 그래프 레이아웃이 그려진 후 막대 그래프 업데이트
        final LinearLayout barGraph = view.findViewById(R.id.bar_graph);
        final LinearLayout eBar = view.findViewById(R.id.e_bar);
        final LinearLayout iBar = view.findViewById(R.id.i_bar);

        barGraph.post(new Runnable() {
            @Override
            public void run() {
                int[] answers = getArguments().getIntArray("answers");
                int eScore = 0;
                int iScore = 0;

                for (int i = 0; i < answers.length; i++) {
                    if (i < 5) {
                        eScore += answers[i];
                    } else {
                        iScore += answers[i];
                    }
                }

                int totalScore = eScore + iScore;
                int ePercentage = (eScore * 100) / totalScore;
                int iPercentage = (iScore * 100) / totalScore;

                ViewGroup.LayoutParams eParams = eBar.getLayoutParams();
                ViewGroup.LayoutParams iParams = iBar.getLayoutParams();

                eParams.width = (barGraph.getWidth() * ePercentage) / 100;
                iParams.width = (barGraph.getWidth() * iPercentage) / 100;

                eBar.setLayoutParams(eParams);
                iBar.setLayoutParams(iParams);
            }
        });
    }
}
package com.example.vibecoding10list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class fragment_qustion extends Fragment {
    private static final String[] questions = {
            "1. 나는 여러 사람들과 함께 있을 때 에너지가 충전된다.",
            "2. 나는 대화를 먼저 시작하는 편이다.",
            "3. 나는 모임에서 다양한 사람들과 대화하는 것을 즐긴다.",
            "4. 나는 사교적인 활동과 모임에 자주 참여한다.",
            "5. 나는 새로운 사람들을 만나는 것을 좋아한다.",
            "6. 나는 혼자 있는 시간이 필요하다.",
            "7. 나는 깊이 생각한 후에 말하는 편이다.",
            "8. 나는 소규모 모임이나 1:1 대화를 선호한다.",
            "9. 나는 조용한 환경에서 집중을 잘 한다.",
            "10. 나는 먼저 생각한 후 행동하는 편이다."
    };

    private int currentQuestionIndex = 0;
    private int[] answers = new int[questions.length];

    private TextView questionText;
    private RadioGroup radioGroup;
    private Button nextButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_qustion, container, false);

        questionText = view.findViewById(R.id.tv_question);
        radioGroup = view.findViewById(R.id.radio_group);
        nextButton = view.findViewById(R.id.btn_next);

        // 첫 번째 질문 설정
        updateQuestion();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 라디오 버튼 선택 확인
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(getContext(), "답변을 선택해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 선택한 답변 저장
                int radioIndex = radioGroup.indexOfChild(view.findViewById(selectedId));
                int value = 5 - radioIndex; // 5: 매우 그렇다, 1: 매우 아니다
                answers[currentQuestionIndex] = value;

                // 다음 질문으로 이동 또는 결과 화면 표시
                if (currentQuestionIndex < questions.length - 1) {
                    currentQuestionIndex++;
                    updateQuestion();
                    radioGroup.clearCheck();
                } else {
                    // 모든 질문 완료, 결과 화면으로 이동
                    fragment_result resultFragment = new fragment_result();
                    Bundle bundle = new Bundle();
                    bundle.putIntArray("answers", answers);
                    resultFragment.setArguments(bundle);
                    ((MainActivity) getActivity()).replaceFragment(resultFragment);
                }
            }
        });

        return view;
    }

    private void updateQuestion() {
        questionText.setText(questions[currentQuestionIndex]);
        // 진행 상황 표시 (예: 2/10)
        getActivity().setTitle("질문 " + (currentQuestionIndex + 1) + "/" + questions.length);
    }
}
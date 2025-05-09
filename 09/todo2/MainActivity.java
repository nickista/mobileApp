package com.example.todo2;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editText;
    private ImageButton imageButton; // ✅ Button → ImageButton
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private ArrayList<TaskItem> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        imageButton = findViewById(R.id.imageButton); // ✅ ID 맞춤
        recyclerView = findViewById(R.id.recyclerView);

        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(taskList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(taskAdapter);

        imageButton.setOnClickListener(v -> {
            String taskText = editText.getText().toString().trim();
            if (!taskText.isEmpty()) {
                TaskItem newTask = new TaskItem(taskText);
                taskList.add(newTask);
                taskAdapter.notifyItemInserted(taskList.size() - 1);
                editText.setText(""); // 입력 필드 초기화
            } else {
                Toast.makeText(MainActivity.this, "Please enter a task", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class TaskItem {
        String taskText;
        boolean isChecked;

        public TaskItem(String taskText) {
            this.taskText = taskText;
            this.isChecked = false;
        }
    }

    public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

        private ArrayList<TaskItem> taskList;

        public TaskAdapter(ArrayList<TaskItem> taskList) {
            this.taskList = taskList;
        }

        @Override
        public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.task_item, parent, false);
            return new TaskViewHolder(view);
        }

        @Override
        public void onBindViewHolder(TaskViewHolder holder, int position) {
            TaskItem task = taskList.get(position);
            holder.taskText.setText(task.taskText);
            holder.radioButton.setChecked(task.isChecked);

            holder.radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                task.isChecked = isChecked;
                if (isChecked) {
                    holder.itemView.animate()
                            .alpha(0f)
                            .setDuration(500)
                            .withEndAction(() -> {
                                taskList.remove(position);
                                notifyItemRemoved(position);
                            });
                }
            });
        }

        @Override
        public int getItemCount() {
            return taskList.size();
        }

        public class TaskViewHolder extends RecyclerView.ViewHolder {
            RadioButton radioButton;
            TextView taskText;

            public TaskViewHolder(View itemView) {
                super(itemView);
                radioButton = itemView.findViewById(R.id.radioButton);
                taskText = itemView.findViewById(R.id.taskText);
            }
        }
    }
}

package br.com.kmg.youdocleaning.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.kmg.youdocleaning.R;
import br.com.kmg.youdocleaning.model.CleaningTask;

public class TaskListAdapter extends BaseAdapter {

    final List<CleaningTask> tasks;

    public TaskListAdapter(List<CleaningTask> tasks) {
        this.tasks = tasks;
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public CleaningTask getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.task_item_list, parent, false);
        CleaningTask task = tasks.get(position);
        TextView name = view.findViewById(R.id.tv_item_task_name);
        TextView duration = view.findViewById(R.id.tv_item_task_duration);
        name.setText(task.getName());
        duration.setText(task.getDuration() + " minutes");
        return view;
    }
}

package com.example.lazlo;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class tasksAdapter extends RecyclerView.Adapter<tasksAdapter.tasksViewHolder> {
    private final Context mContext;
    private final ArrayList<taskModel> mTaskModelArrayList;
    public tasksAdapter(Context context, ArrayList<taskModel> taskModelArrayList){
        mContext = context;
        mTaskModelArrayList = taskModelArrayList;
    }

    public static class tasksViewHolder extends RecyclerView.ViewHolder {
        public TextView taskTitle;
        public TextView taskDescription;
        public TextView TaskAssociatedPrice;

        //taskTitle
        //taskDescription
        //TaskAssociatedDates
        public tasksViewHolder(@NonNull View itemView) {
            super(itemView);

            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskDescription = itemView.findViewById(R.id.taskDescription);
            TaskAssociatedPrice = itemView.findViewById(R.id.TaskAssociatedPrice);
        }
    }
    @NonNull
    @Override
    public tasksAdapter.tasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.tasks_recycler_view, parent,false);
        return new tasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull tasksAdapter.tasksViewHolder holder, int position) {
        final taskModel model = mTaskModelArrayList.get(position);

        String taskTitle = model.getTaskTitle();
        String taskDescription = model.getTaskDescription();
        String taskAssociatedPrice = model.getTaskAssociatedPrice();

        holder.taskTitle.setText(taskTitle);
        holder.taskDescription.setText(taskDescription);
        holder.TaskAssociatedPrice.setText(taskAssociatedPrice);


    }

    @Override
    public int getItemCount() {
        return mTaskModelArrayList.size();
    }


}

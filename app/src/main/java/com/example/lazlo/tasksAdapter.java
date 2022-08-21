package com.example.lazlo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.Locale;

public class tasksAdapter extends RecyclerView.Adapter<tasksAdapter.tasksViewHolder> {
    private final Context mContext;
    private ArrayList<taskModel> mTaskModelArrayList;
    public tasksAdapter(Context context, ArrayList<taskModel> taskModelArrayList){
        mContext = context;
        mTaskModelArrayList = taskModelArrayList;
    }
    public void filterList(ArrayList<taskModel> filteredList){
        mTaskModelArrayList = filteredList;
        notifyDataSetChanged();
    }


    public static class tasksViewHolder extends RecyclerView.ViewHolder {
        public TextView taskTitle;
        public TextView taskDescription;
        public TextView TaskAssociatedPrice;
        public TextView TaskDeadline;
        public AppCompatImageButton optionsButton;

        //taskTitle
        //taskDescription
        //TaskAssociatedDates
        public tasksViewHolder(@NonNull View itemView) {
            super(itemView);

            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskDescription = itemView.findViewById(R.id.taskDescription);
            TaskAssociatedPrice = itemView.findViewById(R.id.TaskAssociatedPrice);
            TaskDeadline = itemView.findViewById(R.id.TaskDeadline);
            optionsButton = itemView.findViewById(R.id.optionsButton);
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
        String taskDeadline = model.getTaskDeadline();

        holder.taskTitle.setText(taskTitle);
        holder.taskDescription.setText(taskDescription);
        holder.TaskAssociatedPrice.setText(String.format(new Locale("en","KE"),"%s%s","Ksh ",taskAssociatedPrice));
        holder.TaskDeadline.setText(HouseOfCommons.returnFormattedDeadline(taskDeadline));
        holder.optionsButton.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(mContext, holder.optionsButton);
            popupMenu.inflate(R.menu.flow_menu);
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()){
                    case R.id.viewEditTask:

                        Bundle bundle = new Bundle();
                        bundle.putDouble("randTaskId", model.getRandTaskId());
                        bundle.putDouble("randUserId", model.getRandUserId());
                        bundle.putString("taskTitle", model.getTaskTitle());
                        bundle.putString("taskDescription", model.getTaskDescription());
                        bundle.putString("taskAssociatedPrice", model.getTaskAssociatedPrice());
                        bundle.putString("taskCategory", model.getTaskCategory());
                        bundle.putString("taskDeadline", model.getTaskDeadline());
                        bundle.putString("taskCreationTime", model.getTaskCreationTime());
                        bundle.putString("taskPredictedDuration", model.getTaskPredictedDuration());
                        bundle.putString("taskState", model.getTaskState());
                        bundle.putString("parentTaskId", model.getParentTaskId());
                        Intent intent = new Intent(mContext,individualTask.class);
                        intent.putExtra("individualTaskDetails", bundle);
                        mContext.startActivity(intent);
                        break;
                    case R.id.performTask:
                        Intent startTask = new Intent(mContext, performTask.class );
                        startTask.putExtra("randTaskId", model.getRandTaskId());
                        startTask.putExtra("taskTitle", model.getTaskTitle());
                        startTask.putExtra("taskDescription", model.getTaskDescription());
                        startTask.putExtra("taskCategory", model.getTaskCategory());
                        startTask.putExtra("taskBills", model.getTaskAssociatedPrice());
                        startTask.putExtra("taskDeadline", model.getTaskDeadline());
                        mContext.startActivity(startTask);
                        break;
                    case R.id.deleteTask:

                        break;
                }
                return false;
            });
            popupMenu.show();
        });


    }

    @Override
    public int getItemCount() {
        return mTaskModelArrayList.size();
    }


}

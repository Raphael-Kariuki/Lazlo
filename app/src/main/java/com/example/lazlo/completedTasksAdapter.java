package com.example.lazlo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class completedTasksAdapter extends RecyclerView.Adapter<completedTasksAdapter.completedTasksViewHolder> {

    private final Context nContext;
    private ArrayList<completedTaskModel> nTaskModelArrayList;

    public completedTasksAdapter(Context nContext, ArrayList<completedTaskModel> completedTaskModelArrayList) {
        this.nContext = nContext;
        this.nTaskModelArrayList = completedTaskModelArrayList;
    }
    public void complete_filterList(ArrayList<completedTaskModel> complete_filteredList){
        nTaskModelArrayList = complete_filteredList;
        notifyDataSetChanged();
    }

    public static class completedTasksViewHolder extends RecyclerView.ViewHolder{

        public TextView completedTaskTitle;
        public TextView completedTaskDescription;
        public TextView completedTaskAssociatedPrice;
        public TextView completedTaskDeadline;
        public AppCompatImageButton completedOptionsButton;

        public completedTasksViewHolder(@NonNull View itemView) {
            super(itemView);

            completedTaskTitle = itemView.findViewById(R.id.completedTaskTitle);
            completedTaskDescription = itemView.findViewById(R.id.completedTaskDescription);
            completedTaskAssociatedPrice = itemView.findViewById(R.id.completedTaskAssociatedPrice);
            completedTaskDeadline = itemView.findViewById(R.id.completedTaskDeadline);
            completedOptionsButton = itemView.findViewById(R.id.completedOptionsButton);
        }
    }
    @NonNull
    @Override
    public completedTasksAdapter.completedTasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(nContext);
        View view = layoutInflater.inflate(R.layout.completed_tasks_recycler_view, parent,false);
        return new completedTasksAdapter.completedTasksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull completedTasksAdapter.completedTasksViewHolder holder, int position) {

        final completedTaskModel completedTaskModel = nTaskModelArrayList.get(position);

        String completedTaskTitle = completedTaskModel.getTaskTitle();
        String completedTaskDescription = completedTaskModel.getTaskDescription();
        String completedTaskAssociatedPrice = completedTaskModel.getTaskAssociatedPrice();
        String completedTaskDeadline = completedTaskModel.getTaskDeadline();

        holder.completedTaskTitle.setText(completedTaskTitle);
        holder.completedTaskDescription.setText(completedTaskDescription);
        holder.completedTaskAssociatedPrice.setText(String.format(new Locale("en","KE"),"%s%s","Ksh ",completedTaskAssociatedPrice));
        holder.completedTaskDeadline.setText(HouseOfCommons.returnFormattedDeadline(completedTaskDeadline));
        holder.completedOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(nContext, holder.completedOptionsButton);
                popupMenu.inflate(R.menu.flow_menu2);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.viewCompletedTask:
                                Bundle bundle = new Bundle();
                                bundle.putDouble("randTaskId", completedTaskModel.getRandTaskId());
                                bundle.putDouble("randUserId", completedTaskModel.getRandUserId());
                                bundle.putString("taskTitle", completedTaskModel.getTaskTitle());
                                bundle.putString("taskDescription", completedTaskModel.getTaskDescription());
                                bundle.putString("taskAssociatedPrice", completedTaskModel.getTaskAssociatedPrice());
                                bundle.putString("taskCategory", completedTaskModel.getTaskCategory());
                                bundle.putLong("taskCreationTime", completedTaskModel.getTaskCreationTime());
                                bundle.putLong("taskStartTime", completedTaskModel.getTaskStartTime());
                                bundle.putLong("taskCompleteTime", completedTaskModel.getTaskCompleteTime());
                                bundle.putLong("taskDuration", completedTaskModel.getTaskDuration());
                                bundle.putString("taskDeadline", completedTaskModel.getTaskDeadline());
                                bundle.putString("taskPredictedDuration", completedTaskModel.getTaskPredictedDuration());
                                bundle.putLong("taskTrial", completedTaskModel.getTaskTrial());
                                bundle.putLong("taskState", completedTaskModel.getTaskState());
                                Intent intent = new Intent(nContext, IndividualCompletedTask.class);
                                intent.putExtra("individualCompletedTaskDetails", bundle);
                                nContext.startActivity(intent);
                                break;
                            case R.id.rescheduleCompletedTask:
                                Bundle bundle2 = new Bundle();
                                bundle2.putString("taskTitle", completedTaskModel.getTaskTitle());
                                bundle2.putString("taskDescription", completedTaskModel.getTaskDescription());
                                bundle2.putString("taskAssociatedPrice", completedTaskModel.getTaskAssociatedPrice());
                                bundle2.putString("taskCategory", completedTaskModel.getTaskCategory());
                                bundle2.putString("taskDeadline", completedTaskModel.getTaskDeadline());
                                bundle2.putString("parentTaskId", completedTaskModel.getParentTaskId());
                                bundle2.putString("taskPredictedDuration", completedTaskModel.getTaskPredictedDuration());
                                Intent intent1 = new Intent(nContext, rescheduleCompletedTask.class);
                                intent1.putExtra("rescheduleCompletedTaskDetails", bundle2);
                                nContext.startActivity(intent1);
                                break;

                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return nTaskModelArrayList.size();
    }
}

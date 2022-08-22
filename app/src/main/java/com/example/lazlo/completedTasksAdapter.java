package com.example.lazlo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    }

    @Override
    public int getItemCount() {
        return nTaskModelArrayList.size();
    }
}

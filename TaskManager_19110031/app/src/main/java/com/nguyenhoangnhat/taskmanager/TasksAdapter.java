package com.nguyenhoangnhat.taskmanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {


    Context mContext;
    private List<Task> items;
    TransferData transferData;

    public TasksAdapter(Context mContext, List<Task> items) {
        this.mContext = mContext;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.task_list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Task item = items.get(position);
        holder.description.setText(item.getDescription());
        holder.title.setText(item.getTitle());
        transferData = (MainActivity)mContext;
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transferData.dataTransfer(item);
            }
        });


        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteTasksID(item.getID(), item.getTitle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView description;
        public  ImageButton btnEdit;
        public  ImageButton btnDelete;
        public ViewHolder(View itemView) {
            super(itemView);
            title =  itemView.findViewById(R.id.titleTextView);
            description = itemView.findViewById(R.id.descriptionTextView);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }



    private void deleteTasksID(int id, String title) {

        int rowDelete = mContext.getContentResolver().delete(TasksProvider.CONTENT_URI, DBOpenHelper.TASK_ID+" = "+id, null);
        if(rowDelete>0) {
            Toast.makeText(mContext, "Delete Task " + title, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(mContext, "Can't Delete Task " + title, Toast.LENGTH_LONG).show();
        }
    }
}

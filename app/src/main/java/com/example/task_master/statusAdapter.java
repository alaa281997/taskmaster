package com.example.task_master;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Task;

import java.util.List;

public class statusAdapter extends RecyclerView.Adapter<statusAdapter.ViewHolder> {
    private final List<Task> statusItems;
    private OnItemClickListener listener;



    public interface OnItemClickListener {
        void onItemClicked(int position);
        void onDeleteItem(int position);

    }
    public statusAdapter(List<Task> statusItems,OnItemClickListener listener ) {
        this.statusItems = statusItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.status_layout, parent, false);
        return new ViewHolder(view, listener);

   }

    @Override
    public void onBindViewHolder(@NonNull statusAdapter.ViewHolder holder, int position) {
        Task items =  statusItems.get(position);
        holder.statusL.setText(items.getStatus());
        holder.bodyTask.setText(items.getBody());
        holder.titleL.setText(items.getTitle());

    }

    @Override
    public int getItemCount() {
        return statusItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView titleL;
        private TextView statusL;
        private TextView bodyTask;
        private ImageView delete;

        public ViewHolder(@NonNull View itemView ,OnItemClickListener listener) {
            super(itemView);

            statusL = itemView.findViewById(R.id.statusL);
            bodyTask = itemView.findViewById(R.id.bodyTask1);
            titleL = itemView.findViewById(R.id.titleL);
            delete = itemView.findViewById(R.id.delete);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 listener.onItemClicked(getAdapterPosition());
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   listener.onDeleteItem(getAdapterPosition());


                }
            });
        }
    }
}

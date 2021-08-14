package com.example.task_master;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class statusAdapter extends RecyclerView.Adapter<statusAdapter.ViewHolder> {
    private final List<StatusItems> statusItems;
    private OnItemClickListener listener;


    public interface OnItemClickListener {
        void onItemClicked(int position);
    }
    public statusAdapter(List<StatusItems> statusItems,OnItemClickListener listener ) {
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
        StatusItems items =  statusItems.get(position);
        holder.statusL.setText(items.getState());
        holder.titleL.setText(items.getTitle());

    }

    @Override
    public int getItemCount() {
        return statusItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView statusList;
        private TextView titleL;
        private TextView statusL;

        public ViewHolder(@NonNull View itemView ,OnItemClickListener listener) {
            super(itemView);

            statusL = itemView.findViewById(R.id.statusL);
            titleL = itemView.findViewById(R.id.titleL);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 listener.onItemClicked(getAdapterPosition());
                }
            });
        }
    }
}

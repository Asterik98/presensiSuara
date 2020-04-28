package com.example.asterik.presensi.ui;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asterik.presensi.R;

import java.util.ArrayList;

public class listViewAdapter extends RecyclerView.Adapter<listViewAdapter.CategoryViewHolder> {

    private Context context;
    private ArrayList<Pegawai> listPegawai;
    public listViewAdapter(Context context,ArrayList<Pegawai>dataPresensi) {
        this.context = context;
        this.listPegawai=dataPresensi;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemRow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_row_layout, viewGroup, false);
        return new CategoryViewHolder(itemRow);
    }
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int position) {

        categoryViewHolder.bind(listPegawai.get(position));
    }

    @Override
    public int getItemCount() {
        return listPegawai.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView status;
        TextView waktu;
        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nama);
            status = itemView.findViewById(R.id.status);
            waktu = itemView.findViewById(R.id.waktu);
        }
        void bind(Pegawai data){
            name.setText(data.getName());
            status.setText(data.getStatus());
            waktu.setText(data.getJam());
        }
    }
}
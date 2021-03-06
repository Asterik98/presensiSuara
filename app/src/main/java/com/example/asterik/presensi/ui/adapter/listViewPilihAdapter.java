package com.example.asterik.presensi.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asterik.presensi.Pegawai;
import com.example.asterik.presensi.R;

import java.util.ArrayList;

public class listViewPilihAdapter extends RecyclerView.Adapter<listViewPilihAdapter.CategoryViewHolder> {
    private Context context;
    private ArrayList<Pegawai> listNama;

    public listViewPilihAdapter(Context context, ArrayList<Pegawai> dataNama) {
        this.context = context;
        this.listNama = dataNama;
    }

    @NonNull
    @Override
    public listViewPilihAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemRow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pilih_nama_item_row_layout, viewGroup, false);
        return new listViewPilihAdapter.CategoryViewHolder(itemRow);
    }

    @Override
    public void onBindViewHolder(@NonNull listViewPilihAdapter.CategoryViewHolder categoryViewHolder, int position) {
        categoryViewHolder.bind(listNama.get(position));
    }

    @Override
    public int getItemCount() {
        return listNama.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView check;
        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nama);
            check= itemView.findViewById(R.id.check);
        }
        void bind(Pegawai data){
            name.setText(data.getName());
            if(data.getJam().equals("-")){
                check.setVisibility(View.INVISIBLE);
            }else {
                check.setVisibility(View.VISIBLE);
            }
        }
    }
}

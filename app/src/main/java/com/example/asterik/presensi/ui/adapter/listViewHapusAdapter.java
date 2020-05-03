package com.example.asterik.presensi.ui.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asterik.presensi.R;

import java.util.ArrayList;

public class listViewHapusAdapter extends RecyclerView.Adapter<listViewHapusAdapter.CategoryViewHolder> {
    private Context context;
    private ArrayList<String> listHapus;
    private ArrayList<String> checked;
    public listViewHapusAdapter(Context context,ArrayList<String>dataHapus) {
        this.context = context;
        this.listHapus=dataHapus;
    }


    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemRow = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hapus_item_row_layout, viewGroup, false);
        return new CategoryViewHolder(itemRow);
    }
    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder categoryViewHolder, int position) {

        categoryViewHolder.bind(listHapus.get(position));
    }

    @Override
    public int getItemCount() {
        return listHapus.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        CheckBox name;

        CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.checkbox);
            name.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    checked.add(String.valueOf(name.getText()));
                }
            });
        }
        void bind(String data){
            name.setText(data);
        }
    }
}
package com.example.frenchopedia.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.frenchopedia.Model.Material;
import com.example.frenchopedia.Model.Practice_;
import com.example.frenchopedia.R;

import java.util.ArrayList;

public class PracticesupportAdapter extends RecyclerView.Adapter<PracticesupportAdapter.ViewHolder> {
    private ArrayList<Material> arrayListPractice;
    private Context context;
    private View.OnClickListener clickListener;

    public PracticesupportAdapter(ArrayList<Material> arrayListPractice, Context context) {
        this.arrayListPractice = arrayListPractice;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).asBitmap().load(arrayListPractice.get(position).getImage()).into(holder.img_view);
        holder.txt_1.setText(arrayListPractice.get(position).getName());
        holder.txt_2.setText(arrayListPractice.get(position).getFrench());
    }

    @Override
    public int getItemCount() {
        return arrayListPractice.size();
    }

    public void setOnClickListner(View.OnClickListener onClickListner) {
        clickListener = onClickListner;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img_view;
        TextView txt_1,txt_2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_view = itemView.findViewById(R.id.img_view);
            txt_1 = itemView.findViewById(R.id.txt_1);
            txt_2 = itemView.findViewById(R.id.txt_2);
            itemView.setTag(this);
            itemView.setOnClickListener(clickListener);
        }
    }
}

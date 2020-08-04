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
import com.example.frenchopedia.Model.Practice_;
import com.example.frenchopedia.R;

import java.util.ArrayList;

public class PracticeAdapter extends RecyclerView.Adapter<PracticeAdapter.ViewHolder>{
    private ArrayList<Practice_> arrayListPractice;
    private Context context;
    private View.OnClickListener clickListener;

    public PracticeAdapter(ArrayList<Practice_> arrayListPractice, Context context) {
        this.arrayListPractice = arrayListPractice;
        this.context = context;
    }


    @NonNull
    @Override
    public PracticeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PracticeAdapter.ViewHolder holder, int position) {
        Glide.with(context).asBitmap().load(arrayListPractice.get(position).getImage()).into(holder.imgv_pokemon);
        holder.txt_pokrmon.setText(arrayListPractice.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return arrayListPractice.size();
    }
    public void setOnClickListner(View.OnClickListener onClickListner)
    {
        clickListener = onClickListner;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgv_pokemon;
        TextView txt_pokrmon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgv_pokemon=itemView.findViewById(R.id.imgv_pokemon);
            txt_pokrmon=itemView.findViewById(R.id.txt_pokemon);
            itemView.setTag(this);
            itemView.setOnClickListener(clickListener);
        }
    }
}

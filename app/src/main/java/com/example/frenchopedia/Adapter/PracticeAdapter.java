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
        Glide.with(context).asBitmap().load(arrayListPractice.get(position).getImage()).into(holder.img_view);
        holder.txt_1.setText(arrayListPractice.get(position).getName());
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

        ImageView img_view;
        TextView txt_1;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_view=itemView.findViewById(R.id.img_view);
            txt_1=itemView.findViewById(R.id.txt_1);
            itemView.setTag(this);
            itemView.setOnClickListener(clickListener);
        }
    }
}

package com.example.footforward;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.ViewHolder>{
    private List<PriceHolder> priceList;
    private Context context;

    //Card clicking stuff
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    //Viewholder
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvShoename, tvCost;
        public ImageView ivThumbnail;

        public ViewHolder(View v, final OnItemClickListener listener){
            super(v);
            ivThumbnail = (ImageView)v.findViewById(R.id.shoe_thumbnail);
            tvShoename = (TextView)v.findViewById(R.id.shoe_name);
            tvCost = (TextView)v.findViewById(R.id.shoe_price);

            //Some item click stuff in constructor
            v.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    if (listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public PriceAdapter(List<PriceHolder> priceList,Context context){
        this.priceList = priceList;
        this.context = context;
    }

    // Yousif's function to check code working and see results
    public void printList(){
        System.out.print("[");
        for(PriceHolder it : priceList) {
            System.out.print(it.toString() + ", ");
        }
        System.out.print("]");
    }

    @Override
    public PriceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_cards, parent, false);
        ViewHolder vh = new ViewHolder(v, mListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(PriceAdapter.ViewHolder holder, int position){
        PriceHolder article = priceList.get(position);
        holder.tvShoename.setText(article.getShoeName());
        holder.tvCost.setText(article.getShoeCost());

        Picasso.with(context)
                .load(article.getShoeThumbnail())
                .resize(1500,750)
                .centerCrop()
                .error(R.drawable.ic_action_line_chart)
                .into(holder.ivThumbnail);
    }

    @Override
    public int getItemCount(){
        return priceList.size();
    }
}
package com.example.footforward;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
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
        public TextView tvTitle, tvContent, tvURL;
        public ImageView tvImage;

        public ViewHolder(View v, final OnItemClickListener listener){
            super(v);
            tvTitle = (TextView)v.findViewById(R.id.title_text);
            tvContent = (TextView)v.findViewById(R.id.article_text);
            tvImage = (ImageView)v.findViewById(R.id.article_image);
            tvURL = (TextView)v.findViewById(R.id.article_url);

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

    @Override
    public PriceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_cards, parent, false);
        ViewHolder vh = new ViewHolder(v, mListener);
        return vh;
    }

    @Override
    public void onBindViewHolder(PriceAdapter.ViewHolder holder, int position){
        PriceHolder price = priceList.get(position);
        holder.tvTitle.setText(price.getArticleTitle());
        holder.tvContent.setText(price.getArticleContent());

        holder.tvURL.setText(price.getArticleUrl());

        Picasso.with(context)
                .load(price.getArticleUrl())
                .resize(200,200)
                .centerCrop()
                .error(R.drawable.ic_action_line_chart)
                .into(holder.tvImage);

    }

    @Override
    public int getItemCount(){
        return priceList.size();
    }
}
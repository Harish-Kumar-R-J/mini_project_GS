package com.example.mini_project;

import static android.R.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class adapter extends RecyclerView.Adapter<adapter.ViewHolder> {

    List<String> p_name = new ArrayList<>();
    List<String> p_desc = new ArrayList<>();
    List<String> p_price = new ArrayList<>();
    List<String> p_image = new ArrayList<>();
    List<String> document = new ArrayList<>();
    List<String> p_email = new ArrayList<>();
    Context context;

    public adapter(Context context, List<String> p_name, List<String> p_desc, List<String> p_price, List<String> p_image, List<String> p_email, List<String> doc) {
        this.p_name.clear();
        this.p_desc.clear();
        this.p_price.clear();
        this.p_image.clear();
        this.document.clear();
        this.p_name = p_name;
        this.p_desc = p_desc;
        this.p_price = p_price;
        this.p_image = p_image;
        this.p_email = p_email;
        this.context = context;
        this.document = doc;
    }

    @NonNull
    @Override
    public adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull adapter.ViewHolder holder, int position) {
        System.out.println(p_name.get(position) + " " + position + " " + p_name + "   " + "hhhhhhhhhh");
        holder.name.setText(p_name.get(position));
        holder.price.setText("₹" + p_price.get(position));
        holder.desc.setText(p_desc.get(position));
        holder.name.setBackgroundColor(16777215);
        holder.price.setBackgroundColor(16777215);
        holder.desc.setBackgroundColor(16777215);
        holder.img_btn.setBackgroundColor(16777215);
        Glide.with(context).load(p_image.get(position)).into(holder.img_btn);
        holder.setIsRecyclable(false);
        if(!context.toString().contains("home"))
        {holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, prod_edit.class);
                intent.putExtra("name", p_name.get(position));
                intent.putExtra("price", p_price.get(position));
                intent.putExtra("desc", p_desc.get(position));
                intent.putExtra("url", p_image.get(position));
                System.out.println(document.get(position)  + " " +  position + " System.out.println(the_name);System.out.println(the_name);");
                intent.putExtra("docx", document.get(position));
                context.startActivity(intent);
            }
        });}

        else
        {holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, expand.class);
                intent.putExtra("name", p_name.get(position));
                intent.putExtra("price", "₹" + p_price.get(position));
                intent.putExtra("desc", p_desc.get(position));
                intent.putExtra("url", p_image.get(position));
                intent.putExtra("email", p_email.get(position));
                context.startActivity(intent);
            }
        });}

    }

    @Override
    public int getItemCount() {
        System.out.println(p_name.size() + " p_name.size()");
        return p_name.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, desc;
View layout;

        ImageView img_btn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

             layout = itemView.findViewById(R.id.constraint_2);
             img_btn = itemView.findViewById(R.id.row_view_image);
             name = itemView.findViewById(R.id.row_view_prod_name);
             price = itemView.findViewById(R.id.row_view_price);
             desc = itemView.findViewById(R.id.row_view_desc);

        }
    }
}
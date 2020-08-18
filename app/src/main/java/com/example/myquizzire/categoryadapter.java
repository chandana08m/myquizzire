package com.example.myquizzire;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.widget.Toast.LENGTH_LONG;

public class categoryadapter extends RecyclerView.Adapter<categoryadapter.Viewholder> {

    private List<categorymodel>categorymodelList;
    private String temp;


    public categoryadapter(List<categorymodel> categorymodelList) {
        this.categorymodelList = categorymodelList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.category,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
      holder.setdata(categorymodelList.get(position).getUrl(),categorymodelList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return categorymodelList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{

        private CircleImageView imageView;
        private TextView title;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById((R.id.image_view));
            title=itemView.findViewById(R.id.title);
        }
        private void setdata(String url, final String title){
            Glide.with(imageView.getContext()).load(url).into(imageView);
            this.title.setText(title);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent2=new Intent(itemView.getContext(),question.class);
                    intent2.putExtra("TITLE",title);


                    itemView.getContext().startActivity(intent2);
                }
            });
        }
    }
}

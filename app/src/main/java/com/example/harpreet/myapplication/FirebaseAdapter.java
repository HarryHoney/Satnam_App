package com.example.harpreet.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FirebaseAdapter extends FirestoreRecyclerAdapter<Data,FirebaseAdapter.ViewHolder> {

    private OnitemClickListener listener;
    private Context context;

    public FirebaseAdapter(@NonNull FirestoreRecyclerOptions options) {
        super(options);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View V=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_list_item,viewGroup,false);
        context = viewGroup.getContext();
        return new ViewHolder(V);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Data model) {

        if(!(model.getName().equals("Admin")&&model.getPoints().equals("1710")))
        {holder.name.setText(model.getName());
        holder.points.setText("Points:"+model.getPoints());
        holder.match.setText("Match:"+model.getMatches());
        String image_id = model.getImage_id();
        Picasso.with(context)
                .load(image_id)
                .into(holder.person_image);}

    }

    //this method will take care of deleting the document form firebase and only feature is to be added to invoke this method
    public void deleteItem(int position)
    {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name;
        public TextView points;
        public TextView match;
        public CircleImageView person_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            points = itemView.findViewById(R.id.points);
            match = itemView.findViewById(R.id.match);
            person_image = itemView.findViewById(R.id.imageView2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && listener!=null)
                    {
                        listener.OnitemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }
    }
    public interface OnitemClickListener{
        void OnitemClick(DocumentSnapshot snapshot,int position);
    }

    public void setOnItemClickListener(OnitemClickListener onItemClickListener){
        this.listener=onItemClickListener;
    }
}

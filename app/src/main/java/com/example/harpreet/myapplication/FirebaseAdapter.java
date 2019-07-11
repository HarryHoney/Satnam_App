package com.example.harpreet.myapplication;

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

public class FirebaseAdapter extends FirestoreRecyclerAdapter<Data,FirebaseAdapter.ViewHolder> {

    private OnitemClickListener listener;

    public FirebaseAdapter(@NonNull FirestoreRecyclerOptions options) {
        super(options);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View V=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_list_item,viewGroup,false);
        return new ViewHolder(V);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Data model) {

        holder.name.setText(model.getName());
        holder.points.setText("Points:"+model.getPoints());
        holder.match.setText("Match:"+model.getMatches());
        //String imagevalue="a"+model.getCurrent_image();
        //     int id=holder.itemView.getResources().getIdentifier(imagevalue,"drawable", MainActivity.PACKAGE_NAME);
        //Drawable res = holder.itemView.getResources().getDrawable(id);
        //   holder.current_image.setImageResource(id);

    }

    //this method will take care of deleting the document form firebase and only feature is to be added to invoke this method
    public void deleteItem(int position)
    {
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public  TextView name;
        public TextView points;
        public TextView match;
        public ImageView person_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            points = itemView.findViewById(R.id.points);
            match = itemView.findViewById(R.id.match);
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

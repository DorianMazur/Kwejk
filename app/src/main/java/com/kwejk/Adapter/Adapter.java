package com.kwejk.Adapter;

import com.kwejk.Model.MemeModel;
import com.kwejk.R;
import android.widget.ArrayAdapter;
import android.content.Context;
import java.util.List;
import android.view.*;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.util.Log;
import com.squareup.picasso.Picasso;

public class Adapter extends ArrayAdapter<MemeModel> {

    Context context;
    List<MemeModel> memes;

    public Adapter(Context context, int resource, List<MemeModel> objects) {
        super(context, resource, objects);

        this.context = context;
        this.memes = objects;
    }


    @Override
    public int getCount() {
        if(memes != null)
            return memes.size();
        return 0;
    }

    @Override
    public MemeModel getItem(int position) {
        if(memes != null)
            return memes.get(position);
        return null;
    }

    @Override
    public long getItemId(int position) {
        if(memes != null)
            return memes.get(position).hashCode();
        return 0;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;
        MemeModel meme = getItem(position);

        if (convertView == null){

            //we need a new holder to hold the structure of the cell
            holder = new Holder();

            //get the XML inflation service
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //Inflate our xml cell to the convertView
            convertView = inflater.inflate(R.layout.meme, null);

            //Get xml components into our holder class
            holder.title = (TextView)convertView.findViewById(R.id.meme_title);
            convertView.findViewById(R.id.vote_up).setTag(meme.getId());
            convertView.findViewById(R.id.vote_down).setTag(meme.getId());
            holder.votesUp = (TextView)convertView.findViewById(R.id.votesUp);
            holder.votesDown = (TextView)convertView.findViewById(R.id.votesDown);
            holder.comments = (TextView)convertView.findViewById(R.id.comments);
            holder.imageView = (ImageView)convertView.findViewById(R.id.meme_imageview);

            //Attach our holder class to this particular cell
            convertView.setTag(holder);

        }else{

            holder = (Holder)convertView.getTag();

        }


        //Fill our view components with data
        holder.title.setText(meme.getTitle());
        holder.votesUp.setText(meme.getVotesUp());
        holder.votesDown.setText(meme.getVotesDown());
        holder.comments.setText(meme.getComments());
        holder.ID = meme.getId();
        Picasso.with(context).setLoggingEnabled(true);
        Picasso.with(context).load(meme.getImageUrl()).into(holder.imageView);

        return convertView;
    }

    /**
     * This holder must replicate the components in the memes.xml
     * We have a textview for the name and the surname and an imageview for the picture
     */
    public class Holder{

        TextView title;
        TextView votesUp;
        TextView votesDown;
        TextView comments;
        ImageView imageView;
        public String ID;

        public void setVotesUp(String number){
            votesUp.setText(number);
        }

        public void setVotesDown(String number){
            votesDown.setText(number);
        }

        public void setComments(String number){
            comments.setText(number);
        }

        public Integer getVotesUp(){
            return Integer.parseInt(votesUp.getText().toString());
        }

        public Integer getVotesDown(){
            return Integer.parseInt(votesDown.getText().toString());
        }

        public ImageView getImage(){
            return imageView;
        }
    }
}

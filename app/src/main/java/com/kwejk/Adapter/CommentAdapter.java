package com.kwejk.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kwejk.Model.CommentModel;
import com.kwejk.R;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;

public class CommentAdapter extends ArrayAdapter<CommentModel> {

    Context context;
    List<CommentModel> memes;

    public CommentAdapter(Context context, int resource, List<CommentModel> objects) {
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
    public CommentModel getItem(int position) {
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
        CommentModel comment = getItem(position);
        if (convertView == null){

            holder = new Holder();

            //get the XML inflation service
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.comment, null);

            //Get xml components into our holder class
            holder.author = (TextView)convertView.findViewById(R.id.authorComment);
            holder.date = (TextView)convertView.findViewById(R.id.dateComment);
            holder.text = (TextView)convertView.findViewById(R.id.textComment);

            //Attach our holder class to this particular cell
            convertView.setTag(holder);

        }else{

            holder = (Holder)convertView.getTag();

        }

        holder.author.setText(comment.getAuthor());
        holder.date.setText(comment.getDate());
        String unescapedString = StringEscapeUtils.unescapeHtml4(comment.getText());
        holder.text.setText(unescapedString);

        return convertView;
    }


    public class Holder{

        TextView author;
        TextView date;
        TextView text;

    }
}

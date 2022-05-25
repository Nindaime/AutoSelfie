package app0.com.autoselfie;

//import android.support.v7.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// The adapter class which
// extends RecyclerView Adapter
public class Adapter
        extends RecyclerView.Adapter<Adapter.MyView> {

    // List with String type
    private List<Integer> imagesId;
    private HashMap<Integer, String> map;
    private DbHelper dbHelper;

    // View Holder class which
    // extends RecyclerView.ViewHolder
    public class MyView
            extends RecyclerView.ViewHolder {

        // Text View
        ImageView imageView;
        ImageButton deleteButton;

        // parameterised constructor for View Holder class
        // which takes the view as a parameter
        public MyView(View view) {
            super(view);


            imageView = (ImageView) view
                    .findViewById(R.id.imageView);

            deleteButton = (ImageButton) view.findViewById(R.id.deleteButton);
        }
    }

    // Constructor for adapter class
    // which takes a list of String type
    public Adapter(ArrayList<Integer> imagesId, HashMap<Integer,String> map, DbHelper dbHelper) {
        this.map = map;
        this.imagesId = imagesId;
        this.dbHelper = dbHelper;

    }

    // Override onCreateViewHolder which deals
    // with the inflation of the card layout
    // as an item for the RecyclerView.
    @Override
    public MyView onCreateViewHolder(ViewGroup parent,
                                     int viewType) {

        // Inflate item.xml using LayoutInflator
        View itemView
                = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item,
                        parent,
                        false);

        // return itemView
        return new MyView(itemView);
    }

    // Override onBindViewHolder which deals
    // with the setting of different data
    // and methods related to clicks on
    // particular items of the RecyclerView.
    @Override
    public void onBindViewHolder(final MyView holder,
                                 final int position) {

        // Set the text of each item of
        // Recycler view with the list items
        int imageId = imagesId.get(position);
        String imageLocation = map.get(imageId);
        File imgFile = new File(imageLocation);


//        Log.d("Adapter ", "imageLocation "+imageLocation);
//        Log.d("Adapter ", "file exists "+imgFile.exists());


        if (imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.imageView.setImageBitmap(bitmap);

            holder.deleteButton.setOnClickListener(view -> {
                // delete from database
                dbHelper.deletePictureFromImageTable(imageId);
                //delete from file system
//                imgFile.delete();
                int newPosition = holder.getAbsoluteAdapterPosition();
                map.remove(imageId);
                imagesId.remove(newPosition);
                notifyItemChanged(newPosition);
                notifyItemRangeChanged(newPosition, imagesId.size() );

            });
        }

    }

    // Override getItemCount which Returns
    // the length of the RecyclerView.
    @Override
    public int getItemCount() {
        return imagesId.size();
    }
}

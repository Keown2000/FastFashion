package keown.cantrell.grade.activities;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import java.util.List;

import keown.cantrell.grade.R;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;                                       //saves
    private List<Upload> mUploads;                                  //contains list of uploaded items
    private OnItemClickListener mListener;


    public ImageAdapter(Context context, List<Upload> uploads) {                //constructor which allows the values to be added to adapter
        mContext = context;
        mUploads = uploads;
    }


    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);         //returns the image view
        return new ImageViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {                  //gets data out of our upload items and into our single car items
        Upload uploadCurrent = mUploads.get(position);                                  //reference to current item
        holder.textViewName.setText(uploadCurrent.getName());                           //set the text view
        Picasso.get()                                                             //get image and outs it into image view
                .load(uploadCurrent.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)                                     //icon for image loading screen
                .fit()                                                              //sizes image
                .centerCrop()                                                       //crops
                .into(holder.imageView);                                            //puts it in image view
    }
    @Override
    public int getItemCount() {
        return mUploads.size(); }                                                  //shows as many items as there are items in the the list

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public TextView textViewName;                                                //displays name of items
        public ImageView imageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);              //assigns variables
            imageView = itemView.findViewById(R.id.image_view_upload);
            itemView.setOnClickListener(this);                                      //implement on click listeners
            itemView.setOnCreateContextMenuListener(this);
        }
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();  //getting position of clicked character
                if (position != RecyclerView.NO_POSITION) {  //make sure in click is still valid
                    mListener.onItemClick(position);  //bring back to activity
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("Select Action");
            MenuItem doWhatever = menu.add(Menu.NONE, 1, 1, "Do whatever");
            MenuItem delete = menu.add(Menu.NONE, 2, 2, "Delete");
            doWhatever.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }


        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    switch (item.getItemId()) {
                        case 1:
                            mListener.onWhatEverClick(position);
                            return true;
                        case 2:
                            mListener.onDeleteClick(position);
                            return true;
                    }
                }
            }
            return false;
        }
    }
    public interface OnItemClickListener {
        void onItemClick(int position);                                         //define three methods
        void onWhatEverClick(int position);
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
package yaseerfarah22.com.nearby.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import yaseerfarah22.com.nearby.Model.POJO.Venue;
import yaseerfarah22.com.nearby.R;
import yaseerfarah22.com.nearby.Util.VenueDiffUtil;



public class VenuesCardViewAdapter extends RecyclerView.Adapter<VenuesCardViewAdapter.Pro_holder> {

    private Context context;
    private List<Venue> venueInfo;








    public VenuesCardViewAdapter(Context context) {
        this.context = context;
        this.venueInfo=new ArrayList<>();


    }

    @NonNull
    @Override
    public Pro_holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;


             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nearby_cardview, parent, false);


        return new Pro_holder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Pro_holder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.size()>0){



            Bundle bundle=(Bundle)payloads.get(0);

            for (String key:bundle.keySet()){
                if (key.trim().matches("title")){
                    holder.title.setText(bundle.getString(key));

                }else if (key.trim().matches("address")){
                    holder.address.setText(bundle.getString(key));
                }
            }



        }else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final Pro_holder holder, final int position) {





        Glide.with(context).load(venueInfo.get(holder.getAdapterPosition()).getImgUrl()).into(holder.imageView);
        holder.title.setText(venueInfo.get(holder.getAdapterPosition()).getName());
        holder.address.setText(venueInfo.get(holder.getAdapterPosition()).getLocation().getAddress());
    }

    @Override
    public int getItemCount() {
        return venueInfo.size();
    }




    public void updateVenuesList(List<Venue> venueList){


        VenueDiffUtil venueDiffUtil=new VenueDiffUtil(context,this.venueInfo,venueList);
        DiffUtil.DiffResult diffResult= DiffUtil.calculateDiff(venueDiffUtil);

        this.venueInfo.clear();
        this.venueInfo.addAll(venueList);
        diffResult.dispatchUpdatesTo(this);

    }


    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

        venueInfo.clear();

    }

    //////////////////////////////////////////////////////////
    public class Pro_holder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView title,address;

        public Pro_holder(View itemView) {
            super(itemView);
           imageView =(ImageView) itemView.findViewById(R.id.card_image);
           title=(TextView) itemView.findViewById(R.id.card_title);
           address=(TextView) itemView.findViewById(R.id.card_address);


        }
    }


}

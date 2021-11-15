package com.real_estate.realestate_dtt_sindhu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.real_estate.realestate_dtt_sindhu.model.DataModel;
import com.real_estate.realestate_dtt_sindhu.services.GPSTracker;
import com.real_estate.realestate_dtt_sindhu.R;

import java.util.ArrayList;
import java.util.Locale;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.RecyclerViewViewHolder> implements  Filterable {

    private ArrayList<DataModel> afterSearchHouseList;
    Context mContext;
    ArrayList<DataModel> originalHouseList;
    private FilterByAddress filter;
    private ItemClickListener mClickListener;

    @NonNull
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.house_list,parent,false);
        this.mContext = parent.getContext();
        return new RecyclerViewViewHolder(rootView);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewViewHolder holder, int position) {

        DataModel dataModel = originalHouseList.get(position);
        holder.setIsRecyclable(false);
        StringBuilder address =new StringBuilder(dataModel.getZip());
        address .append(" ");
        address .append(dataModel.getCity());


        holder.txtPrice.setText(dataModel.getPrice());
        holder.txtLocation.setText(address);
        holder.txtBedroom.setText(dataModel.getBedrooms());
        holder.txtbath.setText(dataModel.getBathroom());
        holder.txtsize.setText(dataModel.getSizes());


        GPSTracker mGPS = new GPSTracker(mContext);
        double distance = distance(mGPS.getLatitude(), mGPS.getLongitude(), Double.parseDouble(dataModel.getLat()), Double.parseDouble(dataModel.getLongi()));
        String distanceStringDecimal = String.format(Locale.US,"%.2f", distance);
        holder.txtdistance.setText(distanceStringDecimal);



        String pic_path = dataModel.getPicture_path();
        ImageView image = holder.houseImage;


        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.dtt_banner)
                .error(R.mipmap.dtt_banner);
        if(mContext != null) {
            Glide.with(mContext).load(pic_path).apply(options).into(image);
        }
    }


    public double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist * 1.609344);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    @Override
    public int getItemCount() {

        if (originalHouseList != null) {
            return originalHouseList.size();
        } else {
            return 0;
        }

    }

    public void setHouseList(ArrayList<DataModel> houseList) {
        this.originalHouseList = houseList;
        notifyDataSetChanged();

        this.afterSearchHouseList = new ArrayList<>();
        this.afterSearchHouseList.addAll(originalHouseList);
        this.afterSearchHouseList = originalHouseList;
        
    }


    public DataModel getItem(int id) {
        return originalHouseList.get(id);
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


    public class RecyclerViewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtPrice;
        TextView txtLocation;
        TextView txtBedroom;
        TextView txtbath;
        TextView txtsize;
        TextView txtdistance;
        ImageView houseImage;

        RecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPrice = itemView.findViewById(R.id.price);
            txtLocation = itemView.findViewById(R.id.location);
            txtBedroom = itemView.findViewById(R.id.no_of_bedroom);
            txtbath = itemView.findViewById(R.id.bathroom);
            txtsize = itemView.findViewById(R.id.layers);
            houseImage = itemView.findViewById(R.id.house_image);
            txtdistance = itemView.findViewById(R.id.distance);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


    @Override
    public Filter getFilter() {
        if (filter == null){
            filter  = new FilterByAddress();
        }
        notifyDataSetChanged();
        return filter;
    }


    private class FilterByAddress extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<DataModel> filteredItems  = new ArrayList<>();
            constraint = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                filteredItems.addAll(afterSearchHouseList);
            } else {
                for (final DataModel house  : afterSearchHouseList) {
                    if (house .getZip().toLowerCase().replaceAll("\\s+","").contains(constraint)) {
                        filteredItems.add(house);
                    }
                }
            }
            results.values = filteredItems  ;
            results.count = filteredItems.size();
            return results;
        }


        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            originalHouseList = (ArrayList<DataModel>)results.values;
            notifyDataSetChanged();
        }
    }
}
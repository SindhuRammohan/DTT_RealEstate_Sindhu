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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.real_estate.realestate_dtt_sindhu.R;
import com.real_estate.realestate_dtt_sindhu.model.HouseDataModel;
import com.real_estate.realestate_dtt_sindhu.services.Constants;
import com.real_estate.realestate_dtt_sindhu.services.GPSTracker;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.RecyclerViewViewHolder>
        implements Filterable {

    private ArrayList<HouseDataModel> afterSearchHouseList;
    private ArrayList<HouseDataModel> originalHouseList;
    private Context context;
    private FilterByAddress filterByAddress;
    private ItemClickListener clickListener;

    @NonNull
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.house_list,
                parent, false);
        this.context = parent.getContext();
        return new RecyclerViewViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewViewHolder holder, int position) {

        HouseDataModel dataModel = originalHouseList.get(position);
        holder.setIsRecyclable(false);

        //Address contains zip code and city name
        StringBuilder address = new StringBuilder(dataModel.getZip());
        address.append(context.getResources().getString(R.string.space));
        address.append(dataModel.getCity());

        holder.txtPrice.setText(currencyFormat(dataModel.getPrice()));
        holder.txtLocation.setText(address);
        holder.txtBedroom.setText(dataModel.getBedrooms());
        holder.txtbath.setText(dataModel.getBathroom());
        holder.txtsize.setText(dataModel.getSizes());

        GPSTracker mGps = new GPSTracker(context);
        String distance = distance(context,mGps.getLatitude(), mGps.getLongitude(),
                Double.parseDouble(dataModel.getLatitude()), Double.parseDouble(dataModel.getLongitude()));
        holder.txtdistance.setText(distance);

        String picPath = dataModel.getPicturePath();
        ImageView image = holder.imgHouseImage;

        RequestOptions options = new RequestOptions()
                .transform(new RoundedCorners((int) context.getResources().getDimension(R.dimen.radius_26)))
                .apply(new RequestOptions().override((int) context.getResources().getDimension(R.dimen.size_100),
                        (int) context.getResources().getDimension(R.dimen.size_200)))
                .placeholder(R.mipmap.dtt_banner)
                .error(R.mipmap.dtt_banner);
        if (context != null) {
            Glide.with(context).load(picPath).apply(options).into(image);
        }
    }

    public static String currencyFormat(String price) {
        double money = Double.parseDouble(price);
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
        //checking for decimal values. if there is no decimal values, remove 0 at the end after decimal
        if (money % 1 == 0) {
            nf.setMaximumFractionDigits(0);
        }
        price = nf.format(money);
        return price;
    }

    public String distance(Context context,double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        //converting Statute Miles to KM.
        dist = dist * Constants.minutes * Constants.miles;
        dist = (dist * Constants.kilometer);
        //Applying 2 decimal values
        String distance = String.format(Locale.US, "%.2f", dist);
        return distance + context.getResources().getString(R.string.kilometer);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / Constants.degree);
    }

    private double rad2deg(double rad) {
        return (rad * Constants.degree / Math.PI);
    }

    @Override
    public int getItemCount() {
        if (originalHouseList != null) {
            return originalHouseList.size();
        } else {
            return 0;
        }
    }

    public void setHouseList(ArrayList<HouseDataModel> houseList) {
        this.originalHouseList = houseList;
        notifyDataSetChanged();
        this.afterSearchHouseList = new ArrayList<>();
        this.afterSearchHouseList = originalHouseList;
    }

    public HouseDataModel getItem(int id) {
        return originalHouseList.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public Filter getFilter() {
        if (filterByAddress == null) {
            filterByAddress = new FilterByAddress();
        }
        return filterByAddress;
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
        ImageView imgHouseImage;

        RecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPrice = itemView.findViewById(R.id.price);
            txtLocation = itemView.findViewById(R.id.location);
            txtBedroom = itemView.findViewById(R.id.bedroom);
            txtbath = itemView.findViewById(R.id.bathroom);
            txtsize = itemView.findViewById(R.id.layers);
            imgHouseImage = itemView.findViewById(R.id.houseImage);
            txtdistance = itemView.findViewById(R.id.distance);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    private class FilterByAddress extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<HouseDataModel> filteredItems = new ArrayList<>();
            constraint = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                filteredItems.addAll(afterSearchHouseList);
            } else {
                for (final HouseDataModel house : afterSearchHouseList) {
                    String address = house.getZip() + house.getCity();
                    if (address.toLowerCase().replaceAll("\\s+", "").contains(constraint)) {
                        filteredItems.add(house);
                    }
                }
            }
            results.values = filteredItems;
            results.count = filteredItems.size();
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            originalHouseList = (ArrayList<HouseDataModel>) results.values;
            notifyDataSetChanged();
        }
    }
}
package com.real_estate.realestate_dtt_sindhu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.real_estate.realestate_dtt_sindhu.DataModel;
import com.real_estate.realestate_dtt_sindhu.R;
import java.util.ArrayList;
import java.util.Locale;

public class CustomAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener, Filterable {
    private final ArrayList<DataModel> originalHouseList;
    private ArrayList<DataModel> afterSearchHouseList;
    Context mContext;

    private FilterByAddress filter;

    private static class ViewHolder {
        TextView txtPrice;
        TextView txtLocation;
        TextView txtBedroom;
        TextView txtbath;
        TextView txtsize;
        TextView txtdistance;
        ImageView houseImage;
    }

    public CustomAdapter(ArrayList<DataModel> countryList, Context context) {
        super(context, R.layout.house_list, countryList);
        this.afterSearchHouseList = new ArrayList<>();
        this.afterSearchHouseList.addAll(countryList);
        this.originalHouseList = new ArrayList<>();
        this.originalHouseList.addAll(countryList);
        this.mContext=context;
    }
    @Override
    public Filter getFilter() {
        if (filter == null){
            filter  = new FilterByAddress();
        }
        return filter;
    }
    @Override
    public void onClick(View v) {

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModel dataModel = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.house_list, parent, false);
            viewHolder.txtPrice = convertView.findViewById(R.id.price);
            viewHolder.txtLocation = convertView.findViewById(R.id.location);
            viewHolder.txtBedroom = convertView.findViewById(R.id.no_of_bedroom);
            viewHolder.txtbath = convertView.findViewById(R.id.bathroom);
            viewHolder.txtsize = convertView.findViewById(R.id.layers);
            viewHolder.houseImage = convertView.findViewById(R.id.house_image);
            viewHolder.txtdistance = convertView.findViewById(R.id.distance);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.txtPrice.setText(convertView.getResources().getString(R.string.currency,dataModel.getPrice()));

        StringBuilder address =new StringBuilder(dataModel.getZip());
        address .append(" ");
        address .append(dataModel.getCity());

        viewHolder.txtLocation.setText(address);
        viewHolder.txtBedroom.setText(dataModel.getBedrooms());
        viewHolder.txtbath.setText(dataModel.getBathroom());
        viewHolder.txtsize.setText(dataModel.getSizes());

        String stringDecimal = String.format(Locale.US,"%.2f", dataModel.getdistance());
        viewHolder.txtdistance.setText(stringDecimal);
        String pic_path = dataModel.getPicture_path();
        ImageView image = viewHolder.houseImage;


        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.dtt_banner)
                .error(R.mipmap.dtt_banner);
        Glide.with(getContext()).load(pic_path).apply(options).into(image);



        viewHolder.houseImage.setOnClickListener(this);
        viewHolder.houseImage.setTag(position);
        return convertView;
    }


    private class FilterByAddress extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if( constraint.toString().length() > 0)
            {
                ArrayList<DataModel> filteredItems = new ArrayList<>();
                for(int i = 0, l = originalHouseList.size(); i < l; i++)
                {
                    DataModel house = originalHouseList.get(i);
                    if(house.getZip().toLowerCase().replaceAll("\\s+","").contains(constraint)) {
                        filteredItems.add(house);
                    }
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            }
            else
            {
                synchronized(this)
                {
                    result.values = originalHouseList;
                    result.count = originalHouseList.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            afterSearchHouseList = (ArrayList<DataModel>)results.values;
            notifyDataSetChanged();
            clear();
            for(int i = 0, l = afterSearchHouseList.size(); i < l; i++)
                add(afterSearchHouseList.get(i));
            notifyDataSetInvalidated();
        }
    }
}
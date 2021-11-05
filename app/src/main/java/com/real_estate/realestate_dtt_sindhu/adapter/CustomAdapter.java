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
import com.real_estate.realestate_dtt_sindhu.DataModel;
import com.real_estate.realestate_dtt_sindhu.ImageLoader;
import com.real_estate.realestate_dtt_sindhu.R;
import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener, Filterable {
    private ArrayList<DataModel> originalHouseList;
    private ArrayList<DataModel> afterSearchHouseList;
    Context mContext;

    private FilterByAddress filter;

    private String pic_path;

    public ImageLoader imageLoader;
    // View lookup cache
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

        this.afterSearchHouseList = new ArrayList<DataModel>();
        this.afterSearchHouseList.addAll(countryList);
        this.originalHouseList = new ArrayList<DataModel>();
        this.originalHouseList.addAll(countryList);


        this.mContext=context;
        imageLoader = new ImageLoader(context);
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

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.house_list, parent, false);
            viewHolder.txtPrice = (TextView) convertView.findViewById(R.id.price);
            viewHolder.txtLocation = (TextView) convertView.findViewById(R.id.location);
            viewHolder.txtBedroom = (TextView) convertView.findViewById(R.id.no_of_bedroom);
            viewHolder.txtbath = (TextView) convertView.findViewById(R.id.bathroom);
            viewHolder.txtsize = (TextView) convertView.findViewById(R.id.layers);
            viewHolder.houseImage = (ImageView) convertView.findViewById(R.id.house_image);
            viewHolder.txtdistance = (TextView) convertView.findViewById(R.id.distance);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        viewHolder.txtPrice.setText(convertView.getResources().getString(R.string.currency) + dataModel.getPrice());
        viewHolder.txtLocation.setText(dataModel.getZip());
        viewHolder.txtBedroom.setText(dataModel.getBedrooms());
        viewHolder.txtbath.setText(dataModel.getBathroom());
        viewHolder.txtsize.setText(dataModel.getSizes());

        String stringDecimal = String.format("%.2f", dataModel.getdistance());
        viewHolder.txtdistance.setText(stringDecimal);
        pic_path = dataModel.getPicture_path();
        ImageView image = viewHolder.houseImage;
        imageLoader.DisplayImage(pic_path, image);
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
            if(constraint != null && constraint.toString().length() > 0)
            {
                ArrayList<DataModel> filteredItems = new ArrayList<DataModel>();
                for(int i = 0, l = originalHouseList.size(); i < l; i++)
                {
                    DataModel country = originalHouseList.get(i);
                    if(country.getZip().toLowerCase().replaceAll("\\s+","").contains(constraint)) {
                        filteredItems.add(country);
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
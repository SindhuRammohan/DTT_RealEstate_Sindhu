package com.real_estate.realestate_dtt_sindhu;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<DataModel> implements View.OnClickListener{
    private ArrayList<DataModel> originalList;
    private ArrayList<DataModel> countryList;
    Context mContext;

    private CountryFilter filter;

    private String data;

    public ImageLoader imageLoader;
    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtType;
        TextView txtVersion;
        TextView bath;
        TextView size;
        ImageView info;
    }

    public CustomAdapter(ArrayList<DataModel> data, Context context) {
        super(context, R.layout.house_list, data);
        this.countryList = data;

        this.originalList = data;


        this.mContext=context;
        imageLoader = new ImageLoader(context);
    }
    @Override
    public Filter getFilter() {
        if (filter == null){
            filter  = new CountryFilter();
        }
        return filter;
    }
    @Override
    public void onClick(View v) {

//        int position=(Integer) v.getTag();
//        Object object= getItem(position);
//        DataModel dataModel=(DataModel)object;
//
//        switch (v.getId())
//        {
//            case R.id.item_info:
//                Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
//                        .setAction("No action", null).show();
//                break;
//        }
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
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.price);
            viewHolder.txtType = (TextView) convertView.findViewById(R.id.location);
            viewHolder.txtVersion = (TextView) convertView.findViewById(R.id.no_of_beedroom);
            viewHolder.bath = (TextView) convertView.findViewById(R.id.bathroom);
            viewHolder.size = (TextView) convertView.findViewById(R.id.layers);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.house_image);
            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

//        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
//        result.startAnimation(animation);
        lastPosition = position;

        viewHolder.txtName.setText(convertView.getResources().getString(R.string.currency) + dataModel.getName());
        viewHolder.txtType.setText(dataModel.getType());
        viewHolder.txtVersion.setText(dataModel.getVersion_number());
        viewHolder.bath.setText(dataModel.getFeature());
        viewHolder.size.setText(dataModel.getSizes());
        data = dataModel.getPicture_path();
        ImageView image = viewHolder.info;
//        ImageView image = viewHolder.info;
        Log.i("App", "Data data:" +data);
        //DisplayImage function from ImageLoader Class
        Log.i("App", "Data image:" +image.toString());
        imageLoader.DisplayImage(data, image);



        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }


    private class CountryFilter extends Filter
    {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if(constraint != null && constraint.toString().length() > 0)
            {
                ArrayList<DataModel> filteredItems = new ArrayList<DataModel>();

                for(int i = 0, l = originalList.size(); i < l; i++)
                {
                    DataModel country = originalList.get(i);
                    if(country.toString().toLowerCase().contains(constraint))
                        filteredItems.add(country);
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            }
            else
            {
                synchronized(this)
                {
                    result.values = originalList;
                    result.count = originalList.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            countryList = (ArrayList<DataModel>)results.values;
            notifyDataSetChanged();
            clear();
            for(int i = 0, l = countryList.size(); i < l; i++)
                add(countryList.get(i));
            notifyDataSetInvalidated();
        }
    }



}
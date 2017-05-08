package com.fd.gobondg0;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

class ForecastAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<ForecastEntity> objects;

    ForecastAdapter(Context context, ArrayList<ForecastEntity> forecasts) {
        ctx = context;
        objects = forecasts;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void clear(){
        objects.clear();
        notifyDataSetChanged();
    }

    public void addAll(ArrayList<ForecastEntity> arr){
        objects.addAll(arr);
        notifyDataSetChanged();
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.forecast_list_item, parent, false);
        }

        ForecastEntity item = getForecastItem(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) view.findViewById(R.id.forecastType)).setText(item.getType());
        ((TextView) view.findViewById(R.id.forecastBasicPrice)).setText(item.getBasicPrice() + "");
        ((TextView) view.findViewById(R.id.forecastStrikePrice)).setText(item.getStrikePrice() + "");
        ((TextView) view.findViewById(R.id.forecastMaturity)).setText(item.getMaturity() + "");
        ((TextView) view.findViewById(R.id.forecastVolatility)).setText(item.getVolatility() + "");
        ((TextView) view.findViewById(R.id.forecastCreatedAt)).setText(item.getDate() + "");
        return view;
    }

    // товар по позиции
    ForecastEntity getForecastItem(int position) {
        return ((ForecastEntity) getItem(position));
    }
}

package com.fd.gobondg0;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PlotLegendAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<ForecastView.PlotLegendItem> objects;

    public PlotLegendAdapter(Context context, ArrayList<ForecastView.PlotLegendItem> legends) {
        ctx = context;
        objects = legends;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            view = lInflater.inflate(R.layout.plot_legend_item, parent, false);
        }

        ForecastView.PlotLegendItem p = getPlotLegendItem(position);

        // заполняем View в пункте списка данными из товаров: наименование, цена
        // и картинка
        ((TextView) view.findViewById(R.id.legendLabelField)).setText(p.getLabel());
        ((ImageView) view.findViewById(R.id.legendColorField)).setBackgroundColor(p.getColor());
        return view;
    }

    // товар по позиции
    ForecastView.PlotLegendItem getPlotLegendItem(int position) {
        return ((ForecastView.PlotLegendItem) getItem(position));
    }
}

package com.udacity.stockhawk.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

/**
 * Created by Peter Stone on 16/01/2017.
 */

public class StockWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            Cursor data = null;

            @Override
            public void onCreate() {

                String[] projection = {
                        Contract.Quote.COLUMN_SYMBOL,
                        Contract.Quote.COLUMN_PRICE,
                        Contract.Quote.COLUMN_ABSOLUTE_CHANGE};

                data = getContentResolver().query(
                        Contract.Quote.URI,
                        projection,
                        null,
                        null,
                        null);
            }

            @Override
            public void onDataSetChanged() {
                long identity = Binder.clearCallingIdentity();
                String[] projection = {
                        Contract.Quote.COLUMN_SYMBOL,
                        Contract.Quote.COLUMN_PRICE,
                        Contract.Quote.COLUMN_ABSOLUTE_CHANGE};

                data = getContentResolver().query(
                        Contract.Quote.URI,
                        projection,
                        null,
                        null,
                        null);
                Binder.restoreCallingIdentity(identity);

            }

            @Override
            public void onDestroy() {

            }

            @Override
            public int getCount() {
                return data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {
                data.moveToPosition(position);
                RemoteViews views = new RemoteViews(getPackageName(), R.layout.stock_widget_list_item);
                int symbolColumn = data.getColumnIndex(Contract.Quote.COLUMN_SYMBOL);
                int priceColumn = data.getColumnIndex(Contract.Quote.COLUMN_PRICE);
                int changeColumn = data.getColumnIndex(Contract.Quote.COLUMN_ABSOLUTE_CHANGE);
                String symbol = data.getString(symbolColumn);
                String price = data.getString(priceColumn);
                String change = data.getString(changeColumn);
                views.setTextViewText(R.id.widget_symbol, symbol);
                views.setTextViewText(R.id.widget_value, "$" + price);
                views.setTextViewText(R.id.widget_change, change + " ($)");
                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.stock_widget_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }
        };
    }
}

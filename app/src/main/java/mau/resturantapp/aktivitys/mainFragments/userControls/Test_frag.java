package mau.resturantapp.aktivitys.mainFragments.userControls;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import mau.resturantapp.R;
import mau.resturantapp.aktivitys.dialogs.DatePickerFragment;
import mau.resturantapp.data.Product;
import mau.resturantapp.typedefs.GraphType;

public class Test_frag extends Fragment implements DatePickerDialog.OnDateSetListener {
    private View root;

    private TextView from;
    private TextView to;

    private GraphView gw;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.amdcontrol_accounting,container,false);

        from = (TextView) root.findViewById(R.id.orderHistoryCustomFrom);
        to = (TextView) root.findViewById(R.id.orderHistoryCustomTo);

        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate("from");
            }
        });

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate("to");
            }
        });

        final Spinner yInterval = (Spinner) root.findViewById(R.id.orderHistoryGraphYInterval);
        final Spinner xInterval = (Spinner) root.findViewById(R.id.orderHistoryGraphXInterval);

        ArrayAdapter<String> yap = new ArrayAdapter<>(
                getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                getProductListNames()
        );

        ArrayAdapter<String> xap = new ArrayAdapter<>(
                getContext(),
                R.layout.support_simple_spinner_dropdown_item,
                getIntervalsAsString()
        );

        yap.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        xap.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        yInterval.setAdapter(yap);
        xInterval.setAdapter(xap);

        root.findViewById(R.id.orderHistoryBuildBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gw = (GraphView) root.findViewById(R.id.orderHisttoryTest);

                gw.getViewport().setScalable(false);
                gw.getViewport().setScrollable(false);

                String f = from.getText().toString();
                String t = to.getText().toString();

                gw.getSeries().clear();

                if(!f.equals("")) {
                    if(!t.equals("")) {
                        gw.addSeries(getDataSeriesPrduct(
                                yInterval.getSelectedItem().toString(),
                                GraphType.isEnum(xInterval.getSelectedItem().toString()),
                                f,
                                t
                        ));
                    } else {
                        gw.addSeries(getDataSeriesPrduct(
                                yInterval.getSelectedItem().toString(),
                                GraphType.isEnum(xInterval.getSelectedItem().toString()),
                                f
                        ));
                    }
                } else {
                    gw.addSeries(getDataSeriesPrduct(
                            yInterval.getSelectedItem().toString(),
                            GraphType.isEnum(xInterval.getSelectedItem().toString())
                    ));
                }

                NumberFormat nf = NumberFormat.getInstance();
                nf.setMaximumFractionDigits(0);
                gw.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(nf,nf));

                gw.getViewport().setXAxisBoundsManual(true);
                gw.getGridLabelRenderer().setNumHorizontalLabels(12);

                gw.setVisibility(View.VISIBLE);

                root.findViewById(R.id.orderHistoryAddLineBtn).setVisibility(View.VISIBLE);
                root.findViewById(R.id.orderHistoryClearGraphBtn).setVisibility(View.VISIBLE);

                root.findViewById(R.id.orderHistoryGraphXInterval).setEnabled(false);
                root.findViewById(R.id.orderHistoryAddLineBtn).setClickable(true);
                root.findViewById(R.id.orderHistoryBuildBtn).setClickable(false);
            }
        });

        root.findViewById(R.id.orderHistoryAddLineBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String f = from.getText().toString();
                String t = to.getText().toString();

                if(!f.equals("")) {
                    if(!t.equals("")) {
                        gw.addSeries(getDataSeriesPrduct(
                                yInterval.getSelectedItem().toString(),
                                GraphType.isEnum(xInterval.getSelectedItem().toString()),
                                f,
                                t
                        ));
                    } else {
                        gw.addSeries(getDataSeriesPrduct(
                                yInterval.getSelectedItem().toString(),
                                GraphType.isEnum(xInterval.getSelectedItem().toString()),
                                f
                        ));
                    }
                } else {
                    gw.addSeries(getDataSeriesPrduct(
                            yInterval.getSelectedItem().toString(),
                            GraphType.isEnum(xInterval.getSelectedItem().toString())
                    ));
                }
            }
        });

        root.findViewById(R.id.orderHistoryClearGraphBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gw.removeAllSeries();

                root.findViewById(R.id.orderHistoryGraphXInterval).setEnabled(true);
                root.findViewById(R.id.orderHistoryBuildBtn).setClickable(true);
                root.findViewById(R.id.orderHistoryAddLineBtn).setClickable(false);
            }
        });

        return root;
    }

    /**
     * Will build a GraphView-series with given product and will show how many products of this type
     * has been sold over the period of time given. The type decides the interval. 12 iterations of
     * the given type will be shown, and it will use "now()" as starting date.
     *
     * @param product product in question (y-axis)
     * @param type interval (x-axis)
     * @return Series of DataPoints
     */
    private LineGraphSeries<DataPoint> getDataSeriesPrduct(String product, GraphType type) {
        // TODO For now just dummy data...
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(
                new DataPoint[]{
                        new DataPoint(1, 2),
                        new DataPoint(2, 5),
                        new DataPoint(3, 4),
                        new DataPoint(4, 6),
                        new DataPoint(5, 1),
                        new DataPoint(6, 2),
                        new DataPoint(7, 5),
                        new DataPoint(8, 4),
                        new DataPoint(9, 4),
                        new DataPoint(10, 7),
                        new DataPoint(11, 1),
                        new DataPoint(12, 3)
                }
        );

        // Create a random color for the line
        int r = new Random().nextInt(256);
        int g = new Random().nextInt(256);
        int b = new Random().nextInt(256);

        series.setColor(Color.rgb(r,g,b));

        return series;
    }

    /**
     * Will build a GraphView-series with given product and will show how many products of this type
     * has been sold over the period of time given. The type decides the interval, while the
     * from and to decides the boundaries for the time period.
     * "from" defines what date to start from, and then the interval decides "how far to go
     * backwards in time". As a default, there will never be pulled more than 12 iterations of the
     * given interval-type. E.g. for WEEK-interval, 12 weeks backwards starting "from" date
     * will be shown.
     * "from" can not be later than "now()" (basically can't be a future date).
     *
     * @param product product in question (y-axis)
     * @param type interval (x-axis)
     * @param from "from" date boundary
     * @return Series of DataPoints
     */
    private LineGraphSeries<DataPoint> getDataSeriesPrduct(String product, GraphType type, String from) {
        // TODO For now just dummy data...
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(
                new DataPoint[]{
                        new DataPoint(1, 1),
                        new DataPoint(2, 4),
                        new DataPoint(3, 8),
                        new DataPoint(4, 2),
                        new DataPoint(5, 7),
                        new DataPoint(6, 12),
                        new DataPoint(7, 11),
                        new DataPoint(8, 21),
                        new DataPoint(9, 15),
                        new DataPoint(10, 7),
                        new DataPoint(11, 13),
                        new DataPoint(12, 15)
                }
        );

        // Create a random color for the line
        int r = new Random().nextInt(256);
        int g = new Random().nextInt(256);
        int b = new Random().nextInt(256);

        series.setColor(Color.rgb(r,g,b));

        return series;
    }

    /**
     * Will build a GraphView-series with given product and will show how many products of this type
     * has been sold over the period of time given. The type decides the interval, while the
     * from and to decides the boundaries for the time period.
     * Neither "from" nor "to" can be later than "now()" (basically can't be a future date).
     *
     * @param product product in question (y-axis)
     * @param type interval (x-axis)
     * @param from "from" date boundary
     * @param to "to" date boundary
     * @return Series of DataPoints
     */
    private LineGraphSeries<DataPoint> getDataSeriesPrduct(String product, GraphType type, String from, String to) {
        /******** THIS IS HOW IT IS SUPPOSED TO BE IMPLEMENTED ********

        // First get a list of orders done with the specified product
        List<Order> orderList = new ArrayList<>();
        // Specify total time between "from" and "to"
        long totalTime = from.getTimeMillis() - to.getTimeMillis();

        // Build series with the following switch
        switch (type) {
            case DAY:
                break;
            case WEEK:
                // E.g. if weekly view was chosen Then do a count of sales between a weekly
                // time-period. This will require us to do something like this:

                // Week time in milliseconds
                long weekTime = 604800000l;

                // How many weeks we've counted so far (this will indicate where on the x-axis
                // the saleCount will be placed
                int weekCount = 0;

                // A temporary list to store the DataPoints for the different weeks
                List<DataPoint> temp = new ArrayList<>();

                // Count backwards "from" towards "to" with the interval of "weekTime"
                // We'll set an offset of 1 week, should the specified dates not be divisible with
                // a week's time in miliseconds (to make sure we get "the last week" in the count
                // as well, otherwise if e.g. end up with a time of weekTime-1 for the last week,
                // it would not be included in the salesCount, even though it is in fact within the
                // given time period.
                while(totalTime > 0) {
                    // The interval we check for (the current counting week's saleCount)
                    long interval = totalTime - weekTime;
                    // Current week's saleCount
                    int saleCount = 0;

                    // For each on all orders
                    for(Order o : orderList) {
                        // Break if the current order's date is less (before) the current interval
                        if(o.getTimestampAsDate().getTime() < interval)
                            break;

                        // Check and count each instance of the specified product in the order
                        // and count 1 up
                        for(Map.Entry<String, Product> entry : o.getCartContent()) {
                            if(entry.getValue().getName().equals(product))
                                saleCount++;
                        }
                    }

                    // Create a new DataPoint for current week and week's sale of product
                    temp.add(new DataPoint(weekCount, saleCount));
                    // Count next week...
                    weekCount++;
                    // Deduct 1 week's time from totalTime
                    totalTime -= weekTime;
                }

                // Now add the DataPoints to a LineGraphSeries. This is what we want to return
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(temp.toArray(new DataPoint[0]));

                // Create a random color for the line
                int r = new Random().nextInt(256);
                int g = new Random().nextInt(256);
                int b = new Random().nextInt(256);

                series.setColor(Color.rgb(r,g,b));

                // Return series!
                return series;

                break;
            case FORTHNIGHT:
                break;
            case MONTH:
                break;
            case QUARTERLY:
                break;
            case ANNUALLY:
                break;
            default:
                break;
        }

         ******** END OF HOW IT'S SUPPOSED TO BE DONE ********/

        // TODO For now just dummy data...
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(
                new DataPoint[]{
                        new DataPoint(1, 6),
                        new DataPoint(2, 43),
                        new DataPoint(3, 23),
                        new DataPoint(4, 12),
                        new DataPoint(5, 54),
                        new DataPoint(6, 32),
                        new DataPoint(7, 17),
                        new DataPoint(8, 14),
                        new DataPoint(9, 13),
                        new DataPoint(10, 7),
                        new DataPoint(11, 23),
                        new DataPoint(12, 33)
                }
        );

        // Create a random color for the line
        int r = new Random().nextInt(256);
        int g = new Random().nextInt(256);
        int b = new Random().nextInt(256);

        series.setColor(Color.rgb(r,g,b));

        return series;
    }

    /**
     * Returns a list of products saved in FireBase.
     *
     * @return list of products
     */
    private List<Product> getProductList() {
        List<Product> productList = new ArrayList<>();

        // TODO Get product list from Firebase
        // For now we just use some dummy data...
        productList.add(new Product("Apple", 2));
        productList.add(new Product("Banana", 2));
        productList.add(new Product("Orange", 2));
        productList.add(new Product("Watermelon", 2));
        productList.add(new Product("Plumb", 2));

        return productList;
    }

    /**
     * Returns a list of product names saved in FireBase.
     *
     * @return list of product names
     */
    private List<String> getProductListNames() {
        List<String> productList = new ArrayList<>();

        // TODO Get product list from Firebase
        // For now we just use some dummy data...
        List<Product> pl = getProductList();

        for(Product p : pl)
            productList.add(p.getName());

        return productList;
    }

    /**
     * Returns a list of GraphTypes as strings.
     *
     * @return list of GraphTypes
     */
    private List<String> getIntervalsAsString() {
        List<String> intervalList = new ArrayList<>();

        List<GraphType> graphTypes = Arrays.asList(GraphType.values());

        for(GraphType gt : graphTypes)
            intervalList.add(gt.toString());

        return intervalList;
    }

    /**
     * Pick a date. The target is to distinguish between what TextView has been clicked.
     *
     * @param target id for TextView
     */
    private void pickDate(String target) {
        DialogFragment nFrag = new DatePickerFragment(this);
        nFrag.show(getFragmentManager(), target);
    }

    /**
     * Callback from the DatePickDialog created with pickDate.
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        /*
        switch(view.getTag().toString()) {
            case "from":
                from.setText(dayOfMonth + "/" + month + "-" + year);
                break;

            case "to":
                to.setText(dayOfMonth + "/" + month + "-" + year);
                break;

            default:
                break;
        }
        */
    }
}

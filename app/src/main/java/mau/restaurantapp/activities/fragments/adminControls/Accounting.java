package mau.restaurantapp.activities.fragments.adminControls;

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

import java.security.cert.Certificate;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import mau.restaurantapp.R;
import mau.restaurantapp.activities.dialogs.DatePickerFragment;
import mau.restaurantapp.data.AppData;
import mau.restaurantapp.data.types.Order;
import mau.restaurantapp.data.types.Product;
import mau.restaurantapp.data.types.GraphType;
import mau.restaurantapp.utils.firebase.FirebaseRead;

public class Accounting extends Fragment implements DatePickerDialog.OnDateSetListener {
    private View root;

    private TextView from;
    private TextView to;

    private GraphView gw;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.admcontrol_accounting, container, false);

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

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM-yyyy");
                Calendar c = Calendar.getInstance();

                long f = 0L;
                long t = 0L;

                try {
                    c.setTime(sdf.parse(from.getText().toString()));
                    f = c.getTimeInMillis();

                    c.setTime(sdf.parse(to.getText().toString()));
                    t = c.getTimeInMillis();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                gw.getSeries().clear();

                if (!from.getText().toString().equals("")) {
                    if (!to.getText().toString().equals("")) {
                        gw.addSeries(getDataSeriesProduct(
                                yInterval.getSelectedItem().toString(),
                                GraphType.isEnum(xInterval.getSelectedItem().toString()),
                                f,
                                t
                        ));
                    } else {
                        gw.addSeries(getDataSeriesProduct(
                                yInterval.getSelectedItem().toString(),
                                GraphType.isEnum(xInterval.getSelectedItem().toString()),
                                f
                        ));
                    }
                } else {
                    gw.addSeries(getDataSeriesProduct(
                            yInterval.getSelectedItem().toString(),
                            GraphType.isEnum(xInterval.getSelectedItem().toString())
                    ));
                }

                NumberFormat nf = NumberFormat.getInstance();
                nf.setMaximumFractionDigits(0);
                gw.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(nf, nf));

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
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM-yyyy");
                Calendar c = Calendar.getInstance();

                long f = 0L;
                long t = 0L;

                try {
                    c.setTime(sdf.parse(from.getText().toString()));
                    f = c.getTimeInMillis();

                    c.setTime(sdf.parse(to.getText().toString()));
                    t = c.getTimeInMillis();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (!from.getText().toString().equals("")) {
                    if (!to.getText().toString().equals("")) {
                        gw.addSeries(getDataSeriesProduct(
                                yInterval.getSelectedItem().toString(),
                                GraphType.isEnum(xInterval.getSelectedItem().toString()),
                                f,
                                t
                        ));
                    } else {
                        gw.addSeries(getDataSeriesProduct(
                                yInterval.getSelectedItem().toString(),
                                GraphType.isEnum(xInterval.getSelectedItem().toString()),
                                f
                        ));
                    }
                } else {
                    gw.addSeries(getDataSeriesProduct(
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
     * @param type    interval (x-axis)
     * @return Series of DataPoints
     */
    private LineGraphSeries<DataPoint> getDataSeriesProduct(String product, GraphType type) {
        return getDataSeriesProduct(product, type, Calendar.getInstance().getTimeInMillis());
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
     * @param type    interval (x-axis)
     * @param from    "from" date boundary
     * @return Series of DataPoints
     */
    private LineGraphSeries<DataPoint> getDataSeriesProduct(String product, GraphType type, long from) {
        // As no "to" has been selected, we'll do a default iteration number of 12
        long interval = 86400000L;

        switch (type) {
            case DAY:
                // Already set as a day, so just skip
                break;

            case WEEK:
                // Week time in milliseconds
                interval *= 7;
                break;

            case FORTHNIGHT:
                interval *= 14;
                break;

            case MONTH:
                interval *= 30;
                break;

            case QUARTERLY:
                interval *= 30*3;
                break;

            case ANNUALLY:
                interval *= 12*30;
                break;

            default:
                // If for some reason the type given isn't regocnized, we'll use a month as standard
                interval *= 30;
                break;
        }

        long to = from - interval * 12;

        return getDataSeriesProduct(product, type, from, to);
    }

    /**
     * Will build a GraphView-series with given product and will show how many products of this type
     * has been sold over the period of time given. The type decides the interval, while the
     * from and to decides the boundaries for the time period.
     * Neither "from" nor "to" can be later than "now()" (basically can't be a future date).
     *
     * @param product product in question (y-axis)
     * @param type    interval (x-axis)
     * @param from    "from" date boundary
     * @param to      "to" date boundary
     * @return Series of DataPoints
     */
    private LineGraphSeries<DataPoint> getDataSeriesProduct(String product, GraphType type, long from, long to) {
        //******** THIS IS HOW IT IS SUPPOSED TO BE IMPLEMENTED ********

        // First get a list of orders done with the specified product
        List<Order> orderList = AppData.allOrders;

        // Specify total time between "from" and "to"
        long totalTime = from - to;

        long interval = 86400000L;

        // Set interval
        switch (type) {
            case DAY:
                // Already set as a day, so just skip
                break;

            case WEEK:
                // Week time in milliseconds
                interval *= 7;
                break;

            case FORTHNIGHT:
                interval *= 14;
                break;

            case MONTH:
                interval *= 30;
                break;

            case QUARTERLY:
                interval *= 30*3;
                break;

            case ANNUALLY:
                interval *= 12*30;
                break;

            default:
                // If for some reason the type given isn't regocnized, we'll use a month as standard
                interval *= 30;
                break;
        }

        // How many weeks we've counted so far (this will indicate where on the x-axis
        // the saleCount will be placed
        int iterations = 0;

        // A temporary list to store the DataPoints for the different weeks
        List<DataPoint> temp = new ArrayList<>();

        // Count backwards "from" towards "to" with the given interval
        while (totalTime > 0) {
            long currentIteration = totalTime - interval;

            // Current week's saleCount
            int saleCount = 0;

            // For each on all orders
            for (Order o : orderList) {
                // Break if the current order's date is less (before) the current interval
                if (o.getTimestampAsDate().getTime() < currentIteration)
                    break;

                // Check and count each instance of the specified product in the order
                // and count 1 up
                for (Product p : o.getCartContent().values()) {
                    if (p.getName().equals(product))
                        saleCount++;
                }
            }

            // Create a new DataPoint for current week and week's sale of product
            temp.add(new DataPoint(iterations, saleCount));
            // Count next week...
            iterations++;
            // Deduct 1 week's time from totalTime
            totalTime -= interval;
        }

        // Now add the DataPoints to a LineGraphSeries. This is what we want to return
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(temp.toArray(new DataPoint[0]));

        // Create a random color for the line
        int r = new Random().nextInt(256);
        int g = new Random().nextInt(256);
        int b = new Random().nextInt(256);

        series.setColor(Color.rgb(r, g, b));

        // Return series!
        return series;
    }

    /**
     * Returns a list of product names saved in FireBase.
     *
     * @return list of product names
     */
    private List<String> getProductListNames() {
        List<String> productList = new ArrayList<>();

        for (Product p : AppData.allProducts)
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

        for (GraphType gt : graphTypes)
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

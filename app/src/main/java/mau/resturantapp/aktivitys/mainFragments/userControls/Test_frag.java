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
        root = inflater.inflate(R.layout.amdcontrol_orderhistory,container,false);

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
                        new DataPoint(1, 1),
                        new DataPoint(2, 5),
                        new DataPoint(3, 0),
                        new DataPoint(4, 8),
                        new DataPoint(5, 4),
                        new DataPoint(6, 1),
                        new DataPoint(7, 9),
                        new DataPoint(8, 5),
                        new DataPoint(9, 4),
                        new DataPoint(10, 7),
                        new DataPoint(11, 1),
                        new DataPoint(12, 8)
                }
        );

        series.setColor(Color.BLUE);

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

        series.setColor(Color.GREEN);

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
        // TODO For now just dummy data...
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(
                new DataPoint[]{
                        new DataPoint(1, 6),
                        new DataPoint(2, 7),
                        new DataPoint(3, 4),
                        new DataPoint(4, 6),
                        new DataPoint(5, 2),
                        new DataPoint(6, 4),
                        new DataPoint(7, 6),
                        new DataPoint(8, 3),
                        new DataPoint(9, 6),
                        new DataPoint(10, 2),
                        new DataPoint(11, 4),
                        new DataPoint(12, 7)
                }
        );

        series.setColor(Color.RED);

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
        productList.add(new Product("Some product", 2));
        productList.add(new Product("Another product", 2));
        productList.add(new Product("A third product", 2));
        productList.add(new Product("More products", 2));
        productList.add(new Product("Final product", 2));

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

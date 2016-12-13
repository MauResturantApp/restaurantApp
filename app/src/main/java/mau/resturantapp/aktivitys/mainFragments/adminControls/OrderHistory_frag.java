package mau.resturantapp.aktivitys.mainFragments.adminControls;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mau.resturantapp.R;
import mau.resturantapp.data.Product;
import mau.resturantapp.typedefs.GraphType;

public class OrderHistory_frag extends Fragment {
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.amdcontrol_orderhistory,container,false);

        GraphView gw = (GraphView) root.findViewById(R.id.orderHisttoryTest);

        gw.getViewport().setScalable(true);
        gw.getViewport().setScrollable(true);

        gw.addSeries(getDataSeriesPrduct(getProductList().get(0), GraphType.MONTH));

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
    private LineGraphSeries<DataPoint> getDataSeriesPrduct(Product product, GraphType type) {
        LineGraphSeries<DataPoint> testSeries = new LineGraphSeries<>(
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

        return testSeries;
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
    private LineGraphSeries<DataPoint> getDataSeriesPrduct(Product product, GraphType type, int from) {
        LineGraphSeries<DataPoint> testSeries = new LineGraphSeries<>(
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

        return testSeries;
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
    private LineGraphSeries<DataPoint> getDataSeriesPrduct(Product product, GraphType type, int from, int to) {
        LineGraphSeries<DataPoint> testSeries = new LineGraphSeries<>(
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

        return testSeries;
    }

    /**
     * Returns a list of products saved in FireBase.
     *
     * @return Product list
     */
    private List<Product> getProductList() {
        List<Product> productList = new ArrayList<>();

        // TODO Get product list from Firebase
        // For now we just use some dummy data...
        productList.add(new Product("Some product", 10));
        productList.add(new Product("Another product", 16));
        productList.add(new Product("A third one", 11));
        productList.add(new Product("Final porduct", 12));

        return productList;
    }
}

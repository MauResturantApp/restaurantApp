package mau.restaurantapp.activities.fragments.adminControls;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import mau.restaurantapp.R;

/**
 * Created by AnwarC on 05/12/2016.
 */

public class NewItem extends Fragment implements View.OnClickListener {

    private Button addProduct;
    private Button removeProduct;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.admcontrol_newitem, container, false);

        addProduct = (Button) root.findViewById(R.id.addNewProduct);
        removeProduct = (Button) root.findViewById(R.id.removeProduct);

        addProduct.setOnClickListener(this);
        removeProduct.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        if (v == addProduct) {
            AddNewProduct frag = new AddNewProduct();
            this.getFragmentManager().beginTransaction()
                    .replace(R.id.frag_placeholder_adminmenu, frag, null)
                    .addToBackStack(null)
                    .commit();
        }

        if (v == removeProduct) {
            RemoveProduct frag = new RemoveProduct();
            this.getFragmentManager().beginTransaction()
                    .replace(R.id.frag_placeholder_adminmenu, frag, null)
                    .addToBackStack(null)
                    .commit();
        }
    }
}

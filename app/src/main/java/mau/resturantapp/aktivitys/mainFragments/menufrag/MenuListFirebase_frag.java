package mau.resturantapp.aktivitys.mainFragments.menufrag;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import mau.resturantapp.R;
import mau.resturantapp.aktivitys.dialogs.Dialog_ConfirmDeleteTab;
import mau.resturantapp.data.appData;
import mau.resturantapp.event.events.AskForDeleteTabeEvent;
import mau.resturantapp.utils.Firebase.FirebaseWrite;

public class MenuListFirebase_frag extends Fragment implements View.OnClickListener {
    private static final String argPage = "Arg_Page";
    private static final String argPageTitle = "Arg_PageTitle";
    private static final String argsTabPosition = "Arg_TabPosition";
    private static final String argsTabActive = "Arg_TabActive";
    private static final String argsTabKey = "Arg_TabKey";

    private int pageNumber;
    private String pageTitle;
    private int tabPosition;
    private String tabActive;
    private String tabKey;

    private Dialog_ConfirmDeleteTab d;

    private EditText name;
    private EditText position;
    private EditText active;
    private Button updateTab;
    private Button addTab;
    private Button removeTab;


    @Override
    public void onClick(View v) {
        if(v == updateTab){
            if(hasUpdate())
            FirebaseWrite.updateTab(name.getText().toString(), Integer.parseInt(position.getText().toString()), active.getText().toString(), tabKey);
        }
        if(v == addTab){
            FirebaseWrite.addTab(name.getText().toString(), Integer.parseInt(position.getText().toString()), active.getText().toString());
            //De her skal være der, så standard input er tilbage efter ny tab.
            name.setText(pageTitle);
            position.setText(Integer.toString(tabPosition));
            active.setText(tabActive);
        }
        if(v == removeTab){
            if(appData.tabs.size() > 1) {
                d = Dialog_ConfirmDeleteTab.newInstance(tabKey);
                d.show(getFragmentManager(), null);
                Log.d("clicked", "clicked+++++++++++++++++++++++++++++++++++++++++++++++++++++++" + removeTab.isActivated());
            } else {
                Toast.makeText(getContext(), "Det er ikke muligt at slette sidste tab af menuen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static MenuListFirebase_frag newInstance(int page, String pageTitle, int position, String active, String key) {
        Bundle args = new Bundle();
        args.putInt(argPage, page);
        args.putString(argPageTitle, pageTitle);
        args.putInt(argsTabPosition, position);
        args.putString(argsTabActive, active);
        args.putString(argsTabKey, key);
        Log.d("recyclerViewAdapter", "imagebutton" + page);
        MenuListFirebase_frag frag = new MenuListFirebase_frag();
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageNumber = getArguments().getInt(argPage);
        pageTitle = getArguments().getString(argPageTitle);
        tabPosition = getArguments().getInt(argsTabPosition);
        tabActive = getArguments().getString(argsTabActive);
        tabKey = getArguments().getString(argsTabKey);
        Log.d("oncreate", "pagenumber" + pageNumber);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("start", "pagenumber" + pageNumber);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("stop", "pagenumber" + pageNumber);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
        Log.d("pause", "pagenumber" + pageNumber);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("destroy", "pagenumber" + pageNumber);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rod = inflater.inflate(R.layout.menuhandler_tabs_and_products, container, false);
        Log.d("creater inhold", "page number: " + pageNumber);

        name = (EditText) rod.findViewById(R.id.tab_name_input);
        position = (EditText) rod.findViewById(R.id.tab_position_input);
        active = (EditText) rod.findViewById(R.id.tab_active_input);
        updateTab = (Button) rod.findViewById(R.id.updateTab_button);
        addTab = (Button) rod.findViewById(R.id.addTab_button);
        removeTab = (Button) rod.findViewById(R.id.removeTab_button);
        updateTab.setOnClickListener(this);
        addTab.setOnClickListener(this);
        removeTab.setOnClickListener(this);

        name.setText(pageTitle);
        position.setText(Integer.toString(tabPosition));
        active.setText(tabActive);

        return rod;
    }

    @Subscribe
    public void confirmDeleteTab(AskForDeleteTabeEvent event){
        if(tabKey.equals(event.getId())) {
            FirebaseWrite.removeTab(event.getId());
            //De her skal være der, ellers husker android tekstinput selv efter onDestroy()
            name.setText(pageTitle);
            position.setText(Integer.toString(tabPosition));
            active.setText(tabActive);
            d.onDestroy();
        }

    }

    public int getPageNumber() {
        return pageNumber;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public boolean isActive() { return appData.stringToBoolean(tabActive); }

    private boolean hasUpdate(){
        if(!(name.getText().equals(pageTitle) && position.getText().equals(tabPosition+"") && active.equals(tabActive))){
           return true;
        }

        return false;
    }
}
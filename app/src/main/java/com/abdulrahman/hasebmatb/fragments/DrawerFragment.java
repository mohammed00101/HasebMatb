package com.abdulrahman.hasebmatb.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.abdulrahman.hasebmatb.R;
import com.abdulrahman.hasebmatb.adapter.DrawerItemCustomAdapter;
import com.abdulrahman.hasebmatb.model.DataModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class DrawerFragment extends Fragment {

    private static String TAG = DrawerFragment.class.getSimpleName();

    private ListView listView;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout mDrawerLayout;
    private DrawerItemCustomAdapter adapter;
    private View containerView;
    private SharedPreferences userSharedPreferences;

    private static String[] titles = null;
    private FragmentDrawerListener drawerListener;

    public DrawerFragment() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout

        View layout = inflater.inflate(R.layout.fragment_drawer, container, false);
        userSharedPreferences = getActivity().getSharedPreferences("user", getActivity().MODE_PRIVATE);

        listView = (ListView) layout.findViewById(R.id.left_drawer);


        DataModel[] drawerItem = new DataModel[4];

        drawerItem[0] = new DataModel(R.drawable.home, "الرئيسيه");
        drawerItem[1] = new DataModel(R.drawable.news, "اخبار الطرق ");
        drawerItem[2] = new DataModel(R.drawable.setting, "اعدادات");
        drawerItem[0] = new DataModel(R.drawable.home, "الرئيسيه");
        drawerItem[1] = new DataModel(R.drawable.news, "اخبار الطرق ");
        drawerItem[2] = new DataModel(R.drawable.setting, "اعدادات");
        if( userSharedPreferences.getInt("userId", 0)==0){
            drawerItem[3] = new DataModel(R.drawable.logout, "تسجيل الدخول");
            // startService(new Intent(this,MyInstanceIDService.class ));

        }else {
            drawerItem[3] = new DataModel(R.drawable.logout, "تسجيل الخروج");
        }

        adapter = new DrawerItemCustomAdapter(getActivity(),R.layout.list_view_item_row, drawerItem);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                drawerListener.onDrawerItemSelected(view, i);
                mDrawerLayout.closeDrawer(containerView);
            }
        });



        return layout;
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        toggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                    mDrawerLayout.closeDrawer(Gravity.RIGHT);

                } else {
                    mDrawerLayout.openDrawer(Gravity.RIGHT);

                }
            }
        });

        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        toolbar.setLeft(View.SCROLL_INDICATOR_RIGHT);




    }
    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }

}

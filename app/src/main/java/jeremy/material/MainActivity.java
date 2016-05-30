package jeremy.material;


import android.app.ActivityManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import SlidingTabs.SlidingTabLayout;
import SlidingTabs.SlidingTabAdapter;


public class MainActivity extends ActionBarActivity {

    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;
    SlidingTabLayout mSlidingTabLayout;
    ViewPager mViewPager;
    private UserDbAdapter mDb;
    private  int itemcount; // 用来记录item个数
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String username = getUsername();

        //刷新所有fragment
        /*swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        tabcontent_1 tabcontent_1 = new tabcontent_1();
                        tabcontent_2 tabcontent_2 = new tabcontent_2();
                        tabcontent_3 tabcontent_3 = new tabcontent_3();
                        tabcontent_4 tabcontent_4 = new tabcontent_4();
                        android.app.FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment,tabcontent_1);
                        fragmentTransaction.replace(R.id.fragment2,tabcontent_2);
                        fragmentTransaction.replace(R.id.fragment3,tabcontent_3);
                        fragmentTransaction.replace(R.id.fragment4,tabcontent_4);
                        fragmentTransaction.commit();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );*/


        this.initLayout();
        this.bindTabEvents();
        this.bindNavDrawerEvents();

        this.addContent();
        //Fragment fragment = new tabcontent_1();
        //FragmentManager fragmentManager = getFragmentManager();
        //FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
        //Bundle bundle = new Bundle();
        //bundle.putString("username", username);
        //fragment.setArguments(bundle);// 将bundle数据加到Fragment中
        //beginTransaction.add(R.id.fragment_tab1, fragment, "fragment");
        //beginTransaction.commit();
        //Button sv_but = (Button)findViewById(R.id.save_button);
        // EditText name_text = (EditText)findViewById(R.id.editText1);
        //final EditText gender_text = (EditText)findViewById(R.id.editText2);

        //Cursor cursor = mDb.getDiary(username);
        //name_text.setText(cursor.getColumnName(1));
        //gender_text.setText(cursor.getColumnName(2));
        //sv_but.setOnClickListener(
               // new View.OnClickListener() {
                   // @Override
                    //public void onClick(View v) {

                       // String name = name_text.getText().toString();
                       // String gender = gender_text.getText().toString();
                       // mDb.updateInfo(username,name,gender);
                  //  }
               // }
       // );

    }
    public int getItemCount() {
        return itemcount;
    }

    public void setItemcount(int count) {
        this.itemcount = count;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //--------------------------------------------------------------------------
        // triggers if the user selects a menu item
        //--------------------------------------------------------------------------
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();

        if (id == R.id.action_exit) {
            System.exit(0);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        //--------------------------------------------------------------------------
        // make sure the drawer toggle is in the right state, nothing to do here
        //--------------------------------------------------------------------------
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        //--------------------------------------------------------------------------
        // close the nav drawer if user pressed the back button
        // nothing to do here
        //--------------------------------------------------------------------------
        if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    public void initLayout() {
        //--------------------------------------------------------------------------
        // create the material toolbar
        //--------------------------------------------------------------------------
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        //--------------------------------------------------------------------------
        // create the material navdrawer
        //--------------------------------------------------------------------------
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.colorMainDark));

        //--------------------------------------------------------------------------
        // create the material navdrawer toggle and bind it to the navigation_drawer
        //--------------------------------------------------------------------------
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //--------------------------------------------------------------------------
        // create the viewpager which holds the tab contents
        // tell the viewpager which tabs he have to listen to
        //--------------------------------------------------------------------------
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(5); // tabcachesize (=tabcount for better performance)
        mViewPager.setAdapter(new SlidingTabAdapter());

        //--------------------------------------------------------------------------
        // create sliding tabs and bind them to the viewpager
        //--------------------------------------------------------------------------
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);

        // use own style rules for tab layout
        mSlidingTabLayout.setCustomTabView(R.layout.toolbar_tab, R.id.toolbar_tab_txtCaption);
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.tab_indicator_color));

        mSlidingTabLayout.setDistributeEvenly(true); // each tab has the same size
        mSlidingTabLayout.setViewPager(mViewPager);

    }

    public void bindTabEvents() {
        // Tab events
        if (mSlidingTabLayout != null) {
            mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset,
                                           int positionOffsetPixels) {
                    // TODO add code tabbar is scrolled
                }

                @Override
                public void onPageSelected(int position) {
                    // TODO add code tab page select
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    // TODO add code tab scrollstate changed
                }
            });
        }
    }

    public void bindNavDrawerEvents() {
        // Click event for one Navigation element
        final LinearLayout homeButton = (LinearLayout) findViewById(R.id.HomeButton);
        homeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // close drawer if you want
                /*if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
                    mDrawerLayout.closeDrawers();
                }*/
                // display a nice toast message
                Toast.makeText(v.getContext(), "Home clicked", Toast.LENGTH_SHORT).show();
                mViewPager.setCurrentItem(0); //指向点击page
                mDrawerLayout.closeDrawers();
                // update loaded Views if you want
                //mViewPager.getAdapter().notifyDataSetChanged();
            }
        });
        final LinearLayout stuffButton = (LinearLayout) findViewById(R.id.StuffButton);
        stuffButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // close drawer if you want
                /*if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
                    mDrawerLayout.closeDrawers();
                }*/
                // display a nice toast message
                Toast.makeText(v.getContext(), "Stuff clicked", Toast.LENGTH_SHORT).show();
                mViewPager.setCurrentItem(1); //指向点击page
                mDrawerLayout.closeDrawers();
                // update loaded Views if you want
                //mViewPager.getAdapter().notifyDataSetChanged();
            }
        });
        final LinearLayout hallButton = (LinearLayout) findViewById(R.id.HallButton);
        hallButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // close drawer if you want
                /*if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
                    mDrawerLayout.closeDrawers();
                }*/
                // display a nice toast message
                Toast.makeText(v.getContext(), "Hall clicked", Toast.LENGTH_SHORT).show();
                mViewPager.setCurrentItem(2); //指向点击page
                mDrawerLayout.closeDrawers();
                // update loaded Views if you want
                //mViewPager.getAdapter().notifyDataSetChanged();
            }
        });
        final LinearLayout addButton = (LinearLayout) findViewById(R.id.AddButton);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // close drawer if you want
                /*if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
                    mDrawerLayout.closeDrawers();
                }*/
                // display a nice toast message
                Toast.makeText(v.getContext(), "Add clicked", Toast.LENGTH_SHORT).show();
                mViewPager.setCurrentItem(3); //指向点击page
                mDrawerLayout.closeDrawers();
                // update loaded Views if you want
                //mViewPager.getAdapter().notifyDataSetChanged();
            }
        });
        final LinearLayout LogoutButton = (LinearLayout) findViewById(R.id.LogoutButton);
        LogoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // close drawer if you want
                /*if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
                    mDrawerLayout.closeDrawers();
                }*/
                // display a nice toast message
                Toast.makeText(v.getContext(), "Logging Out", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this,MainActivity_Login.class);
                mDrawerLayout.closeDrawers();
                startActivity(intent);
                finish();
                // update loaded Views if you want
                //mViewPager.getAdapter().notifyDataSetChanged();
            }
        });
        final LinearLayout ExitButton = (LinearLayout) findViewById(R.id.ExitButton);
        ExitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // close drawer if you want
                /*if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
                    mDrawerLayout.closeDrawers();
                }*/
                // display a nice toast message
                Toast.makeText(v.getContext(), "Bye", Toast.LENGTH_SHORT).show();
                mDrawerLayout.closeDrawers();
                Intent startMain=new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MainActivity.this.startActivity(startMain);
                System.exit(0);
                // update loaded Views if you want
                //mViewPager.getAdapter().notifyDataSetChanged();
            }
        });
    }

    public void addContent(){

        // Inflate your Layouts here
        addTab(R.layout.tab1,"Home");
        addTab(R.layout.tab2, "Stuff");
        addTab(R.layout.tab3, "Hall");
        addTab(R.layout.tab4, "Add");
    }

    public void addTab(int layout,String tabTitle)
    {
        this.addTab(layout,tabTitle,-1);
    }
    public void addTab(int layout,String tabTitle,int position)
    {
        SlidingTabAdapter mTabs = (SlidingTabAdapter)mViewPager.getAdapter();
        mTabs.addView(getLayoutInflater().inflate(layout,null),tabTitle,position);
        mTabs.notifyDataSetChanged();
       mSlidingTabLayout.populateTabStrip();

    }

    public void removeTab()
    {
        this.removeTab(-1);
    }
    public void removeTab(int position)
    {
        SlidingTabAdapter mTabs = (SlidingTabAdapter)mViewPager.getAdapter();
        mTabs.removeView(position);
        mTabs.notifyDataSetChanged();
        mSlidingTabLayout.populateTabStrip();
    }

    public String getUsername(){
        Intent intent = getIntent();
        return intent.getStringExtra("username");
    }
    //用来实现下拉刷新tab1
    public void getFresh(){
        tabcontent_1 tabcontent_1 = new tabcontent_1();
        android.app.FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment,tabcontent_1);
        fragmentTransaction.commit();
    }
}

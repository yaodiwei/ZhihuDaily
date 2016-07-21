package com.yao.zhihudaily;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yao.zhihudaily.tool.FragmentSwitchTool;
import com.yao.zhihudaily.ui.discover.DiscoverMainFragment;
import com.yao.zhihudaily.ui.feed.FeedMainFragment;
import com.yao.zhihudaily.ui.more.MoreMainFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private LinearLayout llFeed, llDiscover, llMore;
    private ImageView ivFeed, ivDiscover, ivMore;
    private TextView tvFeed, tvDiscover, tvMore;
    private FragmentSwitchTool tool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        initView();
        tool = new FragmentSwitchTool(getFragmentManager(), R.id.flContainer);
        tool.setClickableViews(llFeed, llDiscover, llMore);
        tool.addSelectedViews(new View[]{ivFeed, tvFeed}).addSelectedViews(new View[]{ivDiscover, tvDiscover})
                .addSelectedViews(new View[]{ivMore, tvMore});
        tool.setFragments(FeedMainFragment.class, DiscoverMainFragment.class, MoreMainFragment.class);

        tool.changeTag(llFeed);
    }

    private void initView() {
        llFeed = (LinearLayout) findViewById(R.id.llFeed);
        llDiscover = (LinearLayout) findViewById(R.id.llDiscover);
        llMore = (LinearLayout) findViewById(R.id.llMore);

        ivFeed = (ImageView) findViewById(R.id.ivFeed);
        ivDiscover = (ImageView) findViewById(R.id.ivDiscover);
        ivMore = (ImageView) findViewById(R.id.ivMore);

        tvFeed = (TextView) findViewById(R.id.tvFeed);
        tvDiscover = (TextView) findViewById(R.id.tvDiscover);
        tvMore = (TextView) findViewById(R.id.tvMore);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

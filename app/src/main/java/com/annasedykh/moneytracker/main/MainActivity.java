package com.annasedykh.moneytracker.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.annasedykh.moneytracker.app.App;
import com.annasedykh.moneytracker.R;
import com.annasedykh.moneytracker.auth.AuthActivity;
import com.annasedykh.moneytracker.auth.LogoutDialog;
import com.annasedykh.moneytracker.items.AddItemActivity;
import com.annasedykh.moneytracker.items.Item;
import com.annasedykh.moneytracker.items.ItemsFragment;

public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    public static final String LOGOUT = "logout";
    private static final int LOGOUT_CODE = 777;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FloatingActionButton fab;
    private MainPagesAdapter pagesAdapter = null;

    private ActionMode actionMode = null;
    private App app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = (App) getApplication();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.main_screen_title);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        viewPager.addOnPageChangeListener(this);
        tabLayout.setupWithViewPager(viewPager);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPage = viewPager.getCurrentItem();
                String type = null;

                if (currentPage == MainPagesAdapter.PAGE_INCOMES) {
                    type = Item.TYPE_INCOMES;
                } else if (currentPage == MainPagesAdapter.PAGE_EXPENSES) {
                    type = Item.TYPE_EXPENSES;
                }

                Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
                intent.putExtra(ItemsFragment.TYPE_KEY, type);
                startActivityForResult(intent, ItemsFragment.ADD_ITEM_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (app.isAuthorized()) {
            initTabs();
        } else {
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.logout);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showDialog();
                return true;
            }
        });
        return true;
    }

    private void initTabs() {
        if (pagesAdapter == null) {
            pagesAdapter = new MainPagesAdapter(getSupportFragmentManager(), this);
            viewPager.setAdapter(pagesAdapter);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case MainPagesAdapter.PAGE_INCOMES:
            case MainPagesAdapter.PAGE_EXPENSES:
                fab.show();
                break;
            case MainPagesAdapter.PAGE_BALANCE:
                fab.hide();
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case ViewPager.SCROLL_STATE_IDLE:
                fab.setEnabled(true);
                break;
            case ViewPager.SCROLL_STATE_DRAGGING:
            case ViewPager.SCROLL_STATE_SETTLING:
                if (actionMode != null) {
                    actionMode.finish();
                }
                fab.setEnabled(false);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == LOGOUT_CODE){
            finish();
        }

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onSupportActionModeStarted(@NonNull android.support.v7.view.ActionMode mode) {
        super.onSupportActionModeStarted(mode);
        fab.hide();
        actionMode = mode;
    }

    @Override
    public void onSupportActionModeFinished(@NonNull android.support.v7.view.ActionMode mode) {
        super.onSupportActionModeFinished(mode);
        fab.show();
        actionMode = null;
    }

    /*  LOGOUT DIALOG  */

    private void showDialog() {
        LogoutDialog dialog = new LogoutDialog();
        dialog.setListener(new MainActivity.LogoutDialogListener());
        dialog.show(getSupportFragmentManager(), "LogoutDialog");
    }

    private class LogoutDialogListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    Intent intent = new Intent(MainActivity.this, AuthActivity.class);
                    intent.putExtra(LOGOUT, true);
                    startActivityForResult(intent, LOGOUT_CODE);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.cancel();
            }
        }

    }

}

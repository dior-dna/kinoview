package ru.atott.kinoview.android.activity;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import ru.atott.kinoview.android.R;
import ru.atott.kinoview.android.fragment.CinemasFragment;
import ru.atott.kinoview.android.fragment.DummyFragment;
import ru.atott.kinoview.android.fragment.FilmsFragment;

public class TabsActivity extends FragmentActivity implements ActionBar.TabListener {
    public static final int FILMS_TAB_POSITION = 0;
    public static final int SCHEDULE_TAB_POSITION = 1;
    public static final int CINEMAS_TAB_POSITION = 2;

    private ViewPager viewPager;
    private AppTabsPagerAdapter appTabsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs_activity);
        appTabsPagerAdapter = new AppTabsPagerAdapter(getSupportFragmentManager(), getApplicationContext());
        final ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(appTabsPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });
        for (int i = 0; i < appTabsPagerAdapter.getCount(); i++) {
            actionBar.addTab(
                    actionBar.newTab()
                        .setText(appTabsPagerAdapter.getPageTitle(i))
                        .setTabListener(this));
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    public static class AppTabsPagerAdapter extends FragmentPagerAdapter {
        private Context applicationContext;

        public AppTabsPagerAdapter(FragmentManager fm, Context applicationContext) {
            super(fm);
            this.applicationContext = applicationContext;
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case CINEMAS_TAB_POSITION:
                    return new CinemasFragment();
                case FILMS_TAB_POSITION:
                    return new FilmsFragment();
                default:
                    Fragment fragment = new DummyFragment();
                    Bundle args = new Bundle();
                    args.putInt(DummyFragment.ARG_SECTION_NUMBER, i + 1);
                    fragment.setArguments(args);
                    return fragment;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case FILMS_TAB_POSITION:
                    return applicationContext.getString(R.string.ui_tabs_films);
                case CINEMAS_TAB_POSITION:
                    return applicationContext.getString(R.string.ui_tabs_cinemas);
                case SCHEDULE_TAB_POSITION:
                    return applicationContext.getString(R.string.ui_tabs_schedule);
            }
            return "Title " + position;
        }
    }
}

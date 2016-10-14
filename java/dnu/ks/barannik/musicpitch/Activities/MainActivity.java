package dnu.ks.barannik.musicpitch.Activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import dnu.ks.barannik.musicpitch.R;

public class MainActivity extends AppCompatActivity implements ActionBar.TabListener {

    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager = (ViewPager) findViewById(R.id.Pager);
        final PageAdapter adapter = new PageAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                final ActionBar bar = getSupportActionBar();
                if (bar != null) {
                    bar.setSelectedNavigationItem(position);
                }
                adapter.onSelect(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        final ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            for (String name : adapter.getNames())
            bar.addTab(
                    bar.newTab()
                            .setText(name)
                            .setTabListener(this)
            );
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        pager.setCurrentItem(tab.getPosition(), true);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    private class PageAdapter extends FragmentPagerAdapter {

        private NamedFragment[] fragments = {
                new AmplitudeFragment(),
                new SpectreFragment(),
                new TunerFragment()
        };

        public PageAdapter(FragmentManager fm) {
            super(fm);
        }



        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragments[position].name;
        }

        public String[] getNames() {
            String[] names = new String[getCount()];
            int i = 0;
            for (NamedFragment f : fragments) names[i++] = f.name;
            return names;
        }

        private int curPos = 0;
        public void onSelect(int position) {
            fragments[curPos].onDeselect();
            curPos = position;
            fragments[curPos].onSelect();
        }
    }
}

package com.heclient.heapp;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.ai.cmcchina.crm.util.UIUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.heclient.heapp.ui.dashboard.DashboardFragment;
import com.heclient.heapp.ui.home.HomeFragment;
import com.heclient.heapp.ui.notifications.NotificationsFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navView ;
    private NavController navController ;
    private AppBarConfiguration appBarConfiguration;


    private List<Fragment> mFragments;
    private List<String> mFragmentTags;
    private String CURRENT_INDEX="CurrentIndex"; //fragment 标识
    private int mCurrentIndex=0;//上次fragment的位置


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            if (savedInstanceState != null) {
              //  System.out.println("MainActivity-oncreate:savedInstanceState非空");
                /*获取保存的fragment  没有的话返回null*/
                for (int i = 0; i <3; i++) {
                    Fragment fragment = getSupportFragmentManager().getFragment(savedInstanceState, mFragmentTags.get(i));
                    if (fragment != null) {
                        mFragments.set(i, fragment);
                    }
                }
                mCurrentIndex = savedInstanceState.getInt(CURRENT_INDEX,0);
            }
        }


        navView = findViewById(R.id.nav_view);
        //BottomNavigationView navView = findViewById(R.id.nav_view);
        /*initView(savedInstanceState);*/
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.navigation_home) {
                    mCurrentIndex=0;
                   // UIUtil.toast(MainActivity.this, (Object) "登陆主页");


                }
                if (destination.getId() == R.id.navigation_dashboard) {
                    mCurrentIndex=1;
                  //  UIUtil.toast(MainActivity.this, (Object) "数据采集");

                }
                if (destination.getId() == R.id.navigation_notifications) {
                    mCurrentIndex=2;
                   // UIUtil.toast(MainActivity.this, (Object) "数据处理");

                }
            }
        });
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        initData();
    }

    private void initData() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //通过TAG查找得到该Fragment，第一次还没添加的时候得到的fragment为null
        FragmentManager fm= getSupportFragmentManager();
        //mFragments.get(0)
        Fragment homefragment = fm.findFragmentByTag(HomeFragment.TAG);
        if ( homefragment == null ) {
            homefragment = new HomeFragment();
            fm.beginTransaction().add(R.id.nav_host_fragment, homefragment,HomeFragment.TAG).hide(homefragment).commit();
        }
        Fragment dashboardFragment = fm.findFragmentByTag(DashboardFragment.TAG);
        if ( dashboardFragment == null ) {
            dashboardFragment = new HomeFragment();
            fm.beginTransaction().add(R.id.nav_host_fragment, dashboardFragment,DashboardFragment.TAG).hide(dashboardFragment).commit();
        }
        Fragment notificationsfragment = fm.findFragmentByTag(NotificationsFragment.TAG);
        if ( notificationsfragment == null ) {
            notificationsfragment = new NotificationsFragment();
            fm.beginTransaction().add(R.id.nav_host_fragment,notificationsfragment,NotificationsFragment.TAG).hide(notificationsfragment).commit();
        }

/*        */


    }

   /* private void initView(Bundle savedInstanceState) {
       *//* nav_bottom = (BottomNavigationView) findViewById(R.id.nav_bottom);*//*
        navView =(BottomNavigationView)findViewById(R.id.nav_view);
        if (savedInstanceState == null){
            //根据传入的Bundle对象判断是正常启动还是重建 true表示正常启动，false表示重建
            setSelectedFragment(0);
        }
        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        setSelectedFragment(0);
                        break;
                    case R.id.navigation_dashboard:
                        setSelectedFragment(1);
                        break;
                    case R.id.navigation_notifications:
                        setSelectedFragment(2);
                        break;
                }
                return true;
            }
        });
    }*/

    /**
     * 根据位置选择Fragment
     * position 要选中的fragment的位置
     */
/*    private void  setSelectedFragment(int position){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //要显示的Fragment
        Fragment currentFragment = fragmentManager.findFragmentByTag("fragment" + position);//要显示的fragment(解决了activity重建时新建实例的问题)
        //要隐藏的Fragment
        Fragment hideFragment = fragmentManager.findFragmentByTag("fragment" + lastPosition);//要隐藏的fragment(解决了activity重建时新建实例的问题)
        if (position != lastPosition){//如果位置不同
            if (hideFragment != null){//如果要隐藏的fragment存在，则隐藏
                transaction.hide(hideFragment);
            }
            if (currentFragment == null){//如果要显示的fragment不存在，则新加并提交事务
                currentFragment = mFragments.get(position);
                transaction.add(R.id.nav_host_fragment, currentFragment,"fragment"+position);
            }else {//如果要显示的存在则直接显示
                transaction.show(currentFragment);
            }
        }

        if (position == lastPosition){//如果位置相同
            if (currentFragment == null){//如果fragment不存在(第一次启动应用的时候)
                currentFragment = mFragments.get(position);
                transaction.add(R.id.nav_host_fragment, currentFragment,"fragment"+position);
            }//如果位置相同，且fragment存在，则不作任何操作
        }
        transaction.commit();//提交事务
        lastPosition = position;//更新要隐藏的fragment的位置
    }*/
    //处理重影
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
/*      mFragments = new ArrayList<>();
        mFragments.add(HomeFragment.newInstance());
        mFragments.add(DashboardFragment.newInstance());
        mFragments.add(NotificationsFragment.newInstance());*/
        mFragmentTags= new ArrayList<String>();
        mFragmentTags.add(HomeFragment.TAG);
        mFragmentTags.add(DashboardFragment.TAG);
        mFragmentTags.add(NotificationsFragment.TAG);

        /*fragment不为空时 保存*/
        for (int i = 0; i <3; i++) {
            //确保fragment是否已经加入到fragment manager中
            if (mFragments.get(i).isAdded() && mFragments.get(i) != null) {
                //保存已加载的Fragment
                getSupportFragmentManager().putFragment(outState, mFragmentTags.get(i),
                        mFragments.get(i));
            }
        }
        //传入当前选中的tab值，在销毁重启后再定向到该tab//activity重建时保存页面的位置
        outState.putInt(CURRENT_INDEX, mCurrentIndex);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentIndex = savedInstanceState.getInt(CURRENT_INDEX);//获取重建时的fragment的位置

        //setSelectedFragment(lastPosition);//恢复销毁前显示的fragment
    }
}

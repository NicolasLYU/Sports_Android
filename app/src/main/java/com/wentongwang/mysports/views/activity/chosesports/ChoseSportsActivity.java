package com.wentongwang.mysports.views.activity.chosesports;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.wentongwang.mysports.R;
import com.wentongwang.mysports.custome.NoScrollGridView;
import com.wentongwang.mysports.model.module.SportEvents;
import com.wentongwang.mysports.views.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Wentong WANG on 2016/10/14.
 */
public class ChoseSportsActivity extends BaseActivity {
    private static final int FINISHE_CHOSE_SPORTS = 0x123;

    @BindView(R.id.ll_point_container)
    protected LinearLayout mPointContainer;
    @BindView(R.id.event_type_container)
    protected ViewPager mGridViewContainer;
    @BindView(R.id.sports_chosed_container)
    protected NoScrollGridView gvSportsChoose;
    @BindView(R.id.confirm_btn)
    protected Button btnConfirm;

    private MySportsChooseAdapter sportsChooseAdapter;
    private List<SportEvents> sportEvents;
    //events are choose
    private List<SportEvents> sportEventsChoose;
    //save each gridview
    private List<View> listGViews;
    //amount of item in one page
    private int pageItemCount = 9;
    //grideview's page size
    private int gvPageSize = 1;
    private int gvColumns = 3;
    private int totalEvents = 0;

    @Override
    protected boolean notitle() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chose_sports_layout;
    }

    @Override
    protected void initDatasAndViews() {
        ButterKnife.bind(ChoseSportsActivity.this);
        listGViews = new ArrayList<>();
        sportEventsChoose = new ArrayList<>();
        initSportEvents();

        initGirdViewsAndPoints();
        mGridViewContainer.setAdapter(new MyViewPagerAdapter());
        sportsChooseAdapter = new MySportsChooseAdapter();
        gvSportsChoose.setAdapter(sportsChooseAdapter);
    }

    /**
     * 初始化运动events
     */
    private void initSportEvents() {
        sportEvents = new ArrayList<>();
        //Event从服务器获取比较好一点
        sportEvents.add(new SportEvents(R.drawable.basketball, "basketball"));
        sportEvents.add(new SportEvents(R.drawable.soccerball, "soccerball"));
        sportEvents.add(new SportEvents(R.drawable.football, "football"));
        sportEvents.add(new SportEvents(R.drawable.volleyball, "volleyball"));
        sportEvents.add(new SportEvents(R.drawable.badminton, "badminton"));
        sportEvents.add(new SportEvents(R.drawable.pingpang, "pingpang"));
        sportEvents.add(new SportEvents(R.drawable.tennis, "tennis"));
        sportEvents.add(new SportEvents(R.drawable.bicycle, "bicycle"));
        sportEvents.add(new SportEvents(R.drawable.running, "running"));

        sportEvents.add(new SportEvents(R.drawable.swimming, "swimming"));
        sportEvents.add(new SportEvents(R.drawable.exercise, "exercise"));
        sportEvents.add(new SportEvents(R.drawable.boxing, "boxing"));

        totalEvents = sportEvents.size();
    }

    /**
     * 初始化gridview底部的小圆点 and listGirdView
     */
    private void initGirdViewsAndPoints() {
        if (totalEvents > 0) {
            gvPageSize = totalEvents / pageItemCount + 1;
            for (int i = 0; i < gvPageSize; i++) {
                //add gridview to list
                listGViews.add(getViewPagerItem(i));

                View point = new View(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15, 15);
                if (i == 0) {
                    point.setBackgroundResource(R.drawable.point_selected);
                } else {
                    params.leftMargin = 10;
                    point.setBackgroundResource(R.drawable.point_normal);
                }
                mPointContainer.addView(point, params);


            }
            mPointContainer.setVisibility(View.VISIBLE);
        } else {
            mPointContainer.removeAllViews();
            mPointContainer.setVisibility(View.GONE);
        }

    }

    @Override
    protected void initEvents() {
        mGridViewContainer.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setPointSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(FINISHE_CHOSE_SPORTS);
                onBackPressed();
            }
        });
    }

    /**
     * position页的圆点变为选中
     *
     * @param position
     */
    private void setPointSelected(int position) {
        int total = mPointContainer.getChildCount();
        for (int i = 0; i < total; i++) {
            View v = mPointContainer.getChildAt(i);
            v.setBackgroundResource(position == i ? R.drawable.point_selected : R.drawable.point_normal);
        }
    }

    /**
     * 获取不同页的gridview
     *
     * @param index
     * @return
     */
    private View getViewPagerItem(int index) {
        LayoutInflater inflater = (LayoutInflater) ChoseSportsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.viewpage_gridview, null);
        GridView gridView = (GridView) layout.findViewById(R.id.grid_view);

        gridView.setNumColumns(gvColumns);

        MyGridViewAdapter adapter = new MyGridViewAdapter(ChoseSportsActivity.this, index, pageItemCount);

        gridView.setAdapter(adapter);

        return gridView;
    }

    /**
     * viewpager's adapter
     */
    private class MyViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return listGViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View gv = listGViews.get(position);
            //gridview加入到viewpager里
            mGridViewContainer.addView(gv);
            return gv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View gv = listGViews.get(position);
            mGridViewContainer.removeView(gv);
        }
    }

    /**
     * girdview's adapter in the viewpager
     */
    private class MyGridViewAdapter extends BaseAdapter {
        private List<SportEvents> items;

        private Context context;
        /**
         * ViewPager页码
         */
        private int index;
        /**
         * 根据屏幕大小计算得到的每页item个数
         */
        private int pageItemCount;

        /**
         * 构造函数
         * @param context       上下文
         * @param index         页码
         * @param pageItemCount 每页个数
         */
        public MyGridViewAdapter(Context context, int index, int pageItemCount) {
            this.context = context;
            this.index = index;
            this.pageItemCount = pageItemCount;
            items = new ArrayList<SportEvents>();

            //get pageitemcount items
            int list_index = index * pageItemCount;
            int lastItem = list_index + pageItemCount;
            if (lastItem > totalEvents) {
                lastItem = totalEvents;
            }

            for (int i = list_index; i < lastItem; i++) {
                items.add(sportEvents.get(i));
            }


        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return sportEvents.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(ChoseSportsActivity.this).inflate(R.layout.events_gridview_items, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final ViewHolder finalHolder = holder;
            final SportEvents event = items.get(position);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), event.getEvent_image());
            holder.event_icon.setImageBitmap(bitmap);
            holder.event_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (event.isSelected()) {
                        //设为不选定
                        finalHolder.selected.setVisibility(View.INVISIBLE);
                        items.get(position).setIsSelected(false);
                        //从已选择的运动中剔除
                        sportEventsChoose.remove(event);
                        sportsChooseAdapter.notifyDataSetChanged();
                    } else {
                        //设为选定
                        finalHolder.selected.setVisibility(View.VISIBLE);
                        items.get(position).setIsSelected(true);
                        //加入到已选择运动中
                        sportEventsChoose.add(event);
                        sportsChooseAdapter.notifyDataSetChanged();
                    }
                }
            });


            if (event.isSelected()) {
                holder.selected.setVisibility(View.VISIBLE);
            } else {
                holder.selected.setVisibility(View.INVISIBLE);
            }

            return convertView;
        }

        private class ViewHolder {
            ImageView event_icon;
            ImageView selected;

            public ViewHolder(View view) {
                event_icon = (ImageView) view.findViewById(R.id.event_icon);
                selected = (ImageView) view.findViewById(R.id.iv_selected);
            }
        }
    }

    /**
     * 已选择运动的girdview的adapter
     */
    class MySportsChooseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return sportEventsChoose.size();
        }

        @Override
        public Object getItem(int position) {
            return sportEventsChoose.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(ChoseSportsActivity.this).inflate(R.layout.events_gridview_items, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            SportEvents event = sportEventsChoose.get(position);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), event.getEvent_image());
            holder.event_icon.setImageBitmap(bitmap);
            holder.selected.setVisibility(View.INVISIBLE);
            return convertView;
        }

        private class ViewHolder {
            ImageView event_icon;
            ImageView selected;

            public ViewHolder(View view) {
                event_icon = (ImageView) view.findViewById(R.id.event_icon);
                selected = (ImageView) view.findViewById(R.id.iv_selected);
            }
        }
    }

}


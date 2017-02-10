package com.shaba.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.shaba.app.R;
import com.shaba.app.fragment.IndustryRecordFragment;
import com.shaba.app.fragment.ProductsFragment;
import com.shaba.app.fragment.ProductsUserFragment;
import com.shaba.app.fragment.base.FragmentUtils;
import com.shaba.app.transition.FadeInTransition;
import com.shaba.app.transition.FadeOutTransition;
import com.shaba.app.transition.SimpleTransitionListener;
import com.shaba.app.view.TransformingToolbar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class IndustryPaymentActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @Bind(R.id.toolbar_transparent)
    TransformingToolbar toolbar;
    @Bind(R.id.rg_industry_type)
    RadioGroup rg_industry_type;
//    @Bind(R.id.lv_industry)
//    ListView lv_industry;

    private int type_id = 1;
    public static final int SEARCH_CODE = 0;


    private TextView toolbar_title;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_industry_payment;
    }

    @Override
    protected void initView() {
        initToolbar();
        rg_industry_type.setOnCheckedChangeListener(this);
        rg_industry_type.check(R.id.rb_education);
//        lv_industry.setAdapter(new IndustryTypeAdapter(mList));
    }

    private void initToolbar() {
        toolbar.setTitle("");//设置Toolbar标题
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("全行业缴费");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_industry_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
            return true;
        }
        if (item.getItemId() == R.id.main_action_search) {
            transitionToSearch();
            return true;
        }
        if (item.getItemId() == R.id.main_action_record) {
            FragmentUtils.startCoolFragment(this,new IndustryRecordFragment(),R.id.fl_container_industry);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 过渡
     */
    private void transitionToSearch() {
        // create a transition that navigates to search when complete
        Transition transition = FadeOutTransition.withAction(navigateToSearchWhenDone());

        // let the TransitionManager do the heavy work for us!
        // all we have to do is change the attributes of the toolbar and the TransitionManager animates the changes
        // in this case I am removing the bounds of the toolbar (to hide the blue padding on the screen) and
        // I am hiding the contents of the Toolbar (Navigation icon, Title and Option Items)
        TransitionManager.beginDelayedTransition(toolbar, transition);
//        FrameLayout.LayoutParams frameLP = (FrameLayout.LayoutParams) toolbar.getLayoutParams();
//        frameLP.setMargins(0, 0, 0, 0);
//        toolbar.setLayoutParams(frameLP);
        toolbar.hideContent();
    }

    private Transition.TransitionListener navigateToSearchWhenDone() {
        return new SimpleTransitionListener() {
            @Override
            public void onTransitionEnd(Transition transition) {
                Intent intent = new Intent(IndustryPaymentActivity.this, SearchActivity.class);
                startActivityForResult(intent,SEARCH_CODE);
//                startActivity(intent);
                // we are handing the enter transitions ourselves
                // this line overrides that
                overridePendingTransition(0, 0);

                // by this point of execution we have animated the 'expansion' of the Toolbar and hidden its contents.
                // We are half way there. Continue to the SearchActivity to finish the animation
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        // when you are back from the SearchActivity animate the 'shrinking' of the Toolbar and
        // fade its contents back in
        fadeToolbarIn();

        // in case we are not coming here from the SearchActivity the Toolbar would have been already visible
        // so the above method has no effect
    }

    private void fadeToolbarIn() {
        TransitionManager.beginDelayedTransition(toolbar, FadeInTransition.createTransition());
//        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) toolbar.getLayoutParams();
//        layoutParams.setMargins(toolbarMargin, toolbarMargin, toolbarMargin, toolbarMargin);
        toolbar.showContent();
//        toolbar.setLayoutParams(layoutParams);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_education:
                type_id = 1;
                break;
            case R.id.rb_property:
                type_id = 2;
                break;
            case R.id.rb_else:
                type_id = 3;
                break;
        }
        replaceFragment(type_id);
    }

    private void replaceFragment(int type_id) {
        ProductsFragment fragment = new ProductsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type_id",type_id);
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.fl_container_industry_type,fragment).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == SEARCH_CODE) {
                ProductsUserFragment fragment = new ProductsUserFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("type", 2);
                bundle.putString("search", data.getStringExtra("search"));
                fragment.setArguments(bundle);
                FragmentUtils.startNormalFragment(this, fragment, R.id.fl_container_industry,"industry_payment");
            } else {
                //银联缴费记录
                ProductsUserFragment f = (ProductsUserFragment) getFragmentManager().findFragmentByTag("industry_payment");
                f.onActivityResult(requestCode,resultCode,data);
            }
        }
    }
}

package com.shaba.app.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shaba.app.R;
import com.shaba.app.transition.FadeInTransition;
import com.shaba.app.transition.FadeOutTransition;
import com.shaba.app.transition.SimpleTransitionListener;
import com.shaba.app.utils.CommonTools;
import com.shaba.app.utils.PrefUtils;
import com.shaba.app.utils.RegexUtil;
import com.shaba.app.utils.ToastUtils;
import com.shaba.app.view.Searchbar;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import butterknife.Bind;
import butterknife.ButterKnife;


public class SearchActivity extends AppCompatActivity {

    @Bind(R.id.search_toolbar)
    Searchbar searchbar;
    @Bind(R.id.id_flowlayout)
    TagFlowLayout mFlowLayout;
    @Bind(R.id.iv_delete_history)
    FrameLayout iv_delete_history;
    @Bind(R.id.ll_search_history)
    LinearLayout ll_search_history;

    EditText mSearchEidt;

    String[] mDatas;


    private TagAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        ButterKnife.bind(this);

        mSearchEidt = (EditText) searchbar.findViewById(R.id.toolbar_search_edittext);
        TextView tv_search = (TextView) searchbar.findViewById(R.id.tv_search);

        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                search();
            }
        });
        mSearchEidt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    //进行搜索操作的方法，在该方法中可以加入mEditSearchUser的非空判断
                    search();
                }
                return false;
            }
        });
        setSupportActionBar(searchbar);

        // make sure to check if this is the first time running the activity
        // we don't want to play the enter animation on configuration changes (i.e. orientation)
        if (isFirstTimeRunning(savedInstanceState)) {
            // Start with an empty looking Toolbar
            // We are going to fade its contents in, as long as the activity finishes rendering
            searchbar.hideContent();

            ViewTreeObserver viewTreeObserver = searchbar.getViewTreeObserver();
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    searchbar.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    // after the activity has finished drawing the initial layout, we are going to continue the animation
                    // that we left off from the MainActivity
                    showSearch();
                }

                private void showSearch() {
                    // use the TransitionManager to animate the changes of the Toolbar
                    TransitionManager.beginDelayedTransition(searchbar, FadeInTransition.createTransition());
                    // here we are just changing all children to VISIBLE
                    searchbar.showContent();
                }
            });
        }

        setSearchHistory();
        new Thread() {
            @Override
            public void run() {
                super.run();
                SystemClock.sleep(200);
                CommonTools.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        showKeyboard();
                    }
                });
            }
        }.start();
    }

    /**
     * 初始化查询记录
     */
    private void setSearchHistory() {

        mDatas = getHistory();

        if (mDatas != null && mDatas.length > 0) {
            ll_search_history.setVisibility(View.VISIBLE);

            adapter = new TagAdapter<String>(mDatas) {
                @Override
                public View getView(FlowLayout parent, int position, String s) {
                    TextView tv = (TextView) LayoutInflater.from(SearchActivity.this).inflate(R.layout.tv_search_history,
                            mFlowLayout, false);
                    tv.setText(s);
                    return tv;
                }
            };
            mFlowLayout.setAdapter(adapter);

            mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {
                    SearchActivity.this.setResult(5, new Intent().putExtra("search", mDatas[position]));
                    finish();
                    return true;
                }
            });

            iv_delete_history.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //清空记录
                    String username = PrefUtils.getString(SearchActivity.this, "username", "");
                    PrefUtils.putString(SearchActivity.this, username + "<search>", "");
                    ll_search_history.setVisibility(View.GONE);
                }
            });
        }
    }

    private boolean isFirstTimeRunning(Bundle savedInstanceState) {
        return savedInstanceState == null;
    }

    @Override
    public void finish() {
        // when the user tries to finish the activity we have to animate the exit
        // let's start by hiding the keyboard so that the exit seems smooth
        hideSoftInput();

        // at the same time, start the exit transition
        exitTransitionWithAction(new Runnable() {
            @Override
            public void run() {
                // which finishes the activity (for real) when done
                SearchActivity.super.finish();

                // override the system pending transition as we are handling ourselves
                overridePendingTransition(0, 0);
            }
        });
    }

    /**
     * 存储搜索记录
     */
    protected void setHistory(String value) {
        String username = PrefUtils.getString(this, "username", "");
        String history = PrefUtils.getString(this, username + "<search>", "");
        if (!history.contains(value + ",")) {
            StringBuilder sb = new StringBuilder(history);
            sb.insert(0, value + ",");
            PrefUtils.putString(this, username + "<search>", sb.toString());
        }
    }

    protected String[] getHistory() {
        String username = PrefUtils.getString(this, "username", "");
        String history = PrefUtils.getString(this, username + "<search>", "");
        String[] hisArrays = history.split(",");
        if (hisArrays[0].equals("")) {
            return null;
        }
        //只存15条记录
        if (hisArrays.length > 15) {
            String[] newArrays = new String[15];
            System.arraycopy(hisArrays, 0, newArrays, 0, 15);
            mDatas = newArrays;
        } else {
            mDatas = hisArrays;
        }
        return mDatas;
    }

    private void exitTransitionWithAction(final Runnable endingAction) {

        Transition transition = FadeOutTransition.withAction(new SimpleTransitionListener() {
            @Override
            public void onTransitionEnd(Transition transition) {
                endingAction.run();
            }
        });

        TransitionManager.beginDelayedTransition(searchbar, transition);
        searchbar.hideContent();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_user_search, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.activity_enter, R.anim.activity_exit);
            return true;
//        } else if (item.getItemId() == R.id.main_action_search) {
////            searchbar.clearText();
//            search();
//            return true;
//        }
        }
        return super.onOptionsItemSelected(item);
    }

    protected void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     */
    protected void hideSoftInput() {
        View v = getCurrentFocus();
        IBinder token = v.getWindowToken();
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }

    // 搜索功能
    private void search() {
        String searchContext = mSearchEidt.getText().toString().trim();
        if (TextUtils.isEmpty(searchContext)) {
            ToastUtils.showToast("输入框为空,请输入");
            return;
        }
        if (searchContext.endsWith("x")) {
            searchContext = searchContext.substring(0, searchContext.length() - 1) + "X";
        }
        if (!RegexUtil.IdCardRegex(searchContext)) {
            ToastUtils.showToast("请输入正确的身份证号码");
            return;
        }
        setHistory(searchContext);
        mDatas = getHistory();
        if (adapter != null)
            adapter.notifyDataChanged();
        this.setResult(5, new Intent().putExtra("search", searchContext));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
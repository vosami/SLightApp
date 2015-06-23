package com.syncworks.slightapp;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class EasyActivity extends ActionBarActivity {
    // 상단 메뉴
    private Menu menu = null;
    // 연결 상태 확인
    private boolean connectState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_easy);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_easy, menu);
        this.menu = menu;
        setConnectIcon(connectState);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                this.finish();
                break;
            case R.id.action_connect:
                if (connectState) {

                } else {

                }
                break;
            case R.id.action_help:
                break;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (connectState) {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_connected_red));
        } else {
            menu.getItem(0).setIcon(getResources().getDrawable(R.drawable.ic_disconnected));
        }
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * ActionBar 의 연결 상태 아이콘 설정
     * @param connectState 연결상태(true:연결, false:끊김)
     */
    private void setConnectIcon(boolean connectState) {
        this.connectState = connectState;
        invalidateOptionsMenu();
    }
}

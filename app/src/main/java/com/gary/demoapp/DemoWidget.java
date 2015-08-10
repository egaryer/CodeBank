package com.gary.demoapp;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
/**
 * Created by Gary on 2015/8/2.
 */
public class DemoWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        context.startService(new Intent(DemoService.ACTION_UPDATE));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    /**
     * 当用户从桌面上删除widgets的时候被调用
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
    }

    /**
     * 第一次往桌面添加Widgets的时候才会被调用，
     * 往后往桌面添加同类型的widgets时候不会被调用
     */
    @Override
    public void onEnabled(Context context) {
        //启动服务
        context.startService(new Intent(DemoService.ACTION_UPDATE));
    }

    /**
     * 最后一个同类型widgets实例被删除的时候调用
     */
    @Override
    public void onDisabled(Context context) {
        //停止服务
        context.stopService(new Intent(context, DemoService.class));
    }
}

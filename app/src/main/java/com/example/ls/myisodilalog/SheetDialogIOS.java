package com.example.ls.myisodilalog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ls on 2017/9/22.
 */

public class SheetDialogIOS {
    private Context context;
    private Display display;
    private AlertDialog alertDialog;
    private TextView tv_title, tv_cancal;
    private ScrollView scrollView;
    private LinearLayout linearLayout;
    private boolean isShowTitle = false;
    private ArrayList<SheetItem> sheetArrayList;

    /*
    * 这里我们初始化context获取屏幕管理，得到屏幕显示器。
    * */
    public SheetDialogIOS(Context context) {
        this.context = context;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    /**
     * 实例化弹窗并且设置弹出位置和属性等
     */
    public SheetDialogIOS builder() {
        //为弹窗设置布局
        View sheetDialgView = LayoutInflater.from(context).inflate(R.layout.sheet_dialog_bottom, null);
        //这里需要设置宽度最小玩屏幕的宽度。不然显示宽度很小。你可以自己试试
        Point point = new Point();
        display.getSize(point);
        //这里的display.getWidth过时了好像还可以用。你们自己试试吧。
        sheetDialgView.setMinimumWidth(point.x);

        scrollView = sheetDialgView.findViewById(R.id.scroll_view);
        linearLayout = sheetDialgView.findViewById(R.id.Layout_content_message);
        tv_title = sheetDialgView.findViewById(R.id.tv_title);
        tv_cancal = sheetDialgView.findViewById(R.id.tv_cancel);
        tv_cancal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        //实例化弹窗设置view和弹窗的样式
        alertDialog = new AlertDialog.Builder(context, R.style.ActionBootoomDialogStyle).create();
        alertDialog.setView(sheetDialgView);
        //我们知道弹窗默认弹出式在屏幕中间的。这里需要进行窗口设置为底部不然。弹窗最后会在中间
        //获取弹窗的窗口对象
        Window window = alertDialog.getWindow();
        //设置最终显示的位置为最下边
        window.setGravity(Gravity.BOTTOM);
        //如果需要考虑你的弹窗坐标位置那么下面可以通过改变layoutParams.y的值来设置出现距离底部的位置。
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.x = 0;
        layoutParams.y = 0;
        window.setAttributes(layoutParams);
        return this;
    }

    /*
     *dialog弹出后会点击屏幕或物理返回键，dialog不消失
     */
    public SheetDialogIOS setCancelable(boolean cancal) {
        alertDialog.setCancelable(cancal);
        return this;
    }

    /*
    * dialog.setCanceledOnTouchOutside(false);
    * dialog弹出后会点击屏幕，dialog不消失；点击物理返回键dialog消失
    * */
    public SheetDialogIOS setTouchOutSideIfCancal(boolean cancal) {
        alertDialog.setCanceledOnTouchOutside(cancal);
        return this;
    }


    /*
    * 显示title是否显示:这里可以设置boolean也可以不设置以为如果你需要就显示不需要就不用调这个方法.只要调用那么就设为true
    * */
    public SheetDialogIOS setTitle(String title/*, boolean isShowTitle*/) {
        this.isShowTitle = true;
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(title);
        return this;
    }

    /*
    * 这里需要定义一个静态类用来存储字体颜色的。
    * */
    public static class ColorClass {
        public static int COLOR_RED = Color.RED;
        public static int COLOR_BLUE = Color.BLUE;
    }

    /*
    * 定义一个接口用来进行点击事件的回掉。
    * */
    public interface SheetOnclickListenner {
        void sheetOnclickListenner(int index);
    }

    //添加需要的每个item要显示的内容到这个item里面。
    public SheetDialogIOS addSheetItem(String strItem, int color, SheetOnclickListenner sheetOnclickListenner) {
        if (sheetArrayList == null) {
            sheetArrayList = new ArrayList<>();
        }
        sheetArrayList.add(new SheetItem(strItem, color, sheetOnclickListenner));
        return this;
    }

    private class SheetItem {
        private int Color;
        private String strItem_title;
        private SheetOnclickListenner sheetOnclickListenner;

        public SheetItem(String strItem, int color, SheetOnclickListenner sheetOnclickListenner) {
            this.Color = color;
            this.sheetOnclickListenner = sheetOnclickListenner;
            this.strItem_title = strItem;

        }
    }

    /*
  *用来显示弹窗弹窗
  * */
    public SheetDialogIOS Show() {
        //在显示之前首先将所有的条目都初始化并且显示到我们设置的布局上面。
        setSheetItems();
        alertDialog.show();
        return this;
    }

    /*
    * 关键一步。这里讲所有添加到集合里面的条目都初始化显示到我们设置的布局alertDialgView上面。
    * */
    private void setSheetItems() {
        //首先这里判断是否集合储存了多个item。如果为空而且，没有添加那么这里我们就结束掉。只显示
        //布局文件里面的两个title和cancal
        if (sheetArrayList == null || sheetArrayList.size() <= 0) {
            return;
        }
        //但是我们需要保证高度不能太高。这里item的多少最大限制你自己可以设置为屏幕的五分之三
        if (sheetArrayList.size() >= 5) {
            LinearLayout.LayoutParams parames = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
            Point point = new Point();
            display.getSize(point);
            parames.height = point.y * 3 / 5;
            linearLayout.setLayoutParams(parames);
        }

        /*
        * 这里吧每一个item内容的都复制到初始化遍历的TextView上面然后添加单LinearLAyout上面。
        * */
        for (int i =1; i <=sheetArrayList.size(); i++) {
            final int index = i;
            SheetItem sheetItem = sheetArrayList.get(i-1);
            String strinItem = sheetItem.strItem_title;
            int color = sheetItem.Color;
            final SheetOnclickListenner listenner = (SheetOnclickListenner) sheetItem.sheetOnclickListenner;
            TextView textview = new TextView(context);
            textview.setText(strinItem);
            textview.setTextSize(17);
            textview.setGravity(Gravity.CENTER);
            //这是背景图片
            if (sheetArrayList.size() == 1) {
                if (isShowTitle) {
                    //如果标题显示。那么她为中间的所以最下边有圆角。上边不为圆角。
                    textview.setBackgroundResource(R.drawable.sheet_bottom_selector);
                } else {
                    textview.setBackgroundResource(R.drawable.sheet_one_selector);
                }
            } else {
                if (isShowTitle) {
                    if (i >= 1 && i < sheetArrayList.size()) {
                        textview.setBackgroundResource(R.drawable.sheet_middle_selector);
                    } else {
                        textview.setBackgroundResource(R.drawable.sheet_bottom_selector);
                    }
                } else {
                    if (i == 1) {
                        textview.setBackgroundResource(R.drawable.sheet_top_selector);
                    } else if (i < sheetArrayList.size()) {
                        textview.setBackgroundResource(R.drawable.sheet_middle_selector);
                    } else {
                        textview.setBackgroundResource(R.drawable.sheet_bottom_selector);
                    }
                }

            }

            //字体颜色：

            if (color == 0) {
                textview.setTextColor(Color.BLACK);
            } else {
                textview.setTextColor(color);
            }
            /*
            * 这里设置高度为了适配这里通过显示器来设置
            * */
            float scale = context.getResources().getDisplayMetrics().density;
            /*
            * 这里更具屏幕密度进行
            * */
            int height = (int) (45 * scale);
            textview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
            textview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listenner.sheetOnclickListenner(index);
                    alertDialog.dismiss();
                }
            });
            //最后添加到布局容器里面
            linearLayout.addView(textview);
        }

    }
}

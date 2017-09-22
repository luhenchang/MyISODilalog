package com.example.ls.myisodilalog;

import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void OnclickTx(View view) {
        showDialogss();
        //iniDialog();
    }

    private void showDialogss() {
        new SheetDialogIOS(MainActivity.this)
                .builder()
                .setTitle("选择照片")
                .setCancelable(true)
                .setTouchOutSideIfCancal(true)
                .addSheetItem("打开相册",
                        SheetDialogIOS.ColorClass.COLOR_BLUE,
                        new SheetDialogIOS.SheetOnclickListenner() {
                            @Override
                            public void sheetOnclickListenner(int index) {

                            }
                        })
                .addSheetItem("打开照相机",
                        SheetDialogIOS.ColorClass.COLOR_RED,
                        new SheetDialogIOS.SheetOnclickListenner() {
                            @Override
                            public void sheetOnclickListenner(int index) {

                            }
                        })
                .addSheetItem("打开镜子",
                        SheetDialogIOS.ColorClass.COLOR_RED,
                        new SheetDialogIOS.SheetOnclickListenner() {
                            @Override
                            public void sheetOnclickListenner(int index) {

                            }
                        }).Show();
    }

    private void iniDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(this, R.style.ActionBootoomDialogStyle).create();
        View view = getLayoutInflater().inflate(R.layout.dialog_sheet_layout, null);
        TextView txt_cancle = view.findViewById(R.id.txt_cancel);
        TextView txt_title = view.findViewById(R.id.txt_title);
        TextView txt_neirou = view.findViewById(R.id.txt_neirou);
        txt_title.setText("我的.9图片可以用来呀！");
        txt_neirou.setText("么么哒");
        txt_neirou.setTextColor(Color.RED);
        txt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        // 设置Dialog最小宽度为屏幕宽度
        Display display = getWindowManager().getDefaultDisplay();

        view.setMinimumWidth(display.getWidth());
        alertDialog.setView(view);


        Window dialogWindow = alertDialog.getWindow();
        dialogWindow.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);

        alertDialog.show();
    }
}

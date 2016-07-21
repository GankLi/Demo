package com.gank.demo.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.gank.demo.R;
import com.gank.demo.views.progress.AppUpdateProgress;

public class CustomDialog extends Dialog {

    public CustomDialog(Context context) {
        super(context);
    }

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
//            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
//                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
//                    View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | 0x00002000); //View.SYSTEM_UI_FLAG_IMMERSIVE_GESTURE_ISOLATED
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_FULLSCREEN); //View.SYSTEM_UI_FLAG_IMMERSIVE_GESTURE_ISOLATED
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener(){
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    Log.v("Gank6", "---visibility: " + visibility);
                }
            });
        }
    }

    public static class Builder {
        private Context context;

        public Builder(Context context) {
            this.context = context;
        }

        public CustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CustomDialog dialog = new CustomDialog(context, android.R.style.Animation);
            View layout = inflater.inflate(R.layout.dialog_custom_shutdown, null);
//            View layout = inflater.inflate(R.layout.dialog_custom, null);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            lp.dimAmount = 1.0f;
            dialog.addContentView(layout, lp);

//            layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                }
//            });
//
//            layout.findViewById(R.id.restart).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    dialog.dismiss();
//                    Toast.makeText(context, "2", Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            layout.findViewById(R.id.shutdown).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    dialog.dismiss();
//                    Toast.makeText(context, "1", Toast.LENGTH_SHORT).show();
//                }
//            });






//            final View indprolayout = layout.findViewById(R.id.indprolayout);
//            final ProgressBar indpro = (ProgressBar) layout.findViewById(R.id.indpro);
//            final AppUpdateProgress progress = (AppUpdateProgress)layout.findViewById(R.id.progress);
//            progress.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    AppUpdateProgress app = (AppUpdateProgress) v;
//                    if (app.getProgress() == 100) {
//                        progress.setVisibility(View.INVISIBLE);
//                        indprolayout.setVisibility(View.VISIBLE);
//                    }
//                    app.setProgress(app.getProgress() + 20);
//                }
//            });
//            indpro.setIndeterminateDrawable(context.getDrawable(R.drawable.progress_sunmi));
//            layout.findViewById(R.id.indprolayout).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    dialog.dismiss();
//                }
//            });
//            indprolayout.setVisibility(View.GONE);
//            progress.setVisibility(View.INVISIBLE);
//            indprolayout.setVisibility(View.VISIBLE);
            return dialog;
        }
    }
}
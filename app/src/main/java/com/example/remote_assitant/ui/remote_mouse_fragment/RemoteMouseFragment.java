package com.example.remote_assitant.ui.remote_mouse_fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.remote_assitant.R;
import com.example.remote_assitant.gesture_controller.GestureController;
import com.example.remote_assitant.main_application.App;
import com.example.remote_assitant.main_application.GlobalRepository;
import com.example.remote_assitant.utilities.Resolution;

public class RemoteMouseFragment extends Fragment {

    private final int mDecorViewFlags = View.SYSTEM_UI_FLAG_IMMERSIVE
                                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

    private final int mTranslucentNavigationFlag = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
    private final int mFullscreenFlag = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;

    private GestureController mGestureController;

    // App lifecycle methods

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mouse_control, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if(getActivity() != null) {
            final App appInstance = (App)getActivity().getApplication();

           final View layoutView = getActivity().findViewById(R.id.gesture_view);

            layoutView.post(new Runnable() {
                @Override
                public void run() {
                    appInstance.getTcpClient().sendMessage(GlobalRepository
                            .Actions
                            .getLayoutScreenInfoCommandText(new Resolution(
                                    layoutView.getWidth(), layoutView.getHeight()
                            )));
                }
            });

            mGestureController = new GestureController(layoutView.getContext(), appInstance.getTcpClient());

            view.setOnTouchListener(mGestureController);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if(getActivity() != null && getActivity().getWindow() != null)
        {
            ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
            if(actionBar != null)
               actionBar.hide();

            getActivity().getWindow().getDecorView().setSystemUiVisibility( mDecorViewFlags );
            getActivity().getWindow().addFlags(mFullscreenFlag);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() != null && getActivity().getWindow() != null) {

            getActivity().getWindow().clearFlags(mFullscreenFlag);

            // Clear the systemUiVisibility flag
            getActivity().getWindow().getDecorView().setSystemUiVisibility(0);
        }
    }

    // Gesture Methods



    // end of app lifecycle methods
}

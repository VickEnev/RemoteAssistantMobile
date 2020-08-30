package com.example.remote_assitant.ui.main_menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.remote_assitant.R;


public class MainMenuFragment extends Fragment {

    private Button mRemoteMouseButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        boolean hasErrorOccurred = false;

        mRemoteMouseButton = (Button) bindControlToListenerFactory(root,
                R.id.btn_remote_mouse_main_menu,
                R.id.nav_remote_mouse_fragment);

        if(mRemoteMouseButton == null)
            hasErrorOccurred = true;

        if(hasErrorOccurred)
            Toast.makeText(this.getContext()
                 , getResources().getText(R.string.error_set_onclick_listeners)
                 , Toast.LENGTH_SHORT).show();

        return root;
    }

    private View bindControlToListenerFactory(View rootView, @IdRes int controlResourceID, @IdRes  int navID)
    {
        if(rootView == null)
            return null;

        View control = rootView.findViewById(controlResourceID);

        if(control != null)
            control.setOnClickListener(Navigation.createNavigateOnClickListener(navID, null));

        return control;
    }


}

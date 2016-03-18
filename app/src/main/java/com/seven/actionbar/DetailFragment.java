package com.seven.actionbar;

/**
 * Created on 10/14/15.
 */
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class DetailFragment extends Fragment {
    private TextView textView;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.detail, null);
        textView = (TextView)view.findViewById(R.id.textView);

        Bundle bundle = getArguments();
        String str = bundle.getString("id");

        textView.setText(str);

        return view;
    }
}

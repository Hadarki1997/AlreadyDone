package com.example.alreadydone;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class PageFragment extends Fragment {

    private static final String ARG_PAGE_NUMBER = "page_number";

    public static PageFragment newInstance(int pageNumber) {
        PageFragment fragment = new PageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE_NUMBER, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        TextView textView = view.findViewById(R.id.nameTextView);
        int pageNumber = getArguments().getInt(ARG_PAGE_NUMBER);
        textView.setText("Page " + pageNumber);
        return view;
    }
}

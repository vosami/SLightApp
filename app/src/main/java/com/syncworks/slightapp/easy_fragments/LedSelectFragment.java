package com.syncworks.slightapp.easy_fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import com.syncworks.slightapp.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link } interface
 * to handle interaction events.
 * Use the {@link LedSelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LedSelectFragment extends Fragment {

    private boolean isInit = false;

    private ToggleButton tbRGB[];
    private ToggleButton tbSingle[];
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "LedSelectParam";

    // TODO: Rename and change types of parameters
    private int ledSelectParam;

    private OnEasyFragmentListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment LedSelectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LedSelectFragment newInstance(int param1) {
        LedSelectFragment fragment = new LedSelectFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public LedSelectFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ledSelectParam = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_led_select, container, false);
        //findViews(view);
        //initToggleBtn();
        return view;
    }

    private void findViews(View v) {
        tbRGB = new ToggleButton[getResources().getInteger(R.integer.number_of_rgb_led)];
        tbSingle = new ToggleButton[getResources().getInteger(R.integer.number_of_led)];
        /*// RGB 커넥터 토글 버튼 획득
        tbRGB[0] = (ToggleButton) v.findViewById(R.id.tb_rgb_1);
        tbRGB[1] = (ToggleButton) v.findViewById(R.id.tb_rgb_2);
        tbRGB[2] = (ToggleButton) v.findViewById(R.id.tb_rgb_3);
        // 단색 커넥터 토글 버튼 획득
        tbSingle[0] = (ToggleButton) v.findViewById(R.id.tb_single_1);
        tbSingle[1] = (ToggleButton) v.findViewById(R.id.tb_single_2);
        tbSingle[2] = (ToggleButton) v.findViewById(R.id.tb_single_3);
        tbSingle[3] = (ToggleButton) v.findViewById(R.id.tb_single_4);
        tbSingle[4] = (ToggleButton) v.findViewById(R.id.tb_single_5);
        tbSingle[5] = (ToggleButton) v.findViewById(R.id.tb_single_6);
        tbSingle[6] = (ToggleButton) v.findViewById(R.id.tb_single_7);
        tbSingle[7] = (ToggleButton) v.findViewById(R.id.tb_single_8);
        tbSingle[8] = (ToggleButton) v.findViewById(R.id.tb_single_9);*/
/*
        tbRGB[0].setOnClickListener(tbClickListener);
        tbRGB[1].setOnClickListener(tbClickListener);
        tbRGB[2].setOnClickListener(tbClickListener);
        tbSingle[0].setOnClickListener(tbClickListener);
        tbSingle[1].setOnClickListener(tbClickListener);
        tbSingle[2].setOnClickListener(tbClickListener);
        tbSingle[3].setOnClickListener(tbClickListener);
        tbSingle[4].setOnClickListener(tbClickListener);
        tbSingle[5].setOnClickListener(tbClickListener);
        tbSingle[6].setOnClickListener(tbClickListener);
        tbSingle[7].setOnClickListener(tbClickListener);
        tbSingle[8].setOnClickListener(tbClickListener);
        */
    }

    public void initToggleBtn() {
        for (int i=0;i<3;i++) {
            tbRGB[i].setChecked(false);
        }
        for (int i=0;i<9;i++) {
            tbSingle[i].setChecked(false);
        }
    }

    public boolean isRGB() {
        if (tbRGB[0].isChecked() || tbRGB[1].isChecked() || tbRGB[2].isChecked()) {
            return true;
        }
        return false;
    }

    public int getRGBSelect() {
        int retVal = 0;
        if (tbRGB[0].isChecked()) {
            retVal |= 1;
        }
        if (tbRGB[1].isChecked()) {
            retVal |= 2;
        }
        if (tbRGB[2].isChecked()) {
            retVal |= 4;
        }
        return retVal;
    }

    public int getLedSelect() {
        int retVal = 0;
        for (int i=0;i<9;i++) {
            if (tbSingle[i].isChecked()){
                retVal |= 1<<i;
            }
        }
        return retVal;
    }

    private View.OnClickListener tbClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*switch (v.getId()) {
                case R.id.tb_rgb_1:
                    clickRGB(0);
                    break;
                case R.id.tb_rgb_2:
                    clickRGB(1);
                    break;
                case R.id.tb_rgb_3:
                    clickRGB(2);
                    break;
                case R.id.tb_single_1:
                    clickSingle(0);
                    break;
                case R.id.tb_single_2:
                    clickSingle(1);
                    break;
                case R.id.tb_single_3:
                    clickSingle(2);
                    break;
                case R.id.tb_single_4:
                    clickSingle(3);
                    break;
                case R.id.tb_single_5:
                    clickSingle(4);
                    break;
                case R.id.tb_single_6:
                    clickSingle(5);
                    break;
                case R.id.tb_single_7:
                    clickSingle(6);
                    break;
                case R.id.tb_single_8:
                    clickSingle(7);
                    break;
                case R.id.tb_single_9:
                    clickSingle(8);
                    break;
            }*/
        }
    };

    private void clickRGB(int rgbLed) {
        for (int i=0;i<9;i++) {
            if (tbSingle[i].isChecked()) {
                mListener.onSelectLed(i,false);
                tbSingle[i].setChecked(false);
            }
        }
        mListener.onSelectRGB(rgbLed, tbRGB[rgbLed].isChecked());
    }

    private void clickSingle(int led) {
        for (int i=0;i<3;i++) {
            if (tbRGB[i].isChecked()) {
                mListener.onSelectRGB(i,false);
                tbRGB[i].setChecked(false);
            }
        }
        mListener.onSelectLed(led, tbSingle[led].isChecked());
    }

    public void setInit() {
        isInit = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isInit) {
            //initToggleBtn();
            isInit = false;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnEasyFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}

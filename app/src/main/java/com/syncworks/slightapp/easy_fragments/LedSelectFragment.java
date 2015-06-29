package com.syncworks.slightapp.easy_fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.syncworks.slightapp.R;
import com.syncworks.slightapp.dialog.DialogChangePattern;
import com.syncworks.slightapp.util.SelectLed;
import com.syncworks.slightapp.widget.LedBtn;

import static com.syncworks.slightapp.util.Define.NUMBER_OF_COLOR_LED;
import static com.syncworks.slightapp.util.Define.NUMBER_OF_SINGLE_LED;
import static com.syncworks.slightapp.util.Define.TYPE_SINGLE_LED;
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link } interface
 * to handle interaction events.
 * Use the {@link LedSelectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LedSelectFragment extends Fragment {

    private LedBtn btnRGB[] = new LedBtn[NUMBER_OF_COLOR_LED];
    private LedBtn btnSingle[] = new LedBtn[NUMBER_OF_SINGLE_LED];

    private OnEasyFragmentListener mListener = null;

    private DialogChangePattern changePatternDialog = null;

    // 새로운 Fragment Instance 생성
    public static LedSelectFragment newInstance() {
        return new LedSelectFragment();
    }

    public LedSelectFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_led_select,container,false);

        btnRGB[0] = (LedBtn) view.findViewById(R.id.rgb_1);
        btnRGB[1] = (LedBtn) view.findViewById(R.id.rgb_2);
        btnRGB[2] = (LedBtn) view.findViewById(R.id.rgb_3);

        btnSingle[0] = (LedBtn) view.findViewById(R.id.led_1);
        btnSingle[1] = (LedBtn) view.findViewById(R.id.led_2);
        btnSingle[2] = (LedBtn) view.findViewById(R.id.led_3);
        btnSingle[3] = (LedBtn) view.findViewById(R.id.led_4);
        btnSingle[4] = (LedBtn) view.findViewById(R.id.led_5);
        btnSingle[5] = (LedBtn) view.findViewById(R.id.led_6);
        btnSingle[6] = (LedBtn) view.findViewById(R.id.led_7);
        btnSingle[7] = (LedBtn) view.findViewById(R.id.led_8);
        btnSingle[8] = (LedBtn) view.findViewById(R.id.led_9);

        btnRGB[0].setOnLedBtnListener(ledBtnListener);
        btnRGB[1].setOnLedBtnListener(ledBtnListener);
        btnRGB[2].setOnLedBtnListener(ledBtnListener);
        btnSingle[0].setOnLedBtnListener(ledBtnListener);
        btnSingle[1].setOnLedBtnListener(ledBtnListener);
        btnSingle[2].setOnLedBtnListener(ledBtnListener);
        btnSingle[3].setOnLedBtnListener(ledBtnListener);
        btnSingle[4].setOnLedBtnListener(ledBtnListener);
        btnSingle[5].setOnLedBtnListener(ledBtnListener);
        btnSingle[6].setOnLedBtnListener(ledBtnListener);
        btnSingle[7].setOnLedBtnListener(ledBtnListener);
        btnSingle[8].setOnLedBtnListener(ledBtnListener);

        showOverLay();
        return view;
    }

    private LedBtn.OnLedBtnListener ledBtnListener = new LedBtn.OnLedBtnListener() {
        @Override
        public void onClick(View buttonView) {

        }

        @Override
        public void onNotify(View buttonView) {
            switch (buttonView.getId()) {
                case R.id.rgb_1:
                    clickRGB(0);
                    break;
                case R.id.rgb_2:
                    clickRGB(1);
                    break;
                case R.id.rgb_3:
                    clickRGB(2);
                    break;
                case R.id.led_1:
                    clickSingle(0);
                    break;
                case R.id.led_2:
                    clickSingle(1);
                    break;
                case R.id.led_3:
                    clickSingle(2);
                    break;
                case R.id.led_4:
                    clickSingle(3);
                    break;
                case R.id.led_5:
                    clickSingle(4);
                    break;
                case R.id.led_6:
                    clickSingle(5);
                    break;
                case R.id.led_7:
                    clickSingle(6);
                    break;
                case R.id.led_8:
                    clickSingle(7);
                    break;
                case R.id.led_9:
                    clickSingle(8);
                    break;
            }
        }
    };

    private void clickRGB(int ledNum) {
        if (btnRGB[ledNum].getBtnBright()) {
            // TODO 대화창 표시
            return;
        }
        // 현재 선택한 RGB 버튼을 체크할 때
        for (int i=0;i<3;i++) {
            if (btnSingle[ledNum*3 + i].getBtnBright()) {
                // TODO 대화창 표시

                return;
            }
        }
        for (int i=0;i<3;i++) {
            btnSingle[ledNum*3 + i].setBtnEnabled(false);
        }
        btnRGB[ledNum].setBtnEnabled(true);
        btnRGB[ledNum].setBtnChecked(true);

    }
    private void clickSingle(int ledNum) {
        if (btnSingle[ledNum].getBtnBright()) {
            // TODO 대화창 표시
            SelectLed selLed = new SelectLed();
            selLed.ledSet(ledNum);
            showChangePatternDialog(TYPE_SINGLE_LED,selLed);
            return;
        }
        if (btnRGB[ledNum/3].getBtnBright()) {
            // TODO 대화창 표시
            return;
        }
        btnRGB[ledNum/3].setBtnEnabled(false);
        for(int i=0;i<3;i++) {
            btnSingle[(ledNum/3)*3 + i].setBtnEnabled(true);
        }
        btnSingle[ledNum].setBtnChecked(true);
    }

    private void showChangePatternDialog(int ledType,SelectLed selectLed) {
        changePatternDialog = new DialogChangePattern(getActivity());
        changePatternDialog.setDescription(ledType, selectLed);
        changePatternDialog.show();
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

    private void showOverLay() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_help_led_select);
        dialog.setCanceledOnTouchOutside(true);
        //for dismissing anywhere you touch
        View masterView = dialog.findViewById(R.id.overlay_help);
        masterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}

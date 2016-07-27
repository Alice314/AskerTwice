package com.wusui.askertwice.ui.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wusui.askertwice.R;

/**
 * Created by fg on 2016/7/27.
 */

public class LoginDialogFragment extends DialogFragment {

    private RadioButton student;
    private RadioButton teacher;
    private RadioGroup mRadioGroup;


    public interface LoginInputListener {
        void onLoginInputComplete(String type);
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_login, null);
        student = (RadioButton) view.findViewById(R.id.student);
        teacher = (RadioButton) view.findViewById(R.id.teacher);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.radio_group_type);

        dialog.setView(view).setNegativeButton("取消",null);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                LoginInputListener listener = (LoginInputListener) getActivity();

                if (checkedId == student.getId()){
                    listener.onLoginInputComplete( "student");
                }
                else {
                    listener.onLoginInputComplete( "teacher");
                }
            }
        });
        return dialog.create();
    }
}

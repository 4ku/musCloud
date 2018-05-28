package com.example.vadim.muscloud.Authentification;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.vadim.muscloud.Entities.User;
import com.example.vadim.muscloud.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpFragment extends Fragment implements OnClickListener {
	private static View view;
	private static EditText email, password, confirmPassword;
	private static TextView login;
	private static Button signUpButton;
	private static CheckBox terms_conditions;

	private SharedPreferencesHelper mSharedPreferencesHelper;
	public static SignUpFragment newInstance() {
		return new SignUpFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.signup_layout, container, false);
		initViews();
		setListeners();
		return view;
	}

	// Initialize all views
	private void initViews() {
		email = (EditText) view.findViewById(R.id.userEmail);
		password = (EditText) view.findViewById(R.id.password);
		confirmPassword = (EditText) view.findViewById(R.id.confirmPassword);
		signUpButton = (Button) view.findViewById(R.id.signUpBtn);
		login = (TextView) view.findViewById(R.id.already_user);
		terms_conditions = (CheckBox) view.findViewById(R.id.terms_conditions);

		// Setting text selector over textviews
		@SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
		try {
			ColorStateList csl = ColorStateList.createFromXml(getResources(),
					xrp);

			login.setTextColor(csl);
			terms_conditions.setTextColor(csl);
		} catch (Exception e) {
		}
	}

	// Set Listeners
	private void setListeners() {
		signUpButton.setOnClickListener(this);
		login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.signUpBtn:

			// Call checkValidation method
			if(!checkValidation())break;
			mSharedPreferencesHelper = new SharedPreferencesHelper(getActivity());
			boolean isAdded = mSharedPreferencesHelper.addUser(new User(
					email.getText().toString(),
					password.getText().toString()
			));

			if (isAdded) {
				showMessage(R.string.login_register_success);
				getFragmentManager().popBackStack();
			} else {
				new CustomToast().Show_Toast(getActivity(), view,
						"Login уже занят!");
			}
			break;

		case R.id.already_user:

			// Replace login fragment
			String tag = LoginFragment.class.getName();
			getFragmentManager()
					.beginTransaction()
					.setCustomAnimations(R.anim.left_enter, R.anim.right_out)
					.replace(R.id.fragmentContainer, LoginFragment.newInstance(),tag)
					.addToBackStack(tag)
					.commit();
			break;
		}

	}

	private boolean checkValidation() {

		String getEmailId = email.getText().toString();
		String getPassword = password.getText().toString();
		String getConfirmPassword = confirmPassword.getText().toString();

		// Pattern match for email id
		Pattern p = Pattern.compile(AuthActivity.regEx);
		Matcher m = p.matcher(getEmailId);

		// Check if all strings are null or not
		if (getEmailId.equals("") || getEmailId.length() == 0
				|| getPassword.equals("") || getPassword.length() == 0
				|| getConfirmPassword.equals("")
				|| getConfirmPassword.length() == 0) {
			new CustomToast().Show_Toast(getActivity(), view,
					"All fields are required.");
			return false;
		}

		// Check if email id valid or not
		else if (!m.find()) {
			new CustomToast().Show_Toast(getActivity(), view,
					"Your Email Id is Invalid.");
			return false;
		}
		// Check if both password should be equal
		else if (!getConfirmPassword.equals(getPassword)) {
			new CustomToast().Show_Toast(getActivity(), view,
					"Both password doesn't match.");
			return  false;
		}
		// Make sure user should check Terms and Conditions checkbox
		else if (!terms_conditions.isChecked()) {
			new CustomToast().Show_Toast(getActivity(), view,
					"Please select Terms and Conditions.");
			return  false;
		}
		return true;

		// Else do signup or do your stuff
//		else
//			Toast.makeText(getActivity(), "Do SignUp.", Toast.LENGTH_SHORT)
//					.show();
	}
	private void showMessage(@StringRes int string) {
		Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
	}
}

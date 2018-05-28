package com.example.vadim.muscloud.Authentification;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.vadim.muscloud.Entities.User;
import com.example.vadim.muscloud.MusicActivity;
import com.example.vadim.muscloud.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginFragment extends Fragment implements OnClickListener {
	private static View view;

	private  EditText email, password;
	private  Button loginButton;
	private  TextView forgotPassword, signUp;
	private  CheckBox show_hide_password;
	private  LinearLayout loginLayout;
	private  Animation shakeAnimation;
	private  FragmentManager fragmentManager;
	private SharedPreferencesHelper mSharedPreferencesHelper;


	public static LoginFragment newInstance() {
		return new LoginFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.login_layout, container, false);
		initViews();
		setListeners();
		return view;
	}

	// Initiate Views
	private void initViews() {
		fragmentManager = getActivity().getSupportFragmentManager();

		mSharedPreferencesHelper = new SharedPreferencesHelper(getActivity());
		email = (EditText) view.findViewById(R.id.login_emailid);
		password = (EditText) view.findViewById(R.id.login_password);
		loginButton = (Button) view.findViewById(R.id.loginBtn);
		forgotPassword = (TextView) view.findViewById(R.id.forgot_password);
		signUp = (TextView) view.findViewById(R.id.createAccount);
		show_hide_password = (CheckBox) view
				.findViewById(R.id.show_hide_password);
		loginLayout = (LinearLayout) view.findViewById(R.id.login_layout);

		// Load ShakeAnimation
		shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.shake);

		// Setting text selector over textviews
		@SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
		try {
			ColorStateList csl = ColorStateList.createFromXml(getResources(),
					xrp);

			forgotPassword.setTextColor(csl);
			show_hide_password.setTextColor(csl);
			signUp.setTextColor(csl);
		} catch (Exception e) {
		}
	}

	// Set Listeners
	private void setListeners() {
		loginButton.setOnClickListener(this);
		forgotPassword.setOnClickListener(this);
		signUp.setOnClickListener(this);

		// Set check listener over checkbox for showing and hiding password
		show_hide_password
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton button,
							boolean isChecked) {

						// If it is checkec then show password else hide
						// password
						if (isChecked) {

							show_hide_password.setText(R.string.hide_pwd);// change
																			// checkbox
																			// text

							password.setInputType(InputType.TYPE_CLASS_TEXT);
							password.setTransformationMethod(HideReturnsTransformationMethod
									.getInstance());// show password
						} else {
							show_hide_password.setText(R.string.show_pwd);// change
																			// checkbox
																			// text

							password.setInputType(InputType.TYPE_CLASS_TEXT
									| InputType.TYPE_TEXT_VARIATION_PASSWORD);
							password.setTransformationMethod(PasswordTransformationMethod
									.getInstance());// hide password

						}

					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loginBtn:
//			if(!checkValidation())break;

//			User user = mSharedPreferencesHelper.login(
//					email.getText().toString(),
//					password.getText().toString());
//			if (user != null) {
				Intent startMusicIntent =
						new Intent(getActivity(), MusicActivity.class);
				startActivity(startMusicIntent);
				getActivity().finish();

//			} else {
//				new CustomToast().Show_Toast(getActivity(), view,
//						"Неверный логин или пароль");
//			}

			break;

		case R.id.forgot_password:

			// Replace forgot password fragment with animation
			String tag = ForgotPasswordFragment.class.getName();

            fragmentManager
					.beginTransaction()
					.setCustomAnimations(R.anim.right_enter, R.anim.left_out,R.anim.left_enter,R.anim.right_out)
					.replace(R.id.fragmentContainer, ForgotPasswordFragment.newInstance(),tag)
					.addToBackStack(tag)
					.commit();
			break;
		case R.id.createAccount:

			// Replace signup frgament with animation
			String tag2=SignUpFragment.class.getName();
			fragmentManager
					.beginTransaction()
					.setCustomAnimations(R.anim.right_enter, R.anim.left_out,R.anim.left_enter,R.anim.right_out)
					.replace(R.id.fragmentContainer, SignUpFragment.newInstance(),tag2)
					.addToBackStack(tag2)
					.commit();
			break;
		}

	}
	private void showMessage(@StringRes int string) {
		Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
	}

	// Check Validation before login
	private boolean checkValidation() {
		// Get email id and password
		String getEmailId = email.getText().toString();
		String getPassword = password.getText().toString();

		// Check patter for email id
		Pattern p = Pattern.compile(AuthActivity.regEx);

		Matcher m = p.matcher(getEmailId);

		// Check for both field is empty or not
		if (getEmailId.equals("") || getEmailId.length() == 0
				|| getPassword.equals("") || getPassword.length() == 0) {
			loginLayout.startAnimation(shakeAnimation);
			new CustomToast().Show_Toast(getActivity(), view,
					"Enter both credentials.");
			return false;
		}
		// Check if email id is valid or not
		else if (!m.find()) {
			new CustomToast().Show_Toast(getActivity(), view,
					"Your Email Id is Invalid.");
			return false;
		}
		return true;
		// Else do login and do your stuff
//		else
//			Toast.makeText(getActivity(), "Do Login.", Toast.LENGTH_SHORT)
//					.show();

	}
}

package com.example.vadim.muscloud.Authentification;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vadim.muscloud.Entities.User;
import com.example.vadim.muscloud.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPasswordFragment extends Fragment implements
        OnClickListener {
	private static View view;

	private static EditText email;
	private static TextView submit, back;

	private SharedPreferencesHelper mSharedPreferencesHelper;

	public static ForgotPasswordFragment newInstance() {
		return new ForgotPasswordFragment();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_forgotpassword, container,
				false);
		initViews();
		setListeners();
		return view;
	}

	// Initialize the views
	private void initViews() {
		email = (EditText) view.findViewById(R.id.registered_emailid);
		submit = (TextView) view.findViewById(R.id.forgot_button);
		back = (TextView) view.findViewById(R.id.backToLoginBtn);

		// Setting text selector over textviews
		@SuppressLint("ResourceType") XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
		try {
			ColorStateList csl = ColorStateList.createFromXml(getResources(),
					xrp);

			back.setTextColor(csl);
			submit.setTextColor(csl);

		} catch (Exception e) {
		}

	}

	// Set Listeners over buttons
	private void setListeners() {
		back.setOnClickListener(this);
		submit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.backToLoginBtn:

			// Replace Login Fragment on Back Presses
			getFragmentManager().popBackStack();
			break;

		case R.id.forgot_button:

			// Call Submit button task
			if(!submitButtonTask()) break;
			mSharedPreferencesHelper = new SharedPreferencesHelper(getActivity());
			List<User> users = mSharedPreferencesHelper.getUsers();
			for (User u : users) {
				if (email.getText().toString().equalsIgnoreCase(u.getLogin())) {
					Toast.makeText(getActivity(), "Your password:" + u.getPassword(),
							Toast.LENGTH_SHORT).show();
					return;
				}
			}
			new CustomToast().Show_Toast(getActivity(), view,
					"Your Email is not found");
			break;

		}

	}

	private boolean submitButtonTask() {
		String getEmailId = email.getText().toString();

		// Pattern for email id validation
		Pattern p = Pattern.compile(AuthActivity.regEx);

		// Match the pattern
		Matcher m = p.matcher(getEmailId);

		// First check if email id is not null else show error toast
		if (getEmailId.equals("") || getEmailId.length() == 0) {
			new CustomToast().Show_Toast(getActivity(), view,
					"Please enter your Email Id.");
			return false;
		}
		// Check if email id is valid or not
		else if (!m.find()) {
			new CustomToast().Show_Toast(getActivity(), view,
					"Your Email Id is Invalid.");
			return  false;
		}
		// Else submit email id and fetch passwod or do your stuff
		else {
			return  true;
		}
	}
}
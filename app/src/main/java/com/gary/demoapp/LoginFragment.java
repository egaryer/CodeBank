package com.gary.demoapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;


public class LoginFragment extends Fragment {
    String strFacebookEmail = "";
    String strFacebookGender = "";
    String strFacebookLocation = "";

    SharedPreferences fbSavedData;

    TextView fbProfileName;
    TextView fbEmail;
    TextView fbGender;
    TextView fbLocation;
    ProfilePictureView fbProfilePicture;
    LoginButton loginButton;

    CallbackManager callbackManager;
    AccessTokenTracker tokenTracker;
    ProfileTracker profileTracker;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static LoginFragment newInstance(int sectionNumber) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                AccessToken.setCurrentAccessToken(newToken);
            }
        };
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                updateLoginUI(newProfile);
            }
        };
        tokenTracker.startTracking();
        profileTracker.startTracking();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        fbSavedData = getActivity().getPreferences(Context.MODE_PRIVATE);
        strFacebookEmail = fbSavedData.getString("fbEmail", "");
        strFacebookGender = fbSavedData.getString("fbGender", "");
        strFacebookLocation = fbSavedData.getString("fbLocation", "");

        fbEmail = (TextView) rootView.findViewById(R.id.tv_fbEmail);
        fbGender = (TextView) rootView.findViewById(R.id.tv_fbGender);
        fbLocation = (TextView) rootView.findViewById(R.id.tv_fbLocation);
        fbProfileName = (TextView) rootView.findViewById(R.id.tv_fbName);
        fbProfilePicture = (ProfilePictureView) rootView.findViewById(R.id.selection_profile_pic);
        fbProfilePicture.setCropped(true);

        loginButton = (LoginButton) rootView.findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_location"));
        // If using in a fragment
        loginButton.setFragment(this);

        // Callback registration
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                try {
                                    strFacebookEmail = object.getString("email");
                                    strFacebookGender = object.getString("gender");
                                    strFacebookLocation = object.getJSONObject("location").getString("name");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Profile profile = Profile.getCurrentProfile();
                                updateLoginUI(profile);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "email,gender,location");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
                fbProfileName.setText("Cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                fbProfileName.setText("Error");
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        Profile profile = Profile.getCurrentProfile();
        updateLoginUI(profile);

    }

    private void updateLoginUI(Profile profile) {
        if (profile != null) {
            fbProfileName.setText("Welcome " + profile.getName());
            fbProfilePicture.setProfileId(profile.getId());
            fbEmail.setText("Email: " + strFacebookEmail);
            fbGender.setText("Gender: " + strFacebookGender);
            fbLocation.setText("Location: " + strFacebookLocation);
        }
        if (AccessToken.getCurrentAccessToken() == null) {
            fbProfileName.setText("Please login");
            fbProfilePicture.setProfileId(null);
            fbEmail.setText("");
            fbEmail.setText("");
            fbGender.setText("");
            fbLocation.setText("");
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        fbSavedData.edit()
                .putString("fbEmail", strFacebookEmail)
                .putString("fbGender", strFacebookGender)
                .putString("fbLocation", strFacebookLocation)
                .commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        tokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}
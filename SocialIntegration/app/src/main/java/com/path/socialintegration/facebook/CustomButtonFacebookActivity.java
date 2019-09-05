package com.path.socialintegration.facebook;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.path.socialintegration.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class CustomButtonFacebookActivity extends AppCompatActivity {

    private static final String TAG = "CustomButtonFacebookActivity";
    CallbackManager callbackManager;
    TextView txt_username, txt_email;
    Button btn_login, btn_logout;
    String userId;
    ImageView img_profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        img_profile = findViewById(R.id.img_profile);
        txt_username = findViewById(R.id.txt_username);
        txt_email = findViewById(R.id.txt_email);

        btn_login = findViewById(R.id.btn_login);
        btn_logout = findViewById(R.id.btn_logout);

        callbackManager = CallbackManager.Factory.create();

        if (AccessToken.getCurrentAccessToken() == null) {
            btn_login.setVisibility(View.VISIBLE);
            btn_logout.setVisibility(View.GONE);
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbLogin();
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private void logout() {
        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
            img_profile.setImageBitmap(null);
            txt_username.setText("");
            txt_email.setText("");
            btn_login.setVisibility(View.VISIBLE);
            btn_logout.setVisibility(View.GONE);
        }
    }

    public void fbLogin() {        // Before Edit:
        // LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("user_photos", "email", "public_profile", "user_posts" , "AccessToken"));
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        getUserDetails(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.e("Exception", exception + "");
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    protected void getUserDetails(LoginResult loginResult) {
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject json_object,
                            GraphResponse response) {
                        Log.e("Data", json_object.toString());
                        try {
                            Log.e("ID", json_object.getString("id"));
                            userId = json_object.getString("id");
                            txt_username.setText(json_object.getString("name"));
                            txt_email.setText(json_object.getString("email"));
                            Picasso.with(CustomButtonFacebookActivity.this)
                                    .load("https://graph.facebook.com/" + userId + "/picture?type=large")
                                    .into(img_profile);
                            btn_login.setVisibility(View.GONE);
                            btn_logout.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();

    }

    public void printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    public Bitmap getFacebookProfilePicture(String userID) {
        URL imageURL = null;
        Bitmap bitmap = null;
        try {
            imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
            bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}

package com.example.dvcbaberbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.dvcbaberbooking.Common.Common;
import com.example.dvcbaberbooking.Fragments.HomeFragment;
import com.example.dvcbaberbooking.Fragments.ShoppingFragment;
import com.example.dvcbaberbooking.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {


    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    BottomSheetDialog bottomSheetDialog;

    CollectionReference userRef;
    boolean doubleBackToExitPressedOnce = false;
    AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Home");

        ButterKnife.bind(HomeActivity.this);
        //init
        userRef = FirebaseFirestore.getInstance().collection("User");
        dialog = new SpotsDialog.Builder().setContext(this).build();

        //check intent , if is login = true , enable full access - se login for veerdadeiro entao permitir acesso completo
        // if login = false , just let user around shopping view - se login for falso permitir usuario olhar itens
        if (getIntent() != null) {
            boolean islogin = getIntent().getBooleanExtra(Common.IS_LOGIN, false);

            if (islogin) {
                dialog.show();

                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                //check if user
                //save userPhone by Paper
                Paper.init(HomeActivity.this);
                Paper.book().write(Common.LOGGED_KEY, user.getPhoneNumber());
                DocumentReference currentUser = userRef.document(user.getPhoneNumber());
                currentUser.get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot userSnapShot = task.getResult();
                                    if (!userSnapShot.exists()) {
                                        showUpdateDialog(user.getPhoneNumber());
                                    } else {
                                        Common.currentUser = userSnapShot.toObject(User.class);
                                        showUpdateDialog(user.getPhoneNumber());
                                        bottomNavigationView.setSelectedItemId(R.id.action_home);
                                    }
                                    if (dialog.isShowing())
                                        dialog.dismiss();

//                                 checkRatingDialog();
                                }
                            }
                        });
            }


        }

        //view
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Fragment fragment = null;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_home)
                    fragment = new HomeFragment();
                else if (menuItem.getItemId() == R.id.action_shopping)
                    fragment = new ShoppingFragment();

                return loadFragment(fragment);
            }
        });


    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    private void showUpdateDialog(final String phoneNumber) {
        if (dialog.isShowing())
            dialog.show();

        //inicia Dialog
        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setTitle("One step!");
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        bottomSheetDialog.setCancelable(false);
        View sheetView = getLayoutInflater().inflate(R.layout.layout_update_information, null);

        Button btn_update = (Button) sheetView.findViewById(R.id.btn_update);
        final TextInputEditText edt_name = (TextInputEditText) sheetView.findViewById(R.id.edt_name);
        final TextInputEditText edt_address = (TextInputEditText) sheetView.findViewById(R.id.edt_address);

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!dialog.isShowing())
                    dialog.show();
                String name = edt_name.getText().toString();
                String add = edt_address.getText().toString();
                final User user = new User(name,
                        add,
                        phoneNumber);
                if (name.equals("") || add.equals("")) {
                    Toast.makeText(HomeActivity.this, "Vui lòng nhập đầy đủ thông tin !", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    userRef.document(phoneNumber)
                            .set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    bottomSheetDialog.dismiss();
                                    if (dialog.isShowing())
                                        dialog.dismiss();
                                    bottomNavigationView.setSelectedItemId(R.id.action_home);
                                    dialog.dismiss();
                                    Toast.makeText(HomeActivity.this, "Thanks You!", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.isShowing();
                            dialog.dismiss();
                            bottomSheetDialog.dismiss();
                            Toast.makeText(HomeActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });


        bottomSheetDialog.setContentView(sheetView);
        bottomSheetDialog.show();
//        bottomSheetDialog.dismiss();
    }
}
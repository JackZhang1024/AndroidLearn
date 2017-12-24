package com.lucky.androidlearn.provider;

import android.Manifest;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;

import com.lucky.androidlearn.R;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by zfz on 2017/12/31.
 */

public class SimpleProviderActivity extends ListActivity {
    private static final String TAG = "SimpleProviderActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestContactsPermission(this);
    }

    private void requestContactsPermission(final Context context) {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            setListAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, loadContacts()));
                        }
                    }
                });

    }

    private List<String> loadContacts() {
        List<String> contacts = new ArrayList<>();
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        while (cursor != null && cursor.moveToNext()) {
            String userName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Log.e(TAG, "loadContacts: userName " + userName + " phoneNumber " + phoneNumber);
            contacts.add(String.format("姓名 %s %s", userName, phoneNumber));
        }
        if (cursor != null) {
            cursor.close();
        }
        return contacts;
    }

}

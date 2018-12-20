package ru.vetatto.investing.investing.SMS;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

import ru.vetatto.investing.investing.PifList.PifAdapter;
import ru.vetatto.investing.investing.PifList.PifData;
import ru.vetatto.investing.investing.R;

public class SMSRead extends Activity {
    ArrayList<SMSData> phones = new ArrayList();
    RecyclerView recyclerView;
    SMSAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_read);
        recyclerView = findViewById(R.id.sms_recycles);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        adapter = new SMSAdapter(this, phones);
        recyclerView.setAdapter(adapter);

        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            Log.d("SMS_TEST","Разрешение есть");
            Uri uriSMSURI = Uri.parse("content://sms/inbox");
            Cursor cur = getContentResolver().query(uriSMSURI, null, null, null,null);
            String sms = "";
            while (cur.moveToNext()) {
                sms += "From :" + cur.getString(2) + " : " + cur.getString(11)+"\n";
                Log.d("SMS_TEST",sms);
                phones.add(new SMSData(cur.getString(2), cur.getString(cur.getColumnIndexOrThrow("body"))));
            }
            adapter.notifyDataSetChanged();

        } else {
            Log.d("SMS_TEST","Запрашиваем разрешение");
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_SMS},
                    1);
        }

    }
}
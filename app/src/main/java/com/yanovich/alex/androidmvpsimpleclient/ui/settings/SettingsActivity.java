package com.yanovich.alex.androidmvpsimpleclient.ui.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.yanovich.alex.androidmvpsimpleclient.R;
import com.yanovich.alex.androidmvpsimpleclient.data.local.DbOpenHelper;
import com.yanovich.alex.androidmvpsimpleclient.ui.base.BaseActivity;
import com.yanovich.alex.androidmvpsimpleclient.ui.base.BasePresenter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by Alex on 01.05.2016.
 */
public class SettingsActivity extends BaseActivity implements SettingsMvpView{

    private File mExternalDbBuckupFile;
    @Inject
    SettingsPresenter mSettingsPresenter;
    @Inject
    DbOpenHelper mDbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityComponent().inject(this);
        setContentView(R.layout.activity_settings);
        mSettingsPresenter.attachView(this);
        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(R.id.content, new SettingsFragment())
                .commit();
        //getFragmentManager().popBackStack();

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setTitle("Settings");
        }
    }


    public void onEmailSend() {
       mSettingsPresenter.getEmail();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSettingsPresenter.detachView();
    }

    private boolean copyFileToExternal() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File db = getDatabasePath(mDbOpenHelper.getDatabaseName());

            if (sd.canWrite()) {

                String backupDBPath = "smvpclient.db";
               File backupDb = new File(sd, backupDBPath);

                if (db.exists()) {
                    FileChannel src = new FileInputStream(db).getChannel();
                    FileChannel dst = new FileOutputStream(backupDb).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
                mExternalDbBuckupFile = backupDb;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    private void sendEmail(String email, File dbExtra) {
        Timber.i("Start sending email:");
        Uri path = Uri.fromFile(dbExtra);
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("application/octet-stream");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Database");
        String to[] = { email };
        intent.putExtra(Intent.EXTRA_EMAIL, to);
        intent.putExtra(Intent.EXTRA_TEXT, "Some message");
        intent.putExtra(Intent.EXTRA_STREAM, path);
        if(intent.resolveActivity(getPackageManager())!=null) {
            startActivityForResult(Intent.createChooser(intent, "Send mail..."), 101);
        }else {
            Toast.makeText(this, "There are no applications that support sending action", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //check if the code belong to the send message activity
        if (requestCode == 101) {
          boolean deleted =  mExternalDbBuckupFile.delete();
            Timber.i("on request code = 101,datafile isdeleted=" +deleted);
        }
    }

    @Override
    public void sendEmail(String email) {
        boolean isDbCopied = copyFileToExternal();
         Timber.i("copy to extrena:" + isDbCopied);

        if(isDbCopied){
                  sendEmail(email, mExternalDbBuckupFile);

        }else {
            Toast.makeText(this, "Can not copy database to sdcard for perfoming sending operation!", Toast.LENGTH_LONG).show();
        }

    }
}

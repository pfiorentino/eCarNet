package me.alpha12.ecarnet.activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import me.alpha12.ecarnet.GlobalContext;
import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.models.NFCTag;

public class WriteTagActivity extends AppCompatActivity implements View.OnClickListener {
    private boolean writeMode;
    private boolean tagWritten;
    private NfcAdapter nfcAdapter;
    private PendingIntent nfcPendingIntent;

    private NFCTag tag;

    private TextView tagNameTextView;
    private TextView tagTypeTextView;
    private ImageView tagTypeImageView;
    private Button confirmButton;
    private LinearLayout writeTagLayout;
    private TextView writeTagDescTextView;
    private ImageView writeTagDescIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_tag);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tag = (NFCTag) getIntent().getExtras().get("nfcTag");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1)
            nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        confirmButton    = (Button) findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(this);
        tagNameTextView  = (TextView) findViewById(R.id.tagNameTextView);
        tagTypeImageView = (ImageView) findViewById(R.id.tagTypeIcon);
        tagTypeTextView  = (TextView) findViewById(R.id.tagTypeTextView);

        tagNameTextView.setText(tag.getName());
        tagTypeTextView.setText(tag.getMimeTypeDesc());
        if (tag.getMimeTypeIcon() != null)
            tagTypeImageView.setImageResource(tag.getMimeTypeIcon());
        else
            tagTypeImageView.setImageResource(R.drawable.ic_nfc_tblack_24dp);

        writeTagLayout = (LinearLayout) findViewById(R.id.writeTagLayout);
        writeTagLayout.setOnClickListener(this);
        writeTagDescTextView = (TextView) findViewById(R.id.writeTagDescTextView);
        writeTagDescIcon = (ImageView) findViewById(R.id.writeTagDescIcon);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirmButton:
                if (writeMode) {
                    disableTagWriteMode();
                } else {
                    tag.persist();
                    setResult(GlobalContext.RESULT_CLOSE_ALL);
                    finish();
                }
                break;
            case R.id.writeTagLayout:
                if (!tagWritten && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
                    nfcPendingIntent = PendingIntent.getActivity(this, 0,
                            new Intent(this, WriteTagActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

                    enableTagWriteMode();
                }
                break;
        }
    }

    public void onResume() {
        super.onResume();
        disableTagWriteMode();
    }

    private void startBlinking() {
        writeTagDescTextView.setText(R.string.writing_tag_description);

        Animation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(1000);
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        writeTagLayout.startAnimation(anim);
    }

    private void stopBlinking() {
        writeTagLayout.clearAnimation();

        if (tagWritten) {
            writeTagDescIcon.setImageResource(R.drawable.ic_check_black_38pc_96dp);
            writeTagDescTextView.setText(R.string.written_tag_description);
            writeTagLayout.setOnClickListener(null);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1 && nfcAdapter != null && nfcAdapter.isEnabled()) {
                writeTagDescIcon.setImageResource(R.drawable.ic_nfc_black_38pc_96dp);
                writeTagDescTextView.setText(R.string.write_tag_description);
                writeTagLayout.setOnClickListener(this);
            } else {
                writeTagDescIcon.setImageResource(R.drawable.ic_nonfc_black_38pc_96dp);
                writeTagDescTextView.setText(R.string.no_nfc_description);
                writeTagLayout.setOnClickListener(null);
            }
        }
    }

    private void enableTagWriteMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
            writeMode = true;

            startBlinking();
            confirmButton.setText("Annuler");

            IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
            IntentFilter[] writeTagFilters = new IntentFilter[] { tagDetected };
            nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent, writeTagFilters, null);
        }
    }

    private void disableTagWriteMode() {
        writeMode = false;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
            nfcAdapter.disableForegroundDispatch(this);
        }

        stopBlinking();
        confirmButton.setText("Terminer");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (writeMode &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
                NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NdefRecord record = NdefRecord.createMime(this.tag.getMimeType(), this.tag.getMessage().getBytes());
            NdefMessage message = new NdefMessage(new NdefRecord[] { record });
            if (writeTag(message, detectedTag)) {
                tagWritten = true;
            }
        }
    }

    /*
    * Writes an NdefMessage to a NFC tag
    */
    public boolean writeTag(NdefMessage message, Tag tag) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
            int size = message.toByteArray().length;
            try {
                Ndef ndef = Ndef.get(tag);
                if (ndef != null) {
                    ndef.connect();
                    if (!ndef.isWritable()) {
                        Toast.makeText(getApplicationContext(),
                                "Error: tag not writable",
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    if (ndef.getMaxSize() < size) {
                        Toast.makeText(getApplicationContext(),
                                "Error: tag too small",
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    ndef.writeNdefMessage(message);
                    return true;
                } else {
                    NdefFormatable format = NdefFormatable.get(tag);
                    if (format != null) {
                        try {
                            format.connect();
                            format.format(message);
                            return true;
                        } catch (IOException e) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            } catch (Exception e) {
                return false;
            }
        } else {
            Log.e("eCarNet error", "NFC isn't supported on your device");
            return false;
        }
    }
}

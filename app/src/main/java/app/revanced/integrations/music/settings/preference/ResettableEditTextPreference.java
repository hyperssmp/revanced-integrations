package app.revanced.integrations.music.settings.preference;

import static app.revanced.integrations.music.utils.ExtendedUtils.getDialogBuilder;
import static app.revanced.integrations.music.utils.ExtendedUtils.getLayoutParams;
import static app.revanced.integrations.shared.utils.StringRef.str;

import android.app.Activity;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;

import app.revanced.integrations.shared.settings.Setting;
import app.revanced.integrations.shared.utils.Logger;

public class ResettableEditTextPreference {

    public static void showDialog(Activity mActivity, @NonNull Setting<String> setting) {
        showDialog(mActivity, setting, setting.get());
    }

    public static void showDialog(Activity mActivity, @NonNull Setting<String> setting, String hint) {
        try {
            final EditText textView = new EditText(mActivity);
            textView.setHint(hint);
            textView.setText(setting.get());

            TextInputLayout textInputLayout = new TextInputLayout(mActivity);
            textInputLayout.setLayoutParams(getLayoutParams());
            textInputLayout.addView(textView);

            FrameLayout container = new FrameLayout(mActivity);
            container.addView(textInputLayout);

            getDialogBuilder(mActivity)
                    .setTitle(str(setting.key + "_title"))
                    .setView(container)
                    .setNegativeButton(android.R.string.cancel, null)
                    .setNeutralButton(str("revanced_extended_settings_reset"), (dialog, which) -> {
                        setting.resetToDefault();
                        ReVancedPreferenceFragment.showRebootDialog();
                    })
                    .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                        setting.save(textView.getText().toString().trim());
                        ReVancedPreferenceFragment.showRebootDialog();
                    })
                    .show();
        } catch (Exception ex) {
            Logger.printException(() -> "showDialog failure", ex);
        }
    }

}

package com.ai.cmcchina.crm.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.text.SpannableStringBuilder;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.internal.view.SupportMenu;

import com.heclient.heapp.R;


/* renamed from: com.ai.cmcchina.crm.view.CustomerValidateNumMsgDialog */
public class CustomerValidateNumMsgDialog extends Dialog {
    public CustomerValidateNumMsgDialog(Context context) {
        super(context);
    }

    public CustomerValidateNumMsgDialog(Context context, int theme) {
        super(context, theme);
    }

    /* renamed from: com.ai.cmcchina.crm.view.CustomerValidateNumMsgDialog$Builder */
    public static class Builder {
        private View contentView;
        private Context context;
        private View layout;
        private String message;
        /* access modifiers changed from: private */
        public OnClickListener negativeButtonClickListener;
        private String negativeButtonText;
        /* access modifiers changed from: private */
        public OnClickListener positiveButtonClickListener;
        private String positiveButtonText;
        private int resID;
        private String title;

        public Builder(Context context2) {
            this.context = context2;
        }

        public Builder(Context context2, int resID2) {
            this.context = context2;
            this.resID = resID2;
        }

        public Builder setMessage(String message2) {
            this.message = message2;
            return this;
        }

        public Builder setMessage(int message2) {
            this.message = (String) this.context.getText(message2);
            return this;
        }

        public Builder setTitle(int title2) {
            this.title = (String) this.context.getText(title2);
            return this;
        }

        public Builder setTitle(String title2) {
            this.title = title2;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonText2, OnClickListener listener) {
            this.positiveButtonText = (String) this.context.getText(positiveButtonText2);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText2, OnClickListener listener) {
            this.positiveButtonText = positiveButtonText2;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText2, OnClickListener listener) {
            this.negativeButtonText = (String) this.context.getText(negativeButtonText2);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText2, OnClickListener listener) {
            this.negativeButtonText = negativeButtonText2;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public String getEditMessage(int editViewID) {
            return ((EditText) this.layout.findViewById(editViewID)).getText().toString();
        }

        public void setEditMessage(int editViewID, String msg) {
            ((EditText) this.layout.findViewById(editViewID)).setText(msg);
        }

        public CustomerValidateNumMsgDialog create() {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CustomerValidateNumMsgDialog dialog = new CustomerValidateNumMsgDialog(this.context, R.style.Dialog);
            if (this.resID == 0) {
                this.layout = inflater.inflate(R.layout.dialog_customer_msg, (ViewGroup) null);
            } else {
                this.layout = inflater.inflate(this.resID, (ViewGroup) null);
            }
            dialog.addContentView(this.layout, new ViewGroup.LayoutParams(-1, -2));
            ((TextView) this.layout.findViewById(R.id.title)).setText(this.title);
            if (this.positiveButtonText != null) {
                ((Button) this.layout.findViewById(R.id.positiveButton)).setText(this.positiveButtonText);
                if (this.positiveButtonClickListener != null) {
                    ((Button) this.layout.findViewById(R.id.positiveButton)).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Builder.this.positiveButtonClickListener.onClick(dialog, -1);
                        }
                    });
                }
            } else {
                this.layout.findViewById(R.id.positiveButton).setVisibility(8);
            }
            if (this.negativeButtonText != null) {
                ((Button) this.layout.findViewById(R.id.negativeButton)).setText(this.negativeButtonText);
                if (this.negativeButtonClickListener != null) {
                    ((Button) this.layout.findViewById(R.id.negativeButton)).setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            Builder.this.negativeButtonClickListener.onClick(dialog, -2);
                        }
                    });
                }
            } else {
                this.layout.findViewById(R.id.negativeButton).setVisibility(View.GONE);
            }
            if (this.message != null) {
                if ("密码修改失败!\n（错误编码）".equals(this.message)) {
                    ColorStateList redColors = ColorStateList.valueOf(SupportMenu.CATEGORY_MASK);
                    SpannableStringBuilder spanBuilder = new SpannableStringBuilder("密码修改失败!\n（错误编码）");
                    spanBuilder.setSpan(new TextAppearanceSpan((String) null, 0, 0, redColors, (ColorStateList) null), 4, 13, 34);
                    ((TextView) this.layout.findViewById(R.id.message)).setText(spanBuilder);
                } else {
                    ((TextView) this.layout.findViewById(R.id.message)).setText(this.message);
                }
            } else if (this.contentView != null) {
                ((LinearLayout) this.layout.findViewById(R.id.content)).removeAllViews();
                ((LinearLayout) this.layout.findViewById(R.id.content)).addView(this.contentView, new ViewGroup.LayoutParams(-1, -1));
            }
            dialog.setContentView(this.layout);
            return dialog;
        }
    }
}
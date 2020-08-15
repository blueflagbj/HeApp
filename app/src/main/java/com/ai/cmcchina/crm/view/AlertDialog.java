package com.ai.cmcchina.crm.view;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.heclient.heapp.R;


/* renamed from: com.ai.cmcchina.crm.view.AlertDialog */
public class AlertDialog extends Dialog implements DialogInterface {
    public AlertDialog(Context context) {
        super(context);
    }

    public AlertDialog(Context context, int theme) {
        super(context, theme);
    }

    /* renamed from: com.ai.cmcchina.crm.view.AlertDialog$Builder */
    public static class Builder {
        /* access modifiers changed from: private */
        public DialogContentEvents contentEvents;
        private boolean mCancelable;
        private View mContentView;
        private Context mContext;
        private CharSequence[] mItems;
        /* access modifiers changed from: private */
        public OnClickListener mListItemClickListener;
        private String mMessage;
        private String mNegativeButtonText;
        private String mNeutralButtonText;
        private String mPositiveButtonText;
        private String mTitle;
        /* access modifiers changed from: private */
        public OnClickListener negativeButtonClickListener;
        /* access modifiers changed from: private */
        public OnClickListener neutralButtonClickListener;
        /* access modifiers changed from: private */
        public OnClickListener positiveButtonClickListener;

        public void setSingleChoiceItems(String[] feedList, int i, OnClickListener onClickListener) {
        }


        /* renamed from: com.ai.cmcchina.crm.view.AlertDialog$Builder$DialogContentEvents */
        public interface DialogContentEvents {
            void onCancel(AlertDialog alertDialog);

            void onLoad();

            boolean onSubmit(AlertDialog alertDialog);
        }

        public Builder setCancelable(boolean cancelable) {
            this.mCancelable = cancelable;
            return this;
        }

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder setMessage(String message) {
            this.mMessage = message;
            return this;
        }

        public Builder setMessage(int message) {
            this.mMessage = (String) this.mContext.getText(message);
            return this;
        }

        public Builder setTitle(int title) {
            this.mTitle = (String) this.mContext.getText(title);
            return this;
        }

        public Builder setTitle(String title) {
            this.mTitle = title;
            return this;
        }

        public Builder setItems(CharSequence[] items, OnClickListener listener) {
            this.mItems = items;
            this.mListItemClickListener = listener;
            return this;
        }

        public Builder setContentView(View v) {
            this.mContentView = v;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonText, OnClickListener listener) {
            this.mPositiveButtonText = (String) this.mContext.getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText, OnClickListener listener) {
            this.mPositiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNeutralButton(int neutralButtonText, OnClickListener listener) {
            this.mNeutralButtonText = (String) this.mContext.getText(neutralButtonText);
            this.neutralButtonClickListener = listener;
            return this;
        }

        public Builder setNeutralButton(String neutralButtonText, OnClickListener listener) {
            this.mNeutralButtonText = neutralButtonText;
            this.neutralButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText, OnClickListener listener) {
            this.mNegativeButtonText = (String) this.mContext.getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText, OnClickListener listener) {
            this.mNegativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public AlertDialog create() {
            final AlertDialog dialog = new AlertDialog(this.mContext, R.style.alert);
            dialog.setCancelable(this.mCancelable);
            dialog.setOnCancelListener((OnCancelListener) null);
            View layout = ((LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.alert, (ViewGroup) null);
            dialog.setContentView(layout, new ViewGroup.LayoutParams(-1, -2));
            ((TextView) layout.findViewById(R.id.title)).setText(this.mTitle);
            if (this.mPositiveButtonText != null) {
                layout.findViewById(R.id.banner).setVisibility(View.VISIBLE);
                ((Button) layout.findViewById(R.id.positive)).setText(this.mPositiveButtonText);
                ((Button) layout.findViewById(R.id.positive)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (Builder.this.positiveButtonClickListener != null) {
                            Builder.this.positiveButtonClickListener.onClick(dialog, -1);
                        }
                        if (Builder.this.contentEvents == null) {
                            dialog.dismiss();
                        } else if (Builder.this.contentEvents.onSubmit(dialog)) {
                            dialog.dismiss();
                        }
                    }
                });
            } else {
                layout.findViewById(R.id.positive).setVisibility(View.GONE);
            }
            if (this.mNeutralButtonText != null) {
                layout.findViewById(R.id.banner).setVisibility(View.VISIBLE);
                ((Button) layout.findViewById(R.id.neutral)).setText(this.mNeutralButtonText);
                ((Button) layout.findViewById(R.id.neutral)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (Builder.this.neutralButtonClickListener != null) {
                            Builder.this.neutralButtonClickListener.onClick(dialog, -3);
                        }
                        dialog.dismiss();
                    }
                });
            } else {
                layout.findViewById(R.id.neutral).setVisibility(View.GONE);
            }
            if (this.mNegativeButtonText != null) {
                layout.findViewById(R.id.banner).setVisibility(View.VISIBLE);
                ((Button) layout.findViewById(R.id.negative)).setText(this.mNegativeButtonText);
                ((Button) layout.findViewById(R.id.negative)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (Builder.this.negativeButtonClickListener != null) {
                            Builder.this.negativeButtonClickListener.onClick(dialog, -2);
                        }
                        if (Builder.this.contentEvents != null) {
                            Builder.this.contentEvents.onCancel(dialog);
                        }
                        dialog.dismiss();
                    }
                });
            } else {
                layout.findViewById(R.id.negative).setVisibility(View.GONE);
            }
            if (this.mMessage != null) {
                ((TextView) layout.findViewById(R.id.message)).setText(this.mMessage);
            } else if (this.mItems != null) {
                createListView(dialog, layout);
            } else if (this.mContentView != null) {
                ((LinearLayout) layout.findViewById(R.id.content)).removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content)).addView(this.mContentView, new ViewGroup.LayoutParams(-1, -2));
                if (this.contentEvents != null) {
                    this.contentEvents.onLoad();
                }
            }
            dialog.setContentView(layout);
            return dialog;
        }

        @SuppressLint("ResourceType")
        private void createListView(final AlertDialog dialog, View layout) {
            ((ImageView) layout.findViewById(R.id.icon)).setImageResource(17301573);
            ListView listView = (ListView) LayoutInflater.from(this.mContext).inflate(R.layout.list_simple, (ViewGroup) null);
            ((LinearLayout) layout.findViewById(R.id.content)).removeAllViews();
            ((LinearLayout) layout.findViewById(R.id.content)).addView(listView, new ViewGroup.LayoutParams(-1, -2));
            listView.setAdapter(new ArrayAdapter(this.mContext, R.layout.text_simple, R.id.text, this.mItems));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    if (Builder.this.mListItemClickListener != null) {
                        Builder.this.mListItemClickListener.onClick(dialog, position);
                    }
                }
            });
        }

        public void setContentListener(DialogContentEvents contentEvents2) {
            this.contentEvents = contentEvents2;
        }
    }
}
package com.lucky.androidlearn.core.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.lucky.androidlearn.R;


public class DownloadApkDialog extends Dialog {


	public DownloadApkDialog(Context context) {
		super(context);
	}

	public DownloadApkDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private String title;
		private String message;
		private String positiveButtonText;
		private String negativeButtonText;
		private View contentView;
		private  ProgressBar progressBar;
		private OnClickListener positiveButtonClickListener;
		private OnClickListener negativeButtonClickListener;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		/**
		 * Set the Dialog message from resource
		 * 
		 * @param
		 * @return
		 */
		public Builder setMessage(int message) {
			this.message = (String) context.getText(message);
			return this;
		}

		/**
		 * Set the Dialog title from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		/**
		 * Set the Dialog title from String
		 * 
		 * @param title
		 * @return
		 */

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param positiveButtonText
		 * @return
		 */
		public Builder setPositiveButton(int positiveButtonText,
				OnClickListener listener) {
			this.positiveButtonText = (String) context.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText,
				OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(int negativeButtonText,
				OnClickListener listener) {
			this.negativeButtonText = (String) context
					.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText,
				OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}

		public DownloadApkDialog create() {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final DownloadApkDialog dialog = new DownloadApkDialog(context, R.style.Dialog);
			View layout = inflater.inflate(R.layout.layout_downloaddialog, null);
			progressBar=(ProgressBar) layout.findViewById(R.id.progressbar);
			dialog.addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			((TextView) layout.findViewById(R.id.tv_title)).setText(title);
			if (positiveButtonText != null) {
				((TextView) layout.findViewById(R.id.tv_sure)).setText(positiveButtonText);
				if (positiveButtonClickListener != null) {
					((TextView) layout.findViewById(R.id.tv_sure))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									positiveButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			} else {
				layout.findViewById(R.id.tv_sure).setVisibility(
						View.GONE);
			}
			if (negativeButtonText != null) {
				((TextView) layout.findViewById(R.id.tv_cancel))
						.setText(negativeButtonText);
				if (negativeButtonClickListener != null) {
					((TextView) layout.findViewById(R.id.tv_cancel))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									negativeButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_NEGATIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.tv_cancel).setVisibility(
						View.GONE);
			}
			// set the content message
			if (message != null) {
				((TextView) layout.findViewById(R.id.tv_content)).setText(message);
			} else if (contentView != null) {
				((LinearLayout) layout.findViewById(R.id.ll_content))
						.removeAllViews();
				((LinearLayout) layout.findViewById(R.id.ll_content))
						.addView(contentView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
			}
			dialog.setContentView(layout);
			return dialog;
		}
		
		public void setProgressMax(int max){
	        progressBar.setMax(max);	    	
		}
		
		public void setProgress(int progress){
			progressBar.setProgress(progress);
		}
	}
	



}


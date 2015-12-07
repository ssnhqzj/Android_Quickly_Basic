package gzt.com.apptest.Chat.face;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.Spannable;
import android.util.AttributeSet;
import android.widget.EditText;


/**
 * 自定义的textview，用来处理复制粘贴的消息
 * 
 */
public class PasteEditText extends EditText {
	private Context context;

	public PasteEditText(Context context) {
		super(context);
		this.context = context;
	}

	public PasteEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	public PasteEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onTextContextMenuItem(int id) {
		if (id == android.R.id.paste) {
			ClipboardManager clip = (ClipboardManager) getContext()
					.getSystemService(Context.CLIPBOARD_SERVICE);
			if (clip == null || clip.getText() == null) {
				return false;
			}
			String text = clip.getText().toString();
			Editable currentText = getText();
			int selectionLength = getSelectionEnd() - getSelectionStart();
			Spannable span;
			CharSequence selectStartText = currentText.subSequence(0, Math.max(getSelectionStart(), 0));
			if (selectionLength==0){
				CharSequence selectEndText = currentText.subSequence(Math.max(getSelectionStart(), 0), currentText.length());
				span = FaceConversionUtil.getInstace().getExpressionString(context,selectStartText+text+selectEndText);
			}else{
				CharSequence selectEndText = currentText.subSequence(Math.max(getSelectionEnd(), 0), currentText.length());
				span = FaceConversionUtil.getInstace().getExpressionString(context,selectStartText+text+selectEndText);
			}
			setText(span);
				setSelection(selectStartText.length()+text.length());
				return true;

		}
		return super.onTextContextMenuItem(id);
	}

	@Override
	protected void onTextChanged(CharSequence text, int start,
			int lengthBefore, int lengthAfter) {
				// else if(!TextUtils.isEmpty(text)){
		// setText(SmileUtils.getSmiledText(getContext(),
		// text),BufferType.SPANNABLE);
		// }
		super.onTextChanged(text, start, lengthBefore, lengthAfter);
	}

}

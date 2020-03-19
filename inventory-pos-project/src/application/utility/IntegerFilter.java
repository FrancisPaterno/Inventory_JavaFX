package application.utility;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

public class IntegerFilter extends DocumentFilter{

	public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
		Document doc = fb.getDocument();
		StringBuilder sb = new StringBuilder();
		sb.append(doc.getText(0, doc.getLength()));
		sb.insert(offset, string);
		
		if(DataValidator.isInteger(sb.toString())) {
			super.insertString(fb, offset, string, attr);
		}
	}
	
	public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
		Document doc = fb.getDocument();
		StringBuilder sb = new StringBuilder();
		sb.append(doc.getText(0, doc.getLength()));
		sb.replace(offset, offset + length, text);

		if(DataValidator.isInteger(sb.toString())) {
			super.replace(fb, offset, length, text, attrs);
		}
		else {
			
		}
	}

	public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
		Document doc = fb.getDocument();
		StringBuilder sb = new StringBuilder();
		sb.append(doc.getText(0, doc.getLength()));
		sb.delete(offset, offset + length);
		
		if(DataValidator.isInteger(sb.toString())) {
			super.remove(fb, offset, length);
		}
		else {
			
		}
	}
}

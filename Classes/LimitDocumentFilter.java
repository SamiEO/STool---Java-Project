import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
//This class is used to set maximum lengths for different inputfields
// NOTE! This code is not written by me but instead borrowed
//		The source can be found at: http://stackoverflow.com/a/24473097
public class LimitDocumentFilter extends DocumentFilter{
	private int limit;
	
	public LimitDocumentFilter(int l){
		this.limit = l;
	}
	
	public void replace(FilterBypass fb, int offset, int length, String text, javax.swing.text.AttributeSet attrs) throws BadLocationException {
        int currentLength = fb.getDocument().getLength();
        int overLimit = (currentLength + text.length()) - limit - length;
        if (overLimit > 0) {
            text = text.substring(0, text.length() - overLimit);
        }
        if (text.length() > 0) {
            super.replace(fb, offset, length, text, attrs); 
        }
    }
}

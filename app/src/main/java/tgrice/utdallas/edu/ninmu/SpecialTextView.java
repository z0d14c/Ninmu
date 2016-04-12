package tgrice.utdallas.edu.ninmu;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Thomas on 4/11/2016.
 * Based on code from: http://stackoverflow.com/a/10766791/3565842
 */
public class SpecialTextView extends TextView {

    public SpecialTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SpecialTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SpecialTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "Sansumi-Bold.ttf");
            setTypeface(tf);
        }
    }

}

package com.brace.android.b31.spo2andhrv.glossary;

import android.content.Context;
import com.brace.android.b31.R;



/**
 * Created by Administrator on 2017/9/19.
 */

public class BreathGlossary extends AGlossary {
    public BreathGlossary(Context context) {
        super(context);
    }

    @Override
    public void getGlossaryString() {
        head = getResoureStr(R.string.vpspo2h_toptitle_breath);
        groupString = getResoures(R.array.glossary_osahs);
        itemString = new String[][]{
                getResoures(R.array.glossary_breath_item_1),
                getResoures(R.array.glossary_breath_item_2),
                getResoures(R.array.glossary_breath_item_3),
        };
    }

}

package com.brace.android.b31.spo2andhrv.glossary;

import android.content.Context;
import com.brace.android.b31.R;



/**
 * Created by Administrator on 2017/9/19.
 */

public class BeathBreathGlossary extends AGlossary {
    public BeathBreathGlossary(Context context) {
        super(context);
    }

    @Override
    public void getGlossaryString() {
        head = getResoureStr(R.string.vpspo2h_state_breathbreak);
        groupString = getResoures(R.array.glossary_osahs);
        itemString = new String[][]{
                getResoures(R.array.glossary_breathbreak_item_1),
                getResoures(R.array.glossary_breathbreak_item_2),
                getResoures(R.array.glossary_breathbreak_item_3),
        };
    }

}


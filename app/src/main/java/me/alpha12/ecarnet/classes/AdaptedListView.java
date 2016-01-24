package me.alpha12.ecarnet.classes;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by guilhem on 22/01/2016.
 */
public class AdaptedListView extends ListView {

    private int numberOfElements;

        public AdaptedListView(Context context) {
            super(context);
        }

        public AdaptedListView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public AdaptedListView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, numberOfElements*49-1);
        }


    public void setNumberOfElements(int value){
        numberOfElements = value;
    }
}


package com.example.myaccountbook;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

public class Decorator  implements DayViewDecorator {

    private final Drawable 초기화,클릭;
    private int color;
    private HashSet<CalendarDay> dates;
    private TextView textView;

    public Decorator(Collection<CalendarDay> dates, Activity context) {

        초기화 = context.getDrawable(R.drawable.non);
        클릭 = context.getDrawable(R.drawable.transfer_click);


        this.dates = new HashSet<>(dates);

    }

    @Override
    public boolean shouldDecorate(CalendarDay day){
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
//        view.setSelectionDrawable(drawable);
//        view.setBackgroundDrawable();
    }

}
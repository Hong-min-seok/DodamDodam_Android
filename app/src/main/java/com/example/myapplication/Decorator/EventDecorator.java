package com.example.myapplication.Decorator;

import android.app.Activity;
import android.graphics.drawable.Drawable;

import com.example.myapplication.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

public class EventDecorator implements DayViewDecorator {
    
    private final Drawable drawable;
    private int color;
    private HashSet<CalendarDay> dates;
    
    public EventDecorator(int color, Collection<CalendarDay> dates, Activity context) {
        drawable = context.getResources().getDrawable(R.drawable.waterdrop);
        this.color = color;
        this.dates = new HashSet<>(dates);
        
    }
    
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }
    
    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
        //view.addSpan(new DotSpan(5, color));
    }
}
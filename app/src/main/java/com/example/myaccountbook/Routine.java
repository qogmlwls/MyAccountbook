package com.example.myaccountbook;

import java.util.Calendar;
import java.util.Date;

public class Routine {


    public static final int 반복없음 = 0;
    public static final int 반복있음 = 1;

    static final String 주중 = "주중";
    static final String 주말 = "주말";
    static final String 월말 = "월말";

    static final long 하루 = 1000*60*60*24;

    public static Date getTime(Date time, String routineType){

        Date result;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        int dayOfWeekNumber = calendar.get(Calendar.DAY_OF_WEEK);

        if(routineType.equals(주중)){

            if(dayOfWeekNumber>1 && dayOfWeekNumber <7){
                return time;
            }
            else{

                long m_time;
                // 일요일
                if(dayOfWeekNumber == 1){
                    m_time = time.getTime()+하루;

                }
                else if(dayOfWeekNumber == 7){
                    m_time = time.getTime()+하루*2;
                }
                else{
                    return null;
                }
                result = new Date(m_time);
                return result;
            }



        }
        else if(routineType.equals(주말)){

            if(dayOfWeekNumber == 1 || dayOfWeekNumber == 7){
                return time;
            }
            else{

                long m_time;
                // 일요일 2 + 5
                // 3 + 4
                // 4 + 3 (7-4)
                // 5 + 2 (7-3)
                // 6 + 1 (7-6)
                m_time = time.getTime()+하루*(7-dayOfWeekNumber);

                result = new Date(m_time);
                return result;
            }



        }
        else if(routineType.equals(월말)){

            int day = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);  //말일
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH); // 월은 0부터 시작하므로 +1 해줍니다.

            calendar.set(year,month,day);

            result = calendar.getTime();
            return result;

        }
        else{
            return time;
        }

    }


    public static boolean 오늘또는오늘이전인지(Date 날짜){

        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // 월은 0부터 시작하므로 +1 해줍니다.
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(날짜);

        int 나의년도 = calendar2.get(Calendar.YEAR);
        int 나의월 = calendar2.get(Calendar.MONTH) + 1;
        int 나의일 = calendar2.get(Calendar.DAY_OF_MONTH);


        if(나의년도 == year && 나의월 == month && 나의일 == day){
            return true;
        }
        else if(calendar2.before(calendar)){
            return true;
        }
        else{
            return false;
        }

    }

    // true: 반복가능
    // false : 반복안됨.
    public static boolean 반복가능한날짜선택했는지(Date time, String routineType){

        Date result;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        int dayOfWeekNumber = calendar.get(Calendar.DAY_OF_WEEK);

        if(routineType.equals(주중)){

            if(dayOfWeekNumber>1 && dayOfWeekNumber <7){
                return true;
            }
            else{

                return false;
            }

        }
        else if(routineType.equals(주말)){

            if(dayOfWeekNumber == 1 || dayOfWeekNumber == 7){
                return true;
            }
            else
                return false;


        }
        else if(routineType.equals(월말)){

            int 말일 = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);  //말일
            int 일 = calendar.get(Calendar.DAY_OF_MONTH);
            if(말일 == 일 ){
                return true;
            }
            else{
                return false;
            }

        }
        else{
            return true;
        }

    }


}



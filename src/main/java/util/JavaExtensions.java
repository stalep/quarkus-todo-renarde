package util;

import io.quarkus.qute.TemplateExtension;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@TemplateExtension
public class JavaExtensions {
    public static boolean isRecent(Date date){
        Date now = new Date();
        Calendar cal = new GregorianCalendar();
        cal.add(Calendar.MONTH, -6);
        Date sixMonthsAgo = cal.getTime();
        return date.before(now) && date.after(sixMonthsAgo);
    }
}

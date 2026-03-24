package kore.botssdk.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import java.util.Calendar;
import java.util.Date;

public class DateUtilsTest {

    @Test
    public void testGetCurrentDateTime() {
        String currentDateTime = DateUtils.getCurrentDateTime();
        assertNotNull(currentDateTime);
        // Format is yy_MM_dd_HH_mm_ss, so it should be 17 characters long
        assertEquals(17, currentDateTime.length());
    }

    @Test
    public void testIsYesterday() {
        long now = System.currentTimeMillis();
        long yesterday = now - 24 * 60 * 60 * 1000;
        assertTrue(DateUtils.isYesterday(yesterday));
        assertFalse(DateUtils.isYesterday(now));
    }

    @Test
    public void testGetCorrectedTimeZone() {
        assertEquals("asia/kolkata", DateUtils.getCorrectedTimeZone("Asia/Calcutta"));
        assertEquals("utc", DateUtils.getCorrectedTimeZone("UTC"));
        assertEquals("", DateUtils.getCorrectedTimeZone(null));
        assertEquals("", DateUtils.getCorrectedTimeZone("  "));
    }

    @Test
    public void testGetDateFromFormat() {
        // Test with a specific date and format
        String dateStr = "2023-10-25";
        String format = "yyyy-MM-dd";
        long millis = DateUtils.getDateFromFormat(dateStr, format, 0);
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        assertEquals(2023, calendar.get(Calendar.YEAR));
        assertEquals(Calendar.OCTOBER, calendar.get(Calendar.MONTH));
        assertEquals(25, calendar.get(Calendar.DAY_OF_MONTH));
    }
}

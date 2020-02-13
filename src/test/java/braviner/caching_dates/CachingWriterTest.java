package braviner.caching_dates;

import org.junit.Assert;
import org.junit.Test;

public class CachingWriterTest {

     @Test
     public void testAutoLatching() {
          CachingWriter writer = new CachingWriter();

          Assert.assertEquals("2020-01-02T10:30:00.000",
                  writer.compressTimestamp(2020, 1, 2, 10, 30, 0, 0));
          Assert.assertEquals("34:00.000",
                  writer.compressTimestamp(2020, 1, 2, 10, 34, 0, 0));
          Assert.assertEquals("100",
                  writer.compressTimestamp(2020, 1, 2, 10, 34, 0, 100));
          Assert.assertEquals("01.000",
                  writer.compressTimestamp(2020, 1, 2, 10, 34, 1, 0));
          Assert.assertEquals("02.500",
                  writer.compressTimestamp(2020, 1, 2, 10, 34, 2, 500));
     }

     @Test
     public void testManualLatching() {
          CachingWriter writer = new CachingWriter();
          writer.disableAutoLatching();

          Assert.assertEquals("2020-01-02T10:30:00.000",
                  writer.compressTimestamp(2020, 1, 2, 10, 30, 0, 0));
          Assert.assertEquals("2020-01-02T10:34:00.000",
                  writer.compressTimestamp(2020, 1, 2, 10, 34, 0, 0));
          writer.latch();
          Assert.assertEquals("100",
                  writer.compressTimestamp(2020, 1, 2, 10, 34, 0, 100));
          Assert.assertEquals("01.000",
                  writer.compressTimestamp(2020, 1, 2, 10, 34, 1, 0));
          Assert.assertEquals("02.500",
                  writer.compressTimestamp(2020, 1, 2, 10, 34, 2, 500));
          Assert.assertEquals("02.600",
                  writer.compressTimestamp(2020, 1, 2, 10, 34, 2, 600));
          writer.latch();
          Assert.assertEquals("700",
                  writer.compressTimestamp(2020, 1, 2, 10, 34, 2, 700));
     }

}

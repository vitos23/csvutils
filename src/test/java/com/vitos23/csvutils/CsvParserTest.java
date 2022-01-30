package com.vitos23.csvutils;

import com.vitos23.csvutils.exceptions.ParseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class CsvParserTest {
    @Test
    void test1() {
        CsvTable table = new CsvTable(
                new String[]{"a", "b", "c", "d"},
                new String[][]{{"1", "2", "3", "4"}, {"5", "6", "7", "8"}}
        );
        String s = "a,b,c,d\n1,\"2\",3,4\r\n\"5\",6,7,8\n";
        CsvTable tableParsed = CsvParser.parse(s, true);
        assertEquals(table, tableParsed);
    }

    @Test
    void test2() {
        CsvTable table = new CsvTable(
                new String[]{"a", "b", "c", "d"},
                new String[][]{{"1", "2", "3\n2", "4"}, {"5", "6", "7", "8"}}
        );
        String s = "a,b,c,d\n1,\"2\",\"3\n2\",4\r\n\"5\",6,7,8\n";
        CsvTable tableParsed = CsvParser.parse(s, true);
        assertEquals(table, tableParsed);
    }

    @Test
    void test3() {
        CsvTable table = new CsvTable(
                new String[]{"a", "b", "c", "d"},
                new String[][]{{"1", "2", "3\n2", "4"}, {"5", "6\"", "7", "8"}}
        );
        String s = "a;b;c;d\n1;\"2\";\"3\n2\";4\r\n\"5\";6\"\";7;8\n";
        CsvTable tableParsed = CsvParser.parse(s, true, ";");
        assertEquals(table, tableParsed);
    }

    @Test
    void testInvalidNotSameSize1() {
        String s = "a;b;c;d\n1;\"2\";\"3\n2\";4\r\n\"5\";6\"\";7;8;9\n";
        invalid(s, true, ";");
    }

    @Test
    void testInvalidNotSameSize2() {
        String s = "a;b;c;d\n1;\"2\";\"3\n2\";4\r\n\"5\";6\"\";7;8\n\n";
        invalid(s, true, ";");
    }

    @Test
    void testInvalidMissingQuote1() {
        String s = "a,b,c,d\n1,2\",\"3\n2\",4\r\n\"5\",6,7,8\n";
        invalid(s, true, ",");
    }

    @Test
    void testInvalidMissingQuote2() {
        String s = "a,b,c,d\n1,2,\"3\n2\",4\r\n\"5,6,7,8\n";
        invalid(s, true, ",");
    }

    @Test
    void testInvalidCharsAfterValue() {
        String s = "a,b,c,d\n1,\"2\",3,4\r\n\"5\"123,6,7,8\n";
        invalid(s, true, ",");
    }

    private void invalid(String invalidCsvTable, boolean hasHeader, String delimiter) {
        try {
            CsvTable table = CsvParser.parse(invalidCsvTable, hasHeader, delimiter);
            fail("Expected error while parsing following csv table:\n" + invalidCsvTable);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
    }

    /*@Test
    void randomTests() {
        Random rand = new Random(1);
        for (int i = 0; i < 50; i++) {
            randomTest(rand);
        }
    }

    void randomTest(Random rand) {
        boolean hasHeader = rand.nextBoolean();
        int colCnt = rand.nextInt(20) + 1;
        int rowCnt = rand.nextInt(20) + 1;
        String[] header = null;
        if (hasHeader) {
            header = new String[colCnt];

        }
    }

    String randomString(int length) {

    }*/
}

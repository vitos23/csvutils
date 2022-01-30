package com.vitos23.csvutils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

public class CsvTableTest {
    @Test
    void test() {
        CsvTable table = new CsvTable(
                new String[]{"a", "b", "c", "d"},
                new String[][]{{"1", "2", "3", "4"}, {"5", "6", "7", "8"}}
        );
        assertEquals(4, table.getWidth());
        assertEquals(2, table.getHeight());
        assertEquals("3", table.getCell(0, 2));
    }

    @Test
    void testToHtml1() {
        CsvTable table = new CsvTable(
                new String[]{"a", "b", "c", "d"},
                new String[][]{{"1", "2", "3", "4"}, {"5", "6", "7", "8"}}
        );
        String validHtml = "<table><thead><tr><th>a</th><th>b</th><th>c</th><th>d</th></tr></thead>" +
                "<tbody><tr><td>1</td><td>2</td><td>3</td><td>4</td></tr>" +
                "<tr><td>5</td><td>6</td><td>7</td><td>8</td></tr></tbody></table>";
        assertEquals(validHtml, table.toHtml());
    }

    @Test
    void testToHtml2() {
        CsvTable table = new CsvTable(
                new String[]{"a", "b\"1\"", "c", "d"},
                new String[][]{{"1&", "2", "3>", "4"}, {"5", "6", "<7", "8"}}
        );
        String validHtml = "<table><thead><tr><th>a</th><th>b\"1\"</th><th>c</th><th>d</th></tr></thead>" +
                "<tbody><tr><td>1&amp;</td><td>2</td><td>3&gt;</td><td>4</td></tr>" +
                "<tr><td>5</td><td>6</td><td>&lt;7</td><td>8</td></tr></tbody></table>";
        assertEquals(validHtml, table.toHtml());
    }

    @Test
    void testToJson1() {
        CsvTable table = new CsvTable(
                new String[]{"a", "b", "c", "d"},
                new String[][]{{"1", "2", "3", "4"}, {"5", "6", "7", "8"}}
        );
        JsonArray validArray = JsonParser.parseString("[{\"a\":\"1\",\"b\":\"2\",\"c\":\"3\",\"d\":\"4\"}," +
                "{\"a\":\"5\",\"b\":\"6\",\"c\":\"7\",\"d\":\"8\"}]").getAsJsonArray();
        assertEquals(validArray, table.toJson());
    }

    @Test
    void testToJson2() {
        CsvTable table = new CsvTable(
                new String[][]{{"1", "2", "3", "4"}, {"5", "6", "7", "8"}}
        );
        JsonArray validArray = JsonParser.parseString("[{\"COL_1\":\"1\",\"COL_2\":\"2\"," +
                "\"COL_3\":\"3\",\"COL_4\":\"4\"},{\"COL_1\":\"5\",\"COL_2\":\"6\"," +
                "\"COL_3\":\"7\",\"COL_4\":\"8\"}]").getAsJsonArray();
        assertEquals(validArray, table.toJson());
    }

    @Test
    void testToJsonArrays() {
        CsvTable table = new CsvTable(
                new String[]{"a", "b", "c", "d"},
                new String[][]{{"1", "2", "3", "4"}, {"5", "6", "7", "8"}}
        );
        JsonArray validArray = JsonParser.parseString("[[\"1\",\"2\",\"3\",\"4\"],[\"5\",\"6\",\"7\",\"8\"]]")
                .getAsJsonArray();
        assertEquals(validArray, table.toJsonArrays());
    }

    @Test
    void testToJsonColumnArray() {
        CsvTable table = new CsvTable(
                new String[]{"a", "b", "c", "d"},
                new String[][]{{"1", "2", "3", "4"}, {"5", "6", "7", "8"}}
        );
        JsonArray validArray = JsonParser.parseString("[{\"a\":\"1\",\"b\":\"2\",\"c\":\"3\",\"d\":\"4\"}," +
                "{\"a\":\"5\",\"b\":\"6\",\"c\":\"7\",\"d\":\"8\"}]").getAsJsonArray();
        assertEquals(validArray, table.toJson());
    }

    @Test
    void testEquals() {
        CsvTable table1 = new CsvTable(
                new String[]{"a", "b", "c", "d"},
                new String[][]{{"1", "2", "3", "4"}, {"5", "6", "7", "8"}}
        );
        CsvTable table2 = new CsvTable(
                new String[]{"a", "b", "c", "d"},
                new String[][]{{"1", "2", "3", "4"}, {"5", "6", "10", "8"}}
        );
        table2.setCell(1, 2, "7");
        assertEquals(table1, table2);
    }

    @Test
    void testNotEquals() {
        CsvTable table1 = new CsvTable(
                new String[]{"a", "b", "c", "d"},
                new String[][]{{"1", "2", "3", "4"}, {"5", "6", "7", "8"}}
        );
        CsvTable table2 = new CsvTable(
                new String[]{"a", "b", "c", "d"},
                new String[][]{{"1", "2", "3", "4"}, {"5", "6", "10", "8", "7"}}
        );
        assertNotEquals(table1, table2);
    }
}

# csvutils 

A simple csv parser and converter into html table or json.
For json processing [Gson](https://github.com/google/gson) library is used.

### Usage

1. Parsing csv. Csv file should follow RFC 4180 rules.
```java
// (String) source - string with csv file content
// (boolean) hasHeader
// (String) delimiter
CsvTable table = CsvParser.parse(source, hasHeader, delimiter);
```

2. Converting CsvTable into simple html table
```java
// (CsvTable) table
String htmlTable = table.toHtml();
```

3. Converting CsvTable into json

- to array of objects of pairs `"column header" - "value"`
```java
JsonArray json = table.toJson();
```

- to array of arrays (each array is row values)
```java
JsonArray json = table.toJsonArrays();
```

- to json object of pairs `"column header" - "column values"`
```java
JsonObject json = table.toJsonColumnArray();
```
Caching Dates Writer
====================

Want to write a large number of dates to a file, in which frequently the ms, or second component is the only thing that changes? At least the date probably remained the same.

`DateCachingWriter` serializes dates, but only serializing the part that has changed. For examples, this sequence of dates
```
2020-01-01T10:00:00.000
2020-01-01T10:00:00.500
2020-01-01T10:00:00.750
2020-01-01T10:00:10.100
2020-01-01T10:10:10.100
2020-01-03T10:00:00.000
2020-01-03T10:00:05.000
```
would be serialized as
```
2020-01-01T10:00:00.000
500
750
10.100
10:10.100
2020-01-03T10:00:00.000
05.000
```
In CSV files where the majority of characters are in datetimes, this can respresent a significant storage saving.


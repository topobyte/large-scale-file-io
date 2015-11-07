## License

This library is released under the terms of the GNU Lesser General Public
License.

See LGPL.md and GPL.md for details.

## About

This library provides utilities for working with many files at once. When
opening too many `FileInputStream`s or `FileOutputStream`s at the same time,
the operating system will not allow any new files to be opened anymore
because of per-process limits. To avoid this and work with an arbitrary
number of files at the same time transparently, the library provides
`InputStream` and `OutputStream` implementations that open the file for each
reading or writing operation and close it afterwards. When combined with
buffering, the performance doesn't drop too much.

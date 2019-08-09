# Change Log

### Unreleased

* Add `DataDocument.getChildElements()` helper method that returns a list of the child elements, skipping all the comments.
* Add `DataElement.getName()` helper method that accesses the first token of line.
* Fix parser so that it gracefully handles whitespace only lines by skipping them. This includes lines that are prefixed with tab delimiters as well as those that just contain spaces. This is required to correctly load the data that ships with Endless Sky that includes ~5 instances of this pattern.

### [v0.01](https://github.com/realityforge/zifnab/tree/v0.01) (2019-08-06)
[Full Changelog](https://github.com/realityforge/zifnab/compare/b24bbdea2237c119e17341e5597c42b21b76a9c9...v0.01)

 ‎🎉	Initial super-alpha release ‎🎉.

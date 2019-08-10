# Change Log

### Unreleased

* Add initial spike for parsing, building and manipulating an in memory representation of the `system` data from Endless Sky. The method `zifnab.config.SystemConfig.from(DataElement)` is used to parse system data.
* Add simplified `element()` and `comment()` factory methods onto both `DataDocument` and `DataElement` for creating children. Eaxample usage includes:
  - `e.element(location, "planet", "mars")`
  - `e.element("planet", "mars")`
  - `e.comment(location, "Great planet!")`
  - `e.comment("Great planet!")`


### [v0.02](https://github.com/realityforge/zifnab/tree/v0.02) (2019-08-09)
[Full Changelog](https://github.com/realityforge/zifnab/compare/v0.01...v0.02)

* Add `DataDocument.getChildElements()` helper method that returns a list of the child elements, skipping all the comments.
* Add `DataElement.getName()` helper method that accesses the first token of line.
* Fix parser so that it gracefully handles whitespace only lines by skipping them. This includes lines that are prefixed with tab delimiters as well as those that just contain spaces. This is required to correctly load the data that ships with Endless Sky that includes ~5 instances of this pattern.
* Add `DataElement.getChildElements()` helper method that returns a list of the child elements, skipping all the comments.
* Add the constructor `DataElement(SourceLocation,DataElement,String...)` to simplify manual creation of elements.
* Add the method `DataElement.assertTokenName(String)` to simplify verification of an element when mapping to configuration objects.
* Add the method `DataElement.assertTokenCount(int count)` to simplify verification of an element when mapping to configuration objects.
* Add the method `DataElement.assertTokenCount(int min, int max)` to simplify verification of an element when mapping to configuration objects.
* Add the methods `DataElement.getStringAt(int)`, `DataElement.getIntAt(int)` and `DataElement.getDoubleAt(int)` to simplify mapping elements to configuration objects.

### [v0.01](https://github.com/realityforge/zifnab/tree/v0.01) (2019-08-06)
[Full Changelog](https://github.com/realityforge/zifnab/compare/b24bbdea2237c119e17341e5597c42b21b76a9c9...v0.01)

 ‎🎉	Initial super-alpha release ‎🎉.

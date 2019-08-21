# Change Log

### Unreleased

* Rename `DataElement.assertTokenCount(int min,int max)` to `DataElement.assertTokenCountRange(int min,int max)` for the sake of improved clarity.
* Add `DataElement.assertTokenCounts(int...)` helper method to assert that one of a set of token counts is expected.
* Add `DataElement.getTokenCount()` helper method to simplify accessing the number of tokens for an element.
* Add initial support for encoding, decoding, representing and manipulating the entities: `trade`, `outfit` and `galaxy`.
* Add `SystemConfig.isLinked(String)` helper method.
* Separate the representation of `minables` and `asteroids` which involved; introducing a separate config object `SystemConfig.Minable`, modifying existing methods on `SystemConfig` that manipulate `minables`, introducing new methods to access `minables` and removing the `SystemConfig.Asteroid.isMinable()` method.
* Refactor several of the `SystemConfig.addX(...)` methods to return the object created.
* Add the `DataElement.getIntAt(int index, int min)` method that will generate an exception if the value is below the specified minimum.
* Add the `DataElement.getDoubleAt(int index, int min)` method that will generate an exception if the value is below the specified minimum and the `DataElement.getDoubleAt(int index, int min, int max)` method that will generate an exception if the value is above the specified maximum.
* Add the `DataElement.assertLeafNode()` helper method to verify element shape during parsing.
* Add initial support for scanning and registering image assets using the `ImageRegistry` class.
* Ensure that when a `DataComment` with an empty comment string is emitted that there is not trailing whitespace on the line.
* Add an example `zifnab.examples.NormalizeDataFiles` that all data files from a data directory and write them out, thus normalizing the data.
* Ensure that outputting a token in a `DataElement` is quoted if the token contains a tab.

### [v0.03](https://github.com/realityforge/zifnab/tree/v0.03) (2019-08-10)
[Full Changelog](https://github.com/realityforge/zifnab/compare/v0.02...v0.03)

* Add initial spike for encoding, decoding, building and manipulating an in memory representation of the `system` entity from Endless Sky. The method `SystemConfig.from(DataElement)` is used to decode the system entity. The method `SystemConfig.encode(DataDocument,SystemConfig)` is used to encode the system entity. Various utilities exist on the `SystemConfig` class to access and mutate properties on the entity.
* Add simplified `element()` and `comment()` factory methods onto both `DataDocument` and `DataElement` for creating children. Eaxample usage includes:
  - `e.element(location, "planet", "mars")`
  - `e.element("planet", "mars")`
  - `e.comment(location, "Great planet!")`
  - `e.comment("Great planet!")`
* Eliminate the public constructors in `DataElement` and `DataComment` as well as the method `Document.append(DataNode)` and require that manual construction of data occurs via factory methods. This eliminates several scenarios where invalid data can be created by appending nodes to documents where the node is not a top-level node or is already owned by another document.

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

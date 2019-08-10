# TODO

This document is essentially a list of shorthand notes describing work yet to completed.
Unfortunately it is not complete enough for other people to pick work off the list and
complete as there is too much un-said.

* Change usages of `Collections.unmodifiableList(...)` so that they are compiled out in
  production mode.

  These can be extracted into a common interface with default methods? Also `getChildElements()`
  can probably migrate there as well.

* Create `XConfig` objects for representing all the entities that we are going to be manipulating.
  These should have the ability:
  - to parse data from `DataElement` instances.
  - parse delta variants from `DataElement` instances.
  - output to `DataElement` instances.
  - validate according to other `XConfig` instances.
  - Convert to `XModel` instances that is the fully resolved model?

* Investigate other tools and see if they offer any ideas:
  - https://github.com/endless-sky/endless-sky-tools
  - https://github.com/EndlessSkyCommunity/awesome-endless-sky

* Add a simple application that exposes a (read-only?) GraphQL API for accessing ES data. 
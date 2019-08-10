# zifnab: Endless Sky Content Editing Tools

[![Build Status](https://secure.travis-ci.org/realityforge/zifnab.svg?branch=master)](http://travis-ci.org/realityforge/zifnab)
[<img src="https://img.shields.io/maven-central/v/org.realityforge.zifnab/zifnab.svg?label=latest%20release"/>](https://search.maven.org/search?q=g:org.realityforge.zifnab%20a:zifnab)

## What is zifnab?

Zifnab is a simple library for manipulating and generating content for the [Endless Sky](http://endless-sky.github.io/) game.

### Getting Started

The library is released to Maven Central and can be downloaded using normal dependency download mechanisms.
The Maven dependency is:

```xml
<dependency>
  <groupId>org.realityforge.zifnab</groupId>
  <artifactId>zifnab</artifactId>
  <version>0.02</version>
</dependency>
```

See the `examples/` directory for a few examples of the tools in use.

### Data File Format

The `zifnab.hdf` package contains tools for reading and writing the data file format used by `Endless Sky`.

#### Reading

Reading the file format is as simple as:

```java
final Path file = ...;
final DataFile dataFile = DataFile.read( file );
```

The `DataFile` object has a reference to the in memory representation of the data format and it is reasonably
easy to traverse to extract information from the the file. i.e.

```java
final long topLevelElements =
  dataFile.getDocument().getChildren().stream().filter( e -> e instanceof DataElement ).count();
System.out.println( topLevelElements + " top-level elements found in file." );
```

#### Writing

The current API for generating data in the correct format is low-level. A high-level API is expected to be available
in the future. The low-level API for constructing the data file looks like:

```java
final DataDocument document = new DataDocument();
document.comment( "The humanitarian mission!" );
final DataElement element1 = document.element( "mission", "Drought Relief" );
element1.comment( "The name of the mission as presented to user" );
element1.element( "name", "Drought relief to <planet>" );
final DataElement offer = element1.element( "to", "offer" );
offer.element( "random", "<", "10" );

final DataFile dataFile = new DataFile( Paths.get( "output.txt" ), document );
dataFile.write();
```

And this would produce a file named `output.txt` that contains

```
# The humanitarian mission!
mission "Drought Relief"
	# The name of the mission as presented to user
	name "Drought relief to <planet>"
	to offer
		random < 10
```

# Contributing

The project was released as open source so others could benefit from the project. We are thankful for any
contributions from the community. A [Code of Conduct](CODE_OF_CONDUCT.md) has been put in place and
a [Contributing](CONTRIBUTING.md) document is under development.

# License

The project is licensed under [Apache License, Version 2.0](LICENSE).

[![Build Status](https://travis-ci.org/SpaiR/byond-dmm-util.svg?branch=master)](https://travis-ci.org/SpaiR/byond-dmm-util)
[![Javadocs](https://www.javadoc.io/badge/io.github.spair/byond-dmm-util.svg)](https://www.javadoc.io/doc/io.github.spair/byond-dmm-util)
[![License](http://img.shields.io/badge/license-MIT-blue.svg)](http://www.opensource.org/licenses/MIT)

# BYOND Dmm Util

## About 

Library with util methods for `dmm` files.

Features:
 - able to render `dmm` files into proper image
 - compare two maps to get difference between them

## Installation
[![Maven Central](https://img.shields.io/maven-central/v/io.github.spair/byond-dmm-util.svg?style=flat)](https://search.maven.org/search?q=a:byond-dmm-util)
[![JCenter](https://img.shields.io/bintray/v/spair/io.github.spair/byond-dmm-util.svg?label=jcenter)](https://bintray.com/spair/io.github.spair/byond-dmm-util/_latestVersion)

Library deployed to Maven Central and JCenter repositories.

#### pom.xml
```
<dependency>
    <groupId>io.github.spair</groupId>
    <artifactId>byond-dmm-util</artifactId>
    <version>${last.version}</version>
</dependency>
```

#### build.gradle:
```
compile 'io.github.spair:byond-dmm-util:${last.version}'
```

## How To Use

The main class you will use is `Dmm`. To create it you should provide to it constructor `DmmData` and `Dme` objects.
The info how to get them could be found [here](https://github.com/SpaiR/dmm-io) and [here](https://github.com/SpaiR/byond-dme-parser).

### DmmDrawer

Renders `Dmm` object into `BufferedImage`. Has ability to render specific region of map or apply object filter to exclude specific types from render result. For example: 
```
DmmDrawer.drawMap(dmm, MapRegion.of(1, 5), FilterMode.IGNORE, "/area", "/turf")
```

### DmmDiffer

Compares two `Dmm` objects and returns list of `DiffPoint` or `MapRegion` with specific area of differences.

More could be found in [JavaDoc](https://www.javadoc.io/doc/io.github.spair/byond-dmm-util).

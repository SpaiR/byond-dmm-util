[![Build Status](https://travis-ci.org/SpaiR/byond-dmm-util.svg?branch=master)](https://travis-ci.org/SpaiR/byond-dmm-util)
[![Javadocs](https://www.javadoc.io/badge/io.github.spair/byond-dmm-util.svg)](https://www.javadoc.io/doc/io.github.spair/byond-dmm-util)
[![License](http://img.shields.io/badge/license-MIT-blue.svg)](http://www.opensource.org/licenses/MIT)

# BYOND Dmm Util

## About 

Library to parse and render DMM files.

## Installation
[![Maven Central](https://img.shields.io/maven-central/v/io.github.spair/byond-dmm-util.svg?style=flat)](http://search.maven.org/#search|ga|1|g:"io.github.spair"a:"byond-dmm-util")
[![JCenter](https://img.shields.io/bintray/v/spair/io.github.spair/byond-dmm-util.svg?label=jcenter)](https://bintray.com/spair/io.github.spair/byond-dmm-util/_latestVersion)

Library deployed to Maven Central and JCenter repositories.

#### pom.xml
```
<dependency>
    <groupId>io.github.spair</groupId>
    <artifactId>byond-dmm-util</artifactId>
    <version>0.1.4.1</version>
</dependency>
```

#### build.gradle:
```
compile 'io.github.spair:byond-dmm-util:0.1.4.1'
```

## How To Use

### DmmParser

Class to parse `.dmm` file. As an arguments takes `File` which is dmm file itself and `Dme` object,
taken from [byond-dme-parser](https://github.com/SpaiR/byond-dme-parser) library.

### DmmRender

Renders `Dmm` object into `BufferedImage`. Has ability to render specific region of map or apply object filter to exclude it from render result. For example: 
```
DmmRender.renderToImage(dmm, MapRegion.of(1, 5), "/area", "/turf")
```

### DmmComparator

Compares two `Dmm` objects and returns `MapRegion` with specific area of differences.

More could be found in [JavaDoc](https://www.javadoc.io/doc/io.github.spair/byond-dmm-util).

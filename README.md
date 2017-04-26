# Spoon-grime

This is a set of [Spoon](http://spoon.gforge.inria.fr/) processors to calculate metrics for pattern 
grime in GoF pattern instances. The processors can be applied as described in Spoon's [website](http://spoon.gforge.inria.fr/first_analysis_processor.html#apply-the-processor).

Spoon-grime is available as a standalone application, which will calculate all available metrics.

In all cases, the metrics are calculated and exported as an XML file.

**Notes:**
* built with Spoon 5.6.0
* requires JVM 1.8
* compatible with Java 7 (or lower) and Java 8 source code

## Usage

### Processor
```shell
$ java -classpath spoon-grime-0.1.0.jar:spoon-core-5.6.0-jar-with-dependencies.jar 
spoon.Launcher -i <src-folder> -p nl.rug.search.patterngrime.spoon.processors.<MetricProcessor>
```

### Standalone 
```
> java -jar spoon-grime.jar -n <pattern-description-xml> -s <src-folder> [-o <output-xml>]
    <pattern-description-xml>  xml file describing all pattern instances in the source code
    <src-folder>               folder contaning source files
    <output-xml>               output file (defaults to "./<pattern-description-xml>.pttgrime.xml"
```


## Metrics 

#### [Class grime] cg-npm
**Processor:** nl.rug.search.patterngrime.spoon.processors.CgNpmProcessor

Number of public methods that are not part of the original pattern definition.
```
// steps for calculation
1. get list of public methods part of the pattern instace (from pattern description)
2. get list of public methods in the class (from Spoon)
3. subtract set (1) from set (2)
```

#### [Class grime] cg-na
**Processor:** nl.rug.search.patterngrime.spoon.processors.CgNaProcessor

Number of attributes that are not part of the original pattern definition.
```
// steps for calculation
1. get list of attributes that are part of the pattern instace (from pattern description)
2. get list of class attributes (from Spoon)
3. subtract set (1) from set (2)
```

#### [Modular grime] mg-ca
**Processor:** nl.rug.search.patterngrime.spoon.processors.MgCaProcessor

Pattern instance afferent coupling.
```
// steps for calculation
1. get list of classes of the pattern instance (from pattern description)
2. for each class in set (1), get classes in the system that depend on it (from Spoon)
3. remove duplications in set (2)
4. subtract set (1) from set (2)
```

#### [Modular grime] mg-ce
**Processor:** nl.rug.search.patterngrime.spoon.processors.MgCeProcessor

Pattern instance efferent coupling.
```
// steps for calculation
1. get list of classes of the pattern instance (from pattern description)
2. for each class in set (1), get list of (class) dependencies
3. remove duplications in set (2)
4. subtract set (1) from set (2)
```


#### [Organizational grime] og-np
**Processor:** nl.rug.search.patterngrime.spoon.processors.OgNpProcessor

Number of packages within the pattern instance.
```
// steps for calculation
1. get list of classes of the pattern instance (from pattern description)
2. for each class in set (1), get package name
3. remove duplications from set (2)
```

#### [Organizational grime] og-ca
**Processor:** nl.rug.search.patterngrime.spoon.processors.OgCaProcessor

Fan-in at the package level. 
```
// steps for calculation
1. get list of classes of the pattern instance (from pattern description)
2. for each class in set (1), get package name
3. remove duplications from set (2)
4. for each package in set(3), get classes in the system that depend on it (from Spoon)
5. for each class in set (4), get package name
6. remove duplications from set (5)
```

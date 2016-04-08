# Udash TodoMVC Example

Udash is a framework for modern web applications development. Basing on [Scala.js](http://www.scala-js.org/doc/) project, 
it provides type safe way of web apps development, which makes them easier to modify and maintain. 

Udash includes:

* data binding
* support for forms and data validation
* frontend application routing
* grouping DOM templates into reusable components
* type safe server communication with Udash RPC module


## Learning Scala

* [Documentation](http://scala-lang.org/documentation/)
* [API Reference](http://www.scala-lang.org/api/2.11.7/)
* [Functional Programming Principles in Scala, free on Coursera.](https://www.coursera.org/course/progfun)
* [Tutorials](http://docs.scala-lang.org/tutorials/)


## Learning Scala.js

* [Documentation](http://www.scala-js.org/doc/)
* [Tutorials](http://www.scala-js.org/tutorial/)
* [Scala.js Fiddle](http://www.scala-js-fiddle.com/)


## Learning Udash

* [Homepage](http://udash.io/)
* [Documentation](http://guide.udash.io/)


## Development

The build tool for this project is [sbt](http://www.scala-sbt.org), which is 
set up with a [plugin](http://www.scala-js.org/doc/sbt-plugin.html) 
to enable compilation and packaging of Scala.js web applications. 

The scala.js plugin for sbt supports two compilation modes:
 
* `fullOptJS` is a full program optimization, which is slower
* `fastOptJS` is fast, but produces large generated javascript files - use it for development.

After installation, run `sbt` like this:

```
$ sbt
```

You can compile once like this:

```
sbt> fastOptJS 
```

or enable continuous compilation:

```
sbt> ~fastOptJS
```

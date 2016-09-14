# Udash Files Upload Example

The Udash framework provides utilities useful for web applications with file upload/download features:

* `FileInput` object in `core` - creates a file input with binded `Property`
* `FileUploader` class in `core` - uploads files to the server and provides information about the progress via returned `Property`
* `FileDownloadServlet` and `FileUploadServlet` in `rpc` - servlet templates for uploading and downloading files

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

You can compile the project like this:

```
sbt> compile 
```

Then you can run the Jetty server as following:

```
sbt> run
```

Open: [http://localhost:8080/](http://localhost:8080/)

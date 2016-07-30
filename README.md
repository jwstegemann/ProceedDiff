[![Build Status](https://travis-ci.com/jwstegemann/proceed.svg?token=zBsDYAoDLeq7AWeK5fyk&branch=master)](https://travis-ci.com/jwstegemann/proceed)
[![Scala.js](https://www.scala-js.org/assets/badges/scalajs-0.6.8.svg)](https://www.scala-js.org)


# Proceed

a scala libary for creating user interfaces in javascript like [React](https://facebook.github.io/react/) build with [Scala.js](http://www.scala-js.org/)

## The development version

Checkout project an run `sbt ~fastOptJS` in the project. This should
download dependencies and prepare the relevant javascript files. If you open
[http://localhost:12345/target/scala-2.11/classes/index-dev.html](http://localhost:12345/target/scala-2.11/classes/index-dev.html) in your browser,
it will show you your running code in `AppJS`. You can then
edit the application in `AppJS` and see the updates be sent live to the browser
without needing to refresh the page.

## The optimized version

Run `sbt ~fullOptJS` and open up [http://localhost:12345/target/scala-2.11/classes/index-opt.html](http://localhost:12345/target/scala-2.11/classes/index-opt.html) for an optimized (~200kb) version
of the final application, usefull for final publication.

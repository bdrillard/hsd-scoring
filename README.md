# hsd-scoring

A scoring system for the University of Kansas High School Design competition consisting of a Bootstrapped webpage interface for a Clojure Ring based REST api backed by a MySQL databse. 

## Usage

Download this project with `git clone`, compile the JavaScript with `lein cljsbuild once`, and test it with `lein ring server`. 

To deploy, edit the `.lein-env` file that was generated by the `:dev` profile in the `project.clj` file, and ammend the environment variables to match the host url and database credentials. Then package with
```
lein ring uberwar
```
and deploy the resulting `war` on a Jetty server.

## License

Copyright © 2014

Distributed under the Eclipse Public License either version 1.0.

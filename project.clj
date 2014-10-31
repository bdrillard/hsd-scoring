(defproject hsd-scoring "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [ring "1.3.0"]
                 [ring/ring-json "0.3.1"]
                 [compojure "1.1.8"]
                 [korma "0.3.2"]
                 [org.clojure/java.jdbc "0.3.3"]
                 [mysql/mysql-connector-java "5.1.6"]
                 [cheshire "5.3.1"]]
  :plugins [[lein-ring "0.8.11"]]
  :ring {:handler hsd-scoring.handler/app}
  :profiles {:dev {:depencies [[javax.servlet/servlet-api "2.5"]
                               [ring-mock "0.1.5"]]}}
  :main hsd-scoring.handler)

(defproject hsd-scoring "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2069"]
                 [clj-time "0.8.0"]
                 [ring "1.3.0"]
                 [ring/ring-json "0.3.1"]
                 [jumblerg/ring.middleware.cors "1.0.1"]
                 [compojure "1.1.8"]
                 [korma "0.3.2"]
                 [org.clojure/java.jdbc "0.3.3"]
                 [mysql/mysql-connector-java "5.1.6"]
                 [formative "0.8.8"]
                 [prismatic/dommy "0.1.1"]
                 [cljs-ajax "0.1.5"]
                 [enlive "1.1.5"]
                 [environ "1.0.0"]]
  :plugins [[lein-ring "0.8.11"]
            [lein-cljsbuild "1.0.0"]
            [lein-environ "1.0.0"]]
  :source-paths ["src/clj"]
  :main hsd-scoring.handler
  :ring {:handler hsd-scoring.handler/app}
  :cljsbuild {:builds [{:source-paths ["src/cljs"]
                       :compiler {:output-to "resources/js/forms.js"
                                  :optimizations :advanced
                                  :pretty-print false}}]}
  :profiles {:dev {:env {:db-url "localhost"
                         :db-port 3306
                         :db-user "root"
                         :db-pass "password"}}})

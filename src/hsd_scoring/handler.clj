(ns hsd-scoring.handler
  (:gen-class)
  (:use compojure.core
        ring.adapter.jetty)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.params :as wrap-params]
            [ring.middleware.json :as middleware]
            [hsd-scoring.routes :as rs]))

(defroutes app-routes
  (GET "/" [] "Hello world!")
  (GET "/teams" [] (rs/teams-summary)))

(def app
  (-> (handler/api app-routes)
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)))

(defn -main [& args]
  (run-jetty #'app {:port 3000}))

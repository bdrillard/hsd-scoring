(ns hsd-scoring.handler
  (:gen-class)
  (:use compojure.core
        ring.adapter.jetty)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :refer [resource-response]]
            [ring.middleware.params :as wrap-params]
            [ring.middleware.json :as middleware]
            [hsd-scoring.routes :as rs]))

(defroutes app-routes
  (GET "/" [] (resource-response "index.html"))
  (GET "/teams" [] (rs/teams-summary))
  (GET "/teams/:id" [id] (rs/create-team)))

(def app
  (-> (handler/api app-routes)
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)))

(defn -main [& args]
  (run-jetty #'app {:port 3000}))

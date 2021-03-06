(ns hsd-scoring.handler
  "Server routes handler"
  (:gen-class)
  (:use compojure.core
        ring.adapter.jetty
        environ.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :refer [response]]
            [ring.middleware.params :as wrap-params]
            [ring.middleware.json :as middleware]
            [ring.middleware.cors :as cors]
            [hsd-scoring.routes :as rs]))

;; HTTP routes
(defroutes app-routes
  (GET "/" [] (response (rs/wrap-json-url "public/index.html")))
  (GET "/results" [] (rs/results-summary))
  (context "/teams" [] (defroutes team-routes ; routes concerning team update operations
    (GET "/" [] (rs/teams-summary))
    (POST "/create" {params :params} (rs/create-team params))
    (POST "/update" {params :params} (rs/update-team params))
    (POST "/delete" {params :params} (rs/delete-team params)))))

(def app
  (-> (handler/api app-routes)
      (middleware/wrap-json-body)
      (middleware/wrap-json-response)
      (cors/wrap-cors (re-pattern (str "^http://" 
                                       (env :db-url) ":" (env :db-port) 
                                       "/team$"))))) ; allow JSON team summary to be read

(defn -main [& args]
  (run-jetty #'app {:port 3000}))

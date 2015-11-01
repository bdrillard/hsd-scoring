(ns hsd-scoring.routes
  "Functions to translate REST calls to database operations"
  (:use clojure.java.jdbc
        environ.core)
  (:require [ring.util.response :refer [response]]
            [yesql.core :refer [defquery]]
            [net.cgrand.enlive-html :refer [html-resource at set-attr emit*]]))

(defn render [t]
  (apply str t))

(def render-nodes
  (comp render emit*))

(defn wrap-json-url [response]
  (let [main (html-resource response)]
    (render-nodes (at main
        [:table#summary] (set-attr :data-url
                                   (str "http://"
                                        (env :db-url)
                                        ":3000/teams"))))))
;;; Routes served by databse operations
;; database connector parameters
(def db {:classname "com.mysql.jdbc.Driver"
         :subprotocol "mysql"
         :subname (str "//" (env :db-url) ":" (env :db-port) "/" (env :db-name))
         :user (env :db-user)
         :password (env :db-pass)})

;; SQL queries and operations over team data
(defquery select-results "sql/select-results.sql")
(defquery select-summary "sql/select-summary.sql")
(defquery create-team! "sql/insert-team.sql")
(defquery update-team-school! "sql/update-team-school.sql")
(defquery update-team-egg-drop! "sql/update-team-egg-drop.sql")
(defquery update-team-crack-safe! "sql/update-team-crack-safe.sql")
(defquery update-team-break-out! "sql/update-team-break-out.sql")
(defquery delete-team! "sql/delete-team.sql")

(defn teams-summary
  "Presents a summary of team information"
  []
  {:body (select-summary db)})

(defn results-summary 
  "Collects score results, sum the individual scores"
  []
  {:body (select-results db)}) 

(defn create-team 
  "Creates new teams provided team-name is unique"
  [{:keys [team-name]}]
  (let [rows-aff (create-team! db team-name)]
    (if (= 1 rows-aff)
      (response {:status 200
                 :body (str "Successfully created team " team-name)})
      (response {:status 403
                 :body {:error (str "The team '" team-name "' already exists")}}))))

(defn update-by-field 
  "Helper function to update correct field of team"
  [team-name field value]
  (cond
    (= field "School") (update-team-school! db value team-name)
    (= field "Egg Drop") (update-team-egg-drop! db value team-name)
    (= field "Crack the Safe") (update-team-crack-safe! db value team-name)
    (= field "Break Out") (update-team-break-out! db value team-name)))

(defn update-team 
  "Updates team field, school name or score, to new value provided the team exists"
  [{:keys [team-name field value]}]
  (let [rows-aff (update-by-field team-name field value)]
    (if (= 1 rows-aff)
      (response {:status 200
                 :body (str "Successfully updated " team-name "'s " field " to " value)})
      (response {:status 403
                 :body {:error (str "The team named '" team-name "' did not exist")}}))))

(defn delete-team 
  "Deletes a team provided the team exists"
  [{:keys [team-name]}]
  (let [rows-aff (delete-team! db team-name)]
    (if (= 1 rows-aff)
      (response {:status 200
                 :body (str "Successfully deleted team " team-name)})
      (response {:status 403
                 :body {:error (str "The team named '" team-name "' did not exist")}}))))

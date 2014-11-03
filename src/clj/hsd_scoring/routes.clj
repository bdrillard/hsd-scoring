(ns hsd-scoring.routes
  (:use korma.db
        korma.core)
  (:require [ring.util.response :refer [resource-response response]]))

(defdb mys (mysql
                  {:host "localhost"
                   :port 3306
                   :db "hsd_scoring"
                   :make-pool? true
                   :user "root"
                   :password "#yperrea1ity"}))

;;; Table definitions
(defentity team_scores)

(defn teams-summary []
  {:body (select team_scores
                 (order :team_name :asc))})

(defn create-team [params]
  (do
    (insert team_scores (values {:team_name params}))
    (response {:body "Successfully created team"})))

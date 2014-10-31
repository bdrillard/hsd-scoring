(ns hsd-scoring.routes
  (:use korma.db
        korma.core)
  (:require [ring.util.response :as res]))

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
  {:body (select team_scores)})

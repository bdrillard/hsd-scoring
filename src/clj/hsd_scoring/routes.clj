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

(defn exists? [table col value]
  (if (not-empty
        (select table (having (= col value))))
    true
    false))

(defn teams-summary []
  {:body (select team_scores
                 (order :team_name :asc))})

(defn create-team [params]
  (let [team-name (:team_name params)]
    (if (exists? team_scores :team_name team-name)
      (response {:status 403
                 :body {:error (str "The team named '" team-name "' already exists")}})
    (do
      (insert team_scores 
              (values {:team_name team-name}))
      (response {:body "Successfully created team"})))))

(defn get-score-type [name]
  (cond
    (= name "Launch") :launch_score
    (= name "Ramp") :ramp_score
    (= name "Presentation") :presentation_score))

(defn update-team [params]
  (let [team-name (:team_name params)]
    (if (exists? team_scores :team_name team-name)
      (do
        (if (contains? params :weight)
          (update team_scores
                  (set-fields {:weight (:weight params)})
                  (where (= :team_name team-name)))
          (update team_scores
                  (set-fields {(get-score-type (:competition params)) (:score params)})
                  (where (= :team_name team-name))))
        (response {:body "Successfully updated team"}))
      (response {:status 403
                 :body {:error (str "The team named '" team-name "' did not exist")}}))))

(defn delete-team [params]
  (let [team-name (:team_name params)]
    (if (exists? team_scores :team_name team-name)
      (do
        (delete team_scores (where (= :team_name team-name)))
        (response {:body "Deletion successful"}))
    (response {:status 403
               :body {:error (str "The team named '" team-name "' did not exist")}}))))  

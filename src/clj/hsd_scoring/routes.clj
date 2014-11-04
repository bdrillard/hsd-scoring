(ns hsd-scoring.routes
  (:use korma.db
        korma.core)
  (:require [ring.util.response :refer [response]]))

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

;; not my proudest moment
(defn results []
  {:body (exec-raw ["SELECT *, launch_score + ramp_score AS total FROM team_scores WHERE disqualified = 1 ORDER BY total DESC, weight ASC"] :results)}) 

(defn create-team [params]
  (let [team-name (:team_name params)]
    (if (exists? team_scores :team_name team-name)
      (response {:status 403
                 :body {:error (str "The team '" team-name "' already exists")}})
    (do
      (insert team_scores 
              (values {:team_name team-name}))
      (response {:status 200
                 :body (str "Successfully created team " team-name)})))))

(defn get-score-type [name]
  (cond
    (= name "Launch") :launch_score
    (= name "Ramp") :ramp_score
    (= name "Presentation") :presentation_score))

(defn update-team [params]
  (let [team-name (:team_name params)]
    (if (exists? team_scores :team_name team-name)
        (if (contains? params :weight)
          (do 
            (update team_scores
                  (set-fields {:weight (:weight params)})
                  (where (= :team_name team-name)))
            (response {:status 200
                       :body (str "Successfully updated " team-name "'s weight to " (:weight params))}))
          (do
            (update team_scores
                  (set-fields {(get-score-type (:competition params)) (:score params)})
                  (where (= :team_name team-name)))
            (response {:status 200
                       :body (str "Successfully updated " team-name "'s " (:competition params) " score to " (:score params))})))
      (response {:status 403
                 :body {:error (str "The team '" team-name "' did not exist")}}))))

(defn delete-team [params]
  (let [team-name (:team_name params)]
    (if (exists? team_scores :team_name team-name)
      (do
        (delete team_scores (where (= :team_name team-name)))
        (response {:status 200
                   :body (str "Deleted " team-name)}))
    (response {:status 403
               :body {:error (str "The team named '" team-name "' did not exist")}}))))  

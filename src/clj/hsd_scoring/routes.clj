(ns hsd-scoring.routes
  "Functions to translate REST calls to database operations"
  (:use korma.db
        korma.core
        environ.core)
  (:require [ring.util.response :refer [response]]
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

;; Database connection details
(defdb mys (mysql
                  {:host (env :db-url)
                   :port (env :db-port)
                   :db "hsd_scoring"
                   :make-pool? true
                   :user (env :db-user)
                   :password (env :db-pass)}))

;; Table definitions
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
  {:body (exec-raw ["SELECT *, launch_score + ramp_score AS total FROM team_scores WHERE disqualified = 0 ORDER BY total DESC, weight ASC"] :results)}) 

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

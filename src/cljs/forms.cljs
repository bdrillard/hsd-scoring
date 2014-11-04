(ns hsd-scoring.forms
  (:require [formative.core :as f]
            [formative.dom :as fd]
            [dommy.core :as d]
            [dommy.template :as t]
            [ajax.core :as ajax])
  (:require-macros [dommy.macros :refer [sel1 node]]))

(def create-team
  {:fields [{:name :team_name}]
   :validations [[:required [:team_name]]
                 [:matches #"[\w+|_?]*-\d+" :team_name "Team name must be one or more words separated by underscores trailed by a hyphen separated number"]]
   :renderer :bootstrap3-stacked})

(def update-weight
  {:fields [{:name :team_name}
            {:name :weight :datatype :float}]
   :validations [[:required [:team_name :weight]]]
   :renderer :bootstrap3-stacked})

(def update-score
  {:fields [{:name :team_name}
            {:name :competition :type :select :options ["Launch" "Ramp" "Presentation"]}
            {:name :score :datatype :int}]
   :validations [[:required [:team_name :competition :score]]
                 [:within 0 200 :score]]
   :renderer :bootstrap3-stacked})

(def delete-team
  {:fields [{:name :team_name}]
   :validations [[:required [:team_name]]]
   :renderer :bootstrap3-stacked})

(def forms
   [{:form create-team :id "create-team" :func create}
    {:form update-weight :id "update-weight" :func update-w}
    {:form update-score :id "update-score" :func update-s}
    {:form delete-team :id "delete-team" :func delete}])

(defn handler [response]
  (when-let [container (sel1 "#response-pane")]
    (if (= (:status response) 200)
      (d/append! container (node [:div.alert.alert-success.fade.in {:role "alert"} 
                                  [:a.close {:data-dismiss "alert" :href "#"} "\u00D7"]
                                  [:p (str (:body response))]]))
      (d/append! container (node [:div.alert.alert-danger.fade.in {:role "alert"} 
                                  [:a.close {:data-dismiss "alert" :href "#"} "\u00D7"]
                                  [:p (str (:error (:body response)))]])))))

(defn error [{:keys [status status-text]}]
  (.log js/console (str "something bad happened: " status " " status-text)))

(defn create [params]
  (ajax/POST "http://localhost:3000/teams/create"
        {:params {:team_name (:team_name params)}
         :format :json
         :handler handler
         :error-handler error
         :keywordize-keys true}))

(defn update-w [params]
  (ajax/POST "http://localhost:3000/teams/update"
        {:params {:team_name (:team_name params)
                  :weight (:weight params)}
         :format :json
         :handler handler
         :error-handler error
         :keywordize-keys true}))

(defn update-s [params]
  (ajax/POST "http://localhost:3000/teams/update"
        {:params {:team_name (:team_name params)
                  :competition (:competition params)
                  :score (:score params)}
         :format :json
         :handler handler
         :error-handler error
         :keywordize-keys true}))

(defn delete [params]
  (ajax/POST "http://localhost:3000/teams/delete"
        {:params {:team_name (:team_name params)}
         :format :json
         :handler handler
         :error-handler error 
         :keywordize-keys true}))

(defn main []
  (doseq [elem forms]
    (when-let [container (sel1 (str "#" (:id elem)))]
      (d/append! container (node (f/render-form (:form elem))))
      (fd/handle-submit
        (:form elem) 
        container
        (:func elem))))) 

(main)

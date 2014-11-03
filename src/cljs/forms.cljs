(ns hsd-scoring.forms
  (:require [formative.core :as f]
            [formative.dom :as fd]
            [dommy.core :as d]
            [ajax.core :as ajax])
  (:require-macros [dommy.macros :refer [sel1 node]]))

(def create-team
  {:fields [{:name :h2 :type :heading :text "Create a team"}
            {:name :team_name}]})

(def update-team
  {:fields [{:name :h2 :type :heading :text "Update team data"}
            {:name :team_name}
            {:name :weight :datatype :float}
            {:name :competition :type :select :options ["Launch" "Ramp" "Presentation"]}
            {:name :score :datatype :int}]})

(def delete-team
  {:fields [{:name :h2 :type :heading :text "Remove a team"}
            {:name :team_name}]})

(def forms
   [{:form create-team :id "create-team" :func create}
    {:form update-team :id "update-team" :func update}
    {:form delete-team :id "delete-team" :func delete}])

(defn update [params]
  (js/alert (pr-str params)))

(defn handler [response]
  (.log js/console (str response)))

(defn create [params]
  (ajax/POST "http://localhost:3000/teams/create"
        {:params {:team_name (:team_name params)}
         :handler handler
         :error-handler handler}))

;(defn update [params]
;  (ajax/POST "http://localhost:3000/teams/update"
;        {:params {:team_name (:team_name params)
;                  :weight (:weight params)
;                  :competition (:competition params)
;                  :score (:score params)}
;         :handler handler
;         :error-handler handler}))

(defn delete [params]
  (ajax/POST "http://localhost:3000/teams/delete"
        {:params {:team_name (:team_name params)}
         :handler handler
         :error-handler handler}))

(defn main []
  (doseq [elem forms]
    (when-let [container (sel1 (str "#" (:id elem)))]
      (d/append! container (node (f/render-form (:form elem))))
      (fd/handle-submit
        (:form elem) 
        container
        (:func elem))))) 

(main)

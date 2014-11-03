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
            {:name :competition :type :select :options ["--" "Launch" "Ramp" "Presentation"]}
            {:name :score :datatype :int}]})

(def delete-team
  {:fields [{:name :h2 :type :heading :text "Remove a team"}
            {:name :team_name}]})

(def forms
   [{:form create-team :id "create-team"}
    {:form update-team :id "update-team"}
    {:form delete-team :id "delete-team"}])

;(defn post [params]
;  (js/alert (pr-str params)))

(defn handler [_]
  (.reload js/location))

(defn post [params]
  (ajax/POST "http://localhost:3000/teams/create"
        {:params {:team_name (:team_name params)
                  :weight (:weight params)
                  :competition (:competition params)
                  :score (:score params)}
         :handler handler}))

(defn main []
  (doseq [elem forms]
    (when-let [container (sel1 (str "#" (:id elem)))]
      (d/append! container (node (f/render-form (:form elem))))
      (fd/handle-submit
        (:form elem) container
        post)))) 

(main)

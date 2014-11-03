(ns hsd-scoring.forms
  (:require [formative.core :as f]
            [formative.dom :as fd]
            [dommy.core :as d])
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

(def remove-team
  {:fields [{:name :h2 :type :heading :text "Remove a team"}
            {:name :team_name}]})

(def forms
   [{:form create-team :id "create-team"}
    {:form update-team :id "update-team"}
    {:form remove-team :id "remove-team"}])

(defn main []
  (doseq [elem forms]
    (when-let [container (sel1 (str "#" (:id elem)))]
      (d/append! container (node (f/render-form (:form elem))))
      (fd/handle-submit
        (:form elem) container
        (fn [params]
          (js/alert (pr-str params)))))))

(main)

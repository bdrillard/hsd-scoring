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

(def forms
  [create-team
   update-team])

(defn main []
  (when-let [container (sel1 "#forms")]
    (doseq [form forms]
      (d/append! container (node (f/render-form form)))
      (fd/handle-submit
                        form container
        (fn [params]
          (js/alert (pr-str params)))))))

(main)

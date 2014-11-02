(ns hsd-scoring.forms
  (:require [formative.core :as f]
            [formative.dom :as fd]
            [dommy.core :as d])
  (:require-macros [dommy.macros :refer [sel1 node]]))

(def update-score
  {:fields [{:name :h2 :type :heading :text "Update team data"}
            {:name :team_name}
            {:name :weight :datatype :float}
            {:name :competition :type :select :options ["Launch" "Ramp" "Presentation"]}
            {:name :score :datatype :int}]})

(defn main []
  (.log js/console "Hello, world!")
  (when-let [container (sel1 "#update-team")]
    (d/append! container (node (f/render-form update-score)))
    (fd/handle-submit
                      update-score container
      (fn [params]
        (js/alert (pr-str params))))))

(main)

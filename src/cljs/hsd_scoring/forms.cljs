(ns hsd-scoring.forms
  "Forms, rendering, and AJAX functions"
  (:require [formative.core :refer [render-form]]
            [formative.dom :refer [handle-submit]]
            [dommy.core :refer [append!]]
            [ajax.core :refer [POST]])
  (:require-macros [dommy.macros :refer [sel1 node]]))

(defn handler
  "A function executed when the server response to an AJAX call is received
  back by the webpage. JSON objects representing 200 and 403 responses will
  be parsed and converted into Bootstrap alerts appended to the response-pane
  element of the webpage."
  [response]
  (when-let [container (sel1 "#response-pane")]
    (if (= (:status response) 200)
      (append! container (node [:div.alert.alert-success.fade.in {:role "alert"} 
                                  [:a.close {:data-dismiss "alert" :href "#"} "\u00D7"]
                                  [:p (str (:body response))]]))
      (append! container (node [:div.alert.alert-danger.fade.in {:role "alert"} 
                                  [:a.close {:data-dismiss "alert" :href "#"} "\u00D7"]
                                  [:p (str (:error (:body response)))]])))))

(defn error 
  "A function to print the server response to all failed AJAX calls to console"
  [{:keys [status status-text]}]
  (.log js/console (str "Fatal server error: " status " " status-text)))

;; A list representing the forms to deploy. Each full form is a map consisting
;; of a form name, its definition, and a function that makes a REST call to the
;; database for that particular form.
(def forms-list [{:form-name "create-team"
                  :form-definition {:fields [{:name :team-name}]
                                    :validations [[:required [:team-name]]
                                                  [:matches #"[\w+|_?]*_\d+" :team-name "Team name must be one or more words separated by underscores trailed by an underscore separated number"]]
                                    :renderer :bootstrap3-stacked}
                  :form-func (fn [submission]
                               (POST "http://localhost:3000/teams/create"
                                          {:params {:team-name (:team-name submission)}
                                           :format :json
                                           :handler handler
                                           :error-handler error
                                           :keywordize-keys true}))}
                 {:form-name "update-school"
                  :form-definition {:fields [{:name :team-name}
                                             {:name :school-name}]
                                    :validations [[:required [:team-name :school-name]]]
                                    :renderer :bootstrap3-stacked}
                  :form-func (fn [submission]
                               (POST "http://localhost:3000/teams/update"
                                          {:params {:team-name (:team-name submission)
                                                    :field "School"
                                                    :value (:school-name submission)}
                                           :format :json
                                           :handler handler
                                           :error-handler error
                                           :keywordize-keys true}))}
                 {:form-name "update-score"
                  :form-definition {:fields [{:name :team-name}
                                             {:name :competition 
                                              :type :select 
                                              :options ["Egg Drop" "Crack the Safe" "Break Out"]}
                                             {:name :score :datatype :int}]
                                    :validations [[:required [:team-name :competition :score]]
                                                  [:within 0 200 :score]]
                                    :renderer :bootstrap3-stacked}
                  :form-func (fn [submission]
                               (POST "http://localhost:3000/teams/update"
                                          {:params {:team-name (:team-name submission)
                                                    :field (:competition submission)
                                                    :value (:score submission)}
                                           :format :json
                                           :handler handler
                                           :error-handler error
                                           :keywordize-keys true}))}
                 {:form-name "delete-team"
                  :form-definition {:fields [{:name :team-name}]
                                    :validations [[:required [:team-name]]]
                                    :renderer :bootstrap3-stacked}
                  :form-func (fn [submission]
                               (POST "http://localhost:3000/teams/delete"
                                          {:params {:team-name (:team-name submission)}
                                           :format :json
                                           :handler handler
                                           :error-handler error 
                                           :keywordize-keys true}))}])

(defn main
  "Deploys rendered forms to their appropriate location in the webpage, deploys
  the submit handler that parses submission into a map, listens to the node the 
  form was appended to, and passes the parsed results to its respective
  submission function upon occurence of the forms submit event."
  []
  (doseq [form forms-list]
    (when-let [container (sel1 (str "#" (:form-name form)))]
      (append! container (node (render-form (:form-definition form))))
      (handle-submit
        (:form-definition form) 
        container
        (:form-func form))))) 

(main)

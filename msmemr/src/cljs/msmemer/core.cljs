(ns msmemer.core
    (:require [reagent.core :as reagent :refer [atom]]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [antizer.reagent :as ant]))

;; -------------------------
;; Views

(defn home-page []
  [:div [:h2 "Welcome to msmemer"]
   [ant/button {:on-click #(ant/message-info "Hello Reagent")} "Click Me!"]
   [ant/button {:type "primary"} "Primary"]

   [:div [:a {:href "/about"} "go to about page"]]])

(defn about-page []
  [:div [:h2 "About msmemer"]
   [:div [:p "Perdix Medical Solutions is a Startup based out of Charlottesville, VA. Perdix Medical Solutions was created by Mitch Gillin, Sean Rouffa, and Matt Zetkulic"]]
   [:div [:a {:href "/"} "go to the home page"]]])

;; -------------------------
;; Routes

(def page (atom #'home-page))

(defn current-page []
  [:div [@page]])

(secretary/defroute "/" []
  (reset! page #'home-page))

(secretary/defroute "/about" []
  (reset! page #'about-page))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))

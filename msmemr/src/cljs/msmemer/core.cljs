(ns msmemer.core
    (:require [reagent.core :as reagent :refer [atom]]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [antizer.reagent :as ant]
              ))


;;--------------------------
;; Components

(defn actual-form []
  (fn [props]
    (let [my-form (ant/get-form)]
      [ant/form
       [ant/form-item {:label "Name"}
        (ant/decorate-field my-form "name" {:rules [{:required true}]}
                            [ant/input])]
       [ant/form-item {:label "Password"}
        ;; validates that the password field is not empty
        (ant/decorate-field my-form "password" {:rules [{:required true}]}
                            [ant/input])]])))


;;Name card takes a name, the text for the card, and the width/height for the card. 
(defn name-card [name text w h]
  [ant/card {:style {:width w :height h} :title name} text])


;; -------------------------
;; Views

(defn home-page []
  [:div [:h2 "Welcome to msmemer"]
   [:div [:a {:href "/login"} "Go to Login Page"]]
   [:div [:a {:href "/about"} "go to About Page"]]
   [:div [:a {:href "/test"} "Go to Test Page"]]
   ])

(defn about-page []
  [:div [:h2 "About msmemer"]
   [:div [:a {:href "/"} "go to the home page"]]
 
    [name-card "Perdix Medical Solutions" "Perdix Medical Solutions is a Charlottesville Startup focusing on improving patinet-physician relationships thorugh better, smarter software" 400 150]

    [ant/col
     [name-card "Mitchell Gillin, CEO" "Mitch Gillin is a 4th year Biomedical Engineering student with a passion for human centered design and healthcare software. Mitch also enjoys cooking, golfing, and the occasional cold, refreshing beverage" 300 200]]
    [ant/col
     [name-card "Sean Rouffa" "Sean Rouffa is a 4th year Biomedical Engineering student who enjoys water polo and patient-focused design." 300 200]]
    [ant/col
    [name-card "Matthew Zetkulic" "Matthew is a 4th year Biomedical Engineer who excels at database management and rowing" 300 200]
     ]
   ]
   )

(defn login-page []
  [:div
   [:div  [:h1 {:style {:align "right"}} "Welcome! Lets get you logged in!"]]
   [:div (ant/create-form (actual-form))]
  [:div [:a {:href "/"} "Home Page"]]
  [:div [:a {:href "/about"} "About Page"]]]
  )

(defn test-page []
  [:div
    [ant/col {:span 16}
     [name-card "a" "b" 100 100]]
    [ant/col {:span 16}
     [name-card "a" "b" 100 100]]
   ]
   )

;; -------------------------
;; Routes

(def page (atom #'home-page))

(defn current-page []
  [:div [@page]])

(secretary/defroute "/" []
  (reset! page #'home-page))

(secretary/defroute "/about" []
  (reset! page #'about-page))

(secretary/defroute "/login" []
  (reset! page #'login-page))

(secretary/defroute "/test" []
  (reset! page #'test-page))

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

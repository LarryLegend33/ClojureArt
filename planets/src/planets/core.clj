(ns planets.core
  (:require
   [clojure.math.numeric-tower :as math]
   [genartlib.random :as gen]
   [quil.core :as q]
   [quil.middleware :as m]))

(defn setup []
  (q/frame-rate 60)
  {:sun_x 750
   :sun_y (/ (q/height) 2)
   :moon_angle 0
   :moon_speed 0.2
   :planet_angle 0 
   :planet_speed 0.05})

(defn update-state [state]

  ; using this and taking out the central sun produces switching. 2 tumbling dots. 
  ; i.e. neither is the planet...either can be the moon. adding the center
                                        ; back recreates the planet moon dynamic.
  (q/frame-rate 60)
  {;:sun_x (+ (math/round (gen/gauss 0 0)) (:sun_x state))
   ;:sun_y (+ (math/round (gen/gauss 0 0)) (:sun_y state))
   :sun_x (cond (> 1500 (:sun_x state))
                (+ 2 (:sun_x state))
                :else 0)

   ;:sun_x (cond (> 1500 (:sun_x state))
   ;;              (+ 2 (:sun_x state))
   ;;              :else 0)
   :sun_y (+ 0 (:sun_y state))
   :moon_speed (:moon_speed state)
   :moon_angle (+ (:moon_angle state) (:moon_speed state))
   :planet_speed (:planet_speed state)
   :planet_angle (+ (:planet_angle state) (:planet_speed state))})

(defn draw-state [state]
  (q/background 0 0 0)
  (q/fill 255 255 255)
;  (q/fill 255 0 0)
  (let [px (q/cos (:planet_angle state))
        py (q/sin (:planet_angle state))
        mx (q/cos (:moon_angle state))
        my (q/sin (:moon_angle state))
        planet_orbit 50
        moon_orbit 20]
    (q/with-translation [(:sun_x state)
                         (:sun_y state)]
      ; sun
      (q/ellipse 0 0 10 10)
      ; planet
;      (q/ellipse (* planet_orbit px) (* planet_orbit py) 10 10)
      (q/with-translation [(* planet_orbit px)
                           (* planet_orbit py)]
        ; moon
        (q/ellipse (* moon_orbit mx)
                   (* moon_orbit my)
                   10
                   10)
        ))))

(q/defsketch planets
  :size [1500 1000]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])

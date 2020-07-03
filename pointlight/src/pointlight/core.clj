(ns pointlight.core
  (:require [quil.core :as q]
            [genartlib.random :as gen]
            [quil.middleware :as m]
            [clojure.math.numeric-tower :as math]))

(def envwidth 1500)

(defn setup []
  (q/frame-rate 30)
  {:epoch 0
   :angle 0
   :translation_rate 4
   :center_x 0
   :center_y (/ (q/height) 2)})

(defn update-state [state]
  {:angle (+ (:angle state) 0.1)
   :translation_rate (:translation_rate state)
   ; change epoch function to get noises to switch faster
   ; have it be a mod over envwidth
   :epoch (cond (= 0 (mod (:center_x state) (/ envwidth 1)))
                (+ 1 (:epoch state))
                :else (:epoch state))
   :center_x (cond (< (:center_x state) envwidth)
                   (+ (:translation_rate state) (:center_x state))
                   :else 0)
   :center_y (:center_y state)})

;memoized function that returns an offset for a given epoch
(defn gen_offset [epoch noiselevel dot_index]
  (gen/gauss 0 noiselevel))

(def offset_mem (memoize gen_offset))

(defn draw-state [state]
  ; Clear the screen by filling it with black
  (q/background 0 0 0)
  (q/fill 255 255 255)
  (let [angle (:angle state)
        x (q/cos angle)
        y (q/sin angle)
        e_noise 0
        c_noise 0
        g_noise 0
        jitter_edge 0
        jitter_center 0
        center_x (+ (:center_x state) (offset_mem (:epoch state) g_noise 0))
        center_y (+ (:center_y state) (offset_mem (:epoch state) g_noise 1))
        orbit 50
        dotsize 10]

; add noise to either center_x, y in let, or individual translators    
    (q/with-translation [(+ center_x (offset_mem (:epoch state) c_noise 2))
                         (+ center_y (offset_mem (:epoch state) c_noise 3))]
      (cond (> center_x (/ envwidth 2))
            (q/ellipse (gen/gauss 0 jitter_center)
                       (gen/gauss 0 jitter_center)
                       dotsize
                       dotsize)))

    ; add a dummy integer so offset is consistent through the 
    ; epoch but only for specific variables
    (q/with-translation [(+ center_x (offset_mem (:epoch state) e_noise 4))
                         (+ center_y (offset_mem (:epoch state) e_noise 5))]
      (q/ellipse (gen/gauss (* orbit x) jitter_edge)
                 (gen/gauss (* orbit y) jitter_edge)
                 dotsize
                 dotsize))))

(q/defsketch pointlight
  :title "wheel"
  :size [envwidth 500]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])

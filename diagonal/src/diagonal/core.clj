(ns diagonal.core
  (:require [genartlib.random :as gen]
            [quil.core :as q]
            [quil.middleware :as m]
            [clojure.math.numeric-tower :as math]))

(defn triangle_wave
  [per amp]
  (fn [t]
    (* (* amp 2)
       (math/abs
        (- (/ t per) (math/floor (+ (/ t per) 0.5)))))))

(def envwidth 1000)
(def envheight 560)
(def triangle_amp 200)

(defn setup []
  (q/frame-rate 30)
  {:triwave (triangle_wave 200 triangle_amp)
   :epoch 0
   :translation_rate 4
   :center_x 0
   :center_y (/ envheight 3)})

(defn update-state [state]
  (q/frame-rate 30)
  {:triwave (:triwave state)
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


(defn gen_offset [epoch noiselevel dot_index]
  (gen/gauss 0 noiselevel))

(def offset_mem (memoize gen_offset))

(defn draw-state [state]
  (q/background 0)
  (q/fill 255 255 255)
  (q/stroke 255 255 255)
  (let [edge_noise 0
        center_noise 0
        global_noise 0
        y ((:triwave state) (:center_x state))]
    (q/with-translation [(gen/gauss (:center_x state) global_noise)
                         (gen/gauss (:center_y state) 0)]
      (cond (> (:center_x state) (/ envwidth 2))
            (do
              (q/ellipse (gen/gauss 0 edge_noise) 0 10 10)
              (q/ellipse (gen/gauss 0 edge_noise) triangle_amp 10 10)))
      (q/ellipse (gen/gauss 0 center_noise) y 10 10))))

(q/defsketch diagonal
  :title "diagonal motion illusion"
  :size [envwidth envheight]
  :setup setup
  :draw draw-state
  :update update-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])


; Vid List: 8 total: noise free for first slide
; second slide and third slide: low noise (10) edge, center, full. high noise too (30). high noise
; on only one edge too. 

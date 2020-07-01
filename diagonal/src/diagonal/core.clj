(ns diagonal.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [clojure.math.numeric-tower :as math]))

(defn triangle_wave
  [per amp]
  (fn [t]
    (* (* amp 2)
       (math/abs
        (- (/ t per) (math/floor (+ (/ t per) 0.5)))))))

(defn setup []
  (q/frame-rate 30)
  {:triwave (triangle_wave 200 200)})

(defn draw-state [state]
  (q/background 0)
  (q/fill 255 255 255)
  (let [speed 3
        y ((:triwave state) (* speed (q/frame-count)))]
    (q/with-translation [(* speed (q/frame-count))
                         (/ (q/height) 4)]
      (cond (> (* speed (q/frame-count)) 500)
            (do
              (q/ellipse 0 0 10 10)
              (q/ellipse 0 (* 0.4 (q/height)) 10 10)))
      (q/ellipse 0 y 10 10))))

(q/defsketch diagonal
  :title "diagonal motion illusion"
  :size [1500 500]
  :setup setup
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])

(ns sin_ellipse.core
  (:require [overtone.live :as ot]
            [quil.core :as q]
            [genartlib.random :as gen]
            [clojure.math.numeric-tower :as math]
            [quil.middleware :as m]))

(def envwidth 1000)
(def envheight 560)

;; (defn tone
;;   [dur]
;;   (fn [freq]
;;     (ot/demo 
;;      (ot/sin-osc dur (+ 200 freq)))))

(defn tone
  [dur]
  (fn [freq]
    (ot/demo 0.0333 (ot/sin-osc (+ 400 freq)))))


(defn setup []
  (q/frame-rate 30)
  {:angle 0
   :tone_func (tone 0.03)
   :y_bump 0})


(defn update-state [state]
  {:angle (+ (:angle state) 0.05)
;   :y_bump (:y_bump state)
   :tone_func (:tone_func state)  
   :y_bump (+ (:y_bump state) (* 2 (q/sin (:angle state))))
   })

(defn draw-state [state]
  (q/background 0 0 0)
  (q/stroke 255 255 255)
  (q/fill 255 255 255)
  (let [angle (:angle state)
        stim_height (/ envheight 6)
        x (* (/ envwidth 4) (q/cos angle))
        y (* stim_height (q/sin (* (/ q/PI q/PI) angle)))
        ellipse_bump (cond (neg? y)
                           (- (:y_bump state))
                           :else (:y_bump state))]

    (cond (= (mod (q/frame-count) 3) 0)
          ((:tone_func state) (* 1 (:y_bump state)))) 
    (q/with-translation [(/ (q/width) 2)
                         (/ (q/height) 2)]
      (q/ellipse (gen/gauss x 0) (+ stim_height (:y_bump state)) 10 10)
      (q/ellipse (gen/gauss x 0) (- (- stim_height) (:y_bump state)) 10 10)
      (q/ellipse x (+ y 0) 10 10)
      )))

(q/defsketch sin_ellipse
  :title "3D motion illusion"
  :size [envwidth envheight]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  ; This sketch uses functional-mode middleware.
  ; Check quil wiki for more info about middlewares and particularly
  ; fun-mode.
  :middleware [m/fun-mode])

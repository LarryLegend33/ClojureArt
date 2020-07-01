(ns pendulum.core
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [clojure.math.numeric-tower :as math]))

; creates a pendulum that oscillates 
; with sine dynamics. have to add -2 * length 
; b/c y axis is inverted. yields x and y coords at a given time
(defn pendulum_xy
  [rng length]
  (fn [t]
    (list (* rng (q/cos t))
          (* -1 (+ (* -2 length)
                   (math/sqrt (+ (math/expt (* rng (q/cos t)) 2)
                                 (math/expt length 2))))))))

(defn setup []
  (q/frame-rate 30)
  {:angle 0
   :leg_pendulum (pendulum_xy 30 100)
   :arm_pendulum (pendulum_xy 20 100)})


(defn update-state [state]
  (update-in state [:angle] (fn [a] (+ a 0.1))))
  ;; {:angle (+ (:angle state) 0.1)
  ;;  :pendulum (pendulum_xy 50 (* 100 (math/abs (q/cos (:angle state)))))
  ;;  })
  

(defn draw-state [state]
  (q/background 0)
  (q/fill 255 255 255)
  (q/stroke 255 255 255)
  (let [angle (:angle state)
        legcoords ((:leg_pendulum state) angle)
        armcoords ((:arm_pendulum state) angle)]
    (q/with-translation [(/ (q/frame-count) 0.5)
                         (+ (q/sin angle) (/ (q/height) 2))]
      ;; (cond (> (q/frame-count) 300)
      ;;       (q/ellipse 0 0 10 10))
      (cond (> (q/frame-count) 300)
            (do 
              (q/ellipse 0 -50 10 10)
              (q/ellipse 20 -100 10 10)
              (q/ellipse 0 0 10 10)))

      (q/ellipse (* -1 (first legcoords)) (second legcoords) 10 10)
      (q/ellipse (first legcoords) (second legcoords) 10 10)

      (q/with-rotation [(/ q/PI 20)]
        (q/ellipse (* 1 (first armcoords)) (+ -100 (second armcoords)) 10 10)
        (q/ellipse (* -1 (first armcoords)) (+ -100 (second armcoords)) 10 10)))))


    ;; (q/with-translation [(q/frame-count) 100]
    ;;   (q/with-rotation [(/ q/PI 1)]
    ;;     (q/rect 100 0 100 200)))))

(q/defsketch pendulum
  :title "Pendlulum"
  :size [1200 500]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])


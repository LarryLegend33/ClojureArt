(ns looming.core
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(def start-angle 4)
(def end-angle 140)
(def size-velocity-ratio 240)
(def frame-rate 60)
(def fish-to-screen 10)
(def env-size [480 320])

(defn mm-to-pix [w]
  (* w 25.4 (/ 3.5 (first env-size))))

(defn deg-to-mm [ang]
  (* (q/tan (q/radians (/ ang 2))) fish-to-screen 2))

(defn deg-to-delta-deg [ang]
  (let [delta-ms (/ ang size-velocity-ratio)
        frame-ms (* 1000 (/ 1 frame-rate))]
    (float (* delta-ms frame-ms))))

(defn create-size-vector []
  (loop [angle-vec [start-angle]]
    (let [curr-angle (last angle-vec)]
    (if (> curr-angle end-angle)
      (map deg-to-mm angle-vec) 
      (recur (conj angle-vec (+ curr-angle (deg-to-delta-deg curr-angle))))))))

(defn setup []
  (q/frame-rate frame-rate)
  {:size-vector (create-size-vector)})

(defn update-state [state]
  {:size-vector (rest (:size-vector state))})

(defn draw-state [state]
  (let [loom-size (if (seq (:size-vector state))
                    (mm-to-pix (first (:size-vector state)))
                    (-> end-angle
                        deg-to-mm
                        mm-to-pix))]
    (q/background 220)
    (q/fill 0 0 0)
    (q/ellipse (/ (first env-size) 2) 50 loom-size loom-size)))

(q/defsketch looming
  :title "You spin my circle right round"
  :size [(first env-size) (second env-size)]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])



  ;; deg / (deg / ms) = 240
  ;; (deg / ms) = v = deg / 240
  ;; this will tell you how much to grow it per ms. multiply it by
  ;; your time div (16.667) and add to the current angle to get the next angle.
  ;; so you start with 4. you do 4/240 to get the growth in deg per ms.
  ;; multply it by 16.667. recur on this until the size hits 140.
  ;; 4 to 140 deg.
  ;; d = 5 mm
  ;; pix to w = 480 pix = 3.5 in.
  ;; 1 pix = .185 mm.
  ;; 1 mm = 5.4 pix 
  ;; update is frame / 16.666 ms,
  ;; so each frame is /16.666 ms.
  ;; first frame will be 
  ;; deg_to_width = (tan(ang / 2) * d) * 2
  ;; width_to_deg = arctan((w / 2) / d) * 2)

  
